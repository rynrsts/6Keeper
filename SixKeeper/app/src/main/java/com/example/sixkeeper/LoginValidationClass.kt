package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

open class LoginValidationClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private lateinit var userId: String
    private lateinit var username: String
    private lateinit var password: String

    private lateinit var encryptedUsername: String
    private lateinit var encryptedPassword: String

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        ivLoginTogglePass = findViewById(R.id.ivLoginTogglePass)
    }

    fun getEtLoginPassword(): EditText {
        return etLoginPassword
    }

    fun getIvLoginTogglePass(): ImageView {
        return ivLoginTogglePass
    }

    fun setPassword(s: String) {
        password = s
    }

    fun getPassword(): String {
        return password
    }

    fun requestPermission() {                                                                       // Request permission
        ActivityCompat.requestPermissions(
                this,
                arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                52420
        )
    }

    fun createFolder() {                                                                            // Create folder
        @Suppress("DEPRECATION")
        val directory = File(Environment.getExternalStorageDirectory(), "SixKeeper")
        directory.mkdirs()
    }

    fun blockCapture() {
        window.setFlags(                                                                            // Block capture
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts are not empty
        username = etLoginUsername.text.toString()
        password = etLoginPassword.text.toString()

        return username.isNotEmpty() && password.isNotEmpty()
    }

    fun validateUserCredential() {                                                                  // Validate username and password
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()

        if (userAccList.isEmpty()) {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.login_invalid_credentials,
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }

            etLoginPassword.setText("")
            getEtLoginPassword().requestFocus()

            return
        }

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        encryptedUsername = encryptionClass.encrypt(etLoginUsername.text.toString(), userId)
        encryptedPassword = encryptionClass.encryptOnly(etLoginPassword.text.toString(), userId)
        val button = Button(this)

        databaseReference = firebaseDatabase.getReference(userId)

        val usernameRef = databaseReference.child("username")
        val passwordRef = databaseReference.child("password")
        val statusRef = databaseReference.child("status")
        val pwWrongAttemptRef = databaseReference.child("pwWrongAttempt")
        val pwLockTimeRef = databaseReference.child("pwLockTime")

        var username = ""
        var password = ""
        var status = ""
        var pwWrongAttempt = ""
        var pwLockTime = ""
        var count = 0

        passwordRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                password = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                status = encryptionClass.decrypt(value, userId)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        pwWrongAttemptRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                pwWrongAttempt = encryptionClass.decrypt(value, userId)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        pwLockTimeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                pwLockTime = encryptionClass.decrypt(value, userId)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        usernameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    username = dataSnapshot.getValue(String::class.java).toString()
                    count++

                    if (count == 1) {
                        button.performClick()
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            applicationContext,
                            R.string.login_deactivated,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }

                    etLoginPassword.setText("")
                    getEtLoginPassword().requestFocus()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val waitingTime = waitingTime(pwWrongAttempt, pwLockTime)

            if (
                    encryptedUsername == username && encryptedPassword == password &&
                    0.toString() == status
            ) {
                if (waitingTime == 0.toLong()) {
                    restartAttemptAndTime()
                    updateUserStatus()

                    closeKeyboard()

                    val goToMasterPINActivity = Intent(
                            this,
                            MasterPINActivity::class.java
                    )
                    startActivity(goToMasterPINActivity)
                    overridePendingTransition(
                            R.anim.anim_enter_bottom_to_top_2,
                            R.anim.anim_0
                    )

                    this.finish()
                } else {
                    lockToast(waitingTime)
                }
            } else if (
                    encryptedUsername == username && encryptedPassword == password &&
                    0.toString() != status
            ) {
                if (waitingTime == 0.toLong()) {
                    val toast: Toast = Toast.makeText(
                            applicationContext,
                            R.string.login_already_logged_in,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }

                    etLoginPassword.setText("")
                    getEtLoginPassword().requestFocus()
                } else {
                    lockToast(waitingTime)
                }
            } else if (encryptedUsername == username) {
                if (waitingTime == 0.toLong()) {
                    updateAccountStatus(pwWrongAttempt)
                } else {
                    lockToast(waitingTime)
                }

                etLoginPassword.setText("")
                getEtLoginPassword().requestFocus()
            } else {
                val toast: Toast = Toast.makeText(
                        applicationContext,
                        R.string.login_invalid_credentials,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }

                etLoginPassword.setText("")
                getEtLoginPassword().requestFocus()
            }
        }
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)             // Close keyboard
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun waitingTime(pwWrongAttempt: String, pwLockTime: String): Long {
        var waitingTime: Long = 0

        if (pwWrongAttempt.isNotEmpty()) {
            val wrongAttempts = Integer.parseInt(pwWrongAttempt)

            if (wrongAttempts % 3 == 0) {
                if (pwLockTime.isNotEmpty()) {
                    val dateToday: Date = dateFormat.parse(getCurrentDate())
                    val lockeDate: Date = dateFormat.parse(pwLockTime)
                    val timeDifference: Long = dateToday.time - lockeDate.time
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference)
                    val loop = wrongAttempts / 3
                    var timer = 30

                    for (i in 1 until loop) {
                        timer *= 2
                    }

                    if (seconds < timer) {
                        waitingTime = timer - seconds
                    }
                }
            }
        }

        return waitingTime
    }

    private fun getCurrentDate(): String {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        return dateFormat.format(Date(elapsedRealtime)).toString()
    }

    private fun restartAttemptAndTime() {
        databaseReference.child("pwWrongAttempt").setValue("")
        databaseReference.child("pwLockTime").setValue("")
    }

    private fun updateUserStatus() {                                                                // Update account status to 1
        val encryptedActiveStatus = encryptionClass.encrypt(
                1.toString() + "ramjcammjar" + getUniquePseudoID(),
                userId
        )
        databaseReference.child("status").setValue(encryptedActiveStatus)
    }
    
    private fun lockToast(waitingTime: Long) {
        var sec = ""

        if (waitingTime == 1.toLong()) {
            sec = "second"
        } else if (waitingTime > 1.toLong()) {
            sec = "seconds"
        }

        val toast: Toast = Toast.makeText(
                applicationContext,
                "Account is locked. Please wait for $waitingTime $sec",
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun getUniquePseudoID(): String {
        @Suppress("DEPRECATION")
        val mSzDevIDShort = "35" +
                Build.BOARD.length % 10 +
                Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 +
                Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 +
                Build.HOST.length % 10 +
                Build.ID.length % 10 +
                Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 +
                Build.TYPE.length % 10 +
                Build.USER.length % 10
        var serial: String

        try {
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            serial = Build::class.java.getField("SERIAL")[null].toString()

            return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: java.lang.Exception) {
            serial = "serial"
        }

        return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    private fun updateAccountStatus(pwWrongAttempt: String) {                                       // Update password wrong attempt
        var wrongAttempt = 0
        var timer = 30

        if (pwWrongAttempt.isNotEmpty()) {
            wrongAttempt = Integer.parseInt(pwWrongAttempt)
        }

        wrongAttempt++

        val encryptedWrongAttempt = encryptionClass.encrypt(wrongAttempt.toString(), userId)

        databaseReference.child("pwWrongAttempt").setValue(encryptedWrongAttempt)

        if (wrongAttempt % 3 == 0) {
            for (i in 1 until (wrongAttempt / 3)) {
                timer *= 2
            }

            val encryptedCurrentDate = encryptionClass.encrypt(getCurrentDate(), userId)

            databaseReference.child("pwLockTime").setValue(encryptedCurrentDate)

            val toast: Toast = Toast.makeText(
                    applicationContext,
                    "Account is locked. Please wait for $timer seconds",
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        } else {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.login_invalid_credentials,
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    fun setUserIdAfterImport() {
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()

        if (userAccList.isNotEmpty()) {
            for (u in userAccList) {
                userId = encryptionClass.decode(u.userId)
            }
        }

        databaseReference = firebaseDatabase.getReference(userId)
    }
}