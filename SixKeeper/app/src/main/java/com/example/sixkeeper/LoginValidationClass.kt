package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
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
    private lateinit var encodingClass: EncodingClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var userId: String

    private lateinit var encodedUsername: String
    private lateinit var encryptedPassword: ByteArray

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()
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
        val encryptionClass = EncryptionClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()

        encodedUsername = encodingClass.encodeData(etLoginUsername.text.toString())
        val encodedPassword = encodingClass.encodeData(etLoginPassword.text.toString())
        encryptedPassword = encryptionClass.hashData(encodedPassword)
        var uUsername = ""
        var uPassword: ByteArray? = null
        val passwordString = encodingClass.decodeSHA(encryptedPassword)

        val dataList = ArrayList<String>(0)
        val button = Button(this)

        for (u in userAccList) {
            userId = u.userId
            uUsername = u.username
            uPassword = u.password
        }

        val decodedUserId = encodingClass.decodeData(userId)
        databaseReference = firebaseDatabase.getReference(decodedUserId)

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataList.add(snapshot.getValue(String::class.java).toString())

                if (dataList.size == 9) {
                    button.performClick()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        button.setOnClickListener {
            val waitingTime = waitingTime()

            if (
                    encodedUsername == uUsername && encodedUsername == dataList[8] &&
                    encryptedPassword.contentEquals(uPassword) && passwordString == dataList[6]
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
            } else if (validateUsername()) {
                if (waitingTime == 0.toLong()) {
                    updateAccountStatus()
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

    private fun restartAttemptAndTime() {
        databaseHandlerClass.updateAccountStatus(
                "pw_wrong_attempt",
                ""
        )

        databaseHandlerClass.updateAccountStatus(
                "pw_lock_time",
                ""
        )
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun waitingTime(): Long {
        val userStatusList: List<UserAccountStatusModelClass> =
                databaseHandlerClass.viewAccountStatus()
        var waitingTime: Long = 0

        for (u in userStatusList) {
            val pwWrongAttempt = encodingClass.decodeData(u.pwWrongAttempt)

            if (pwWrongAttempt.isNotEmpty()) {
                val wrongAttempts = Integer.parseInt(pwWrongAttempt)

                if (wrongAttempts % 3 == 0) {
                    val pwLockDate = encodingClass.decodeData(u.pwLockTime)

                    if (pwLockDate.isNotEmpty()) {
                        val dateToday: Date = dateFormat.parse(getCurrentDate())
                        val lockeDate: Date = dateFormat.parse(pwLockDate)
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
        }

        return waitingTime
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    private fun updateUserStatus() {                                                                // Update account status to 1
        databaseHandlerClass.updateUserStatus(
                userId,
                encodingClass.encodeData(1.toString())
        )
    }

    private fun validateUsername(): Boolean {                                                       // Validate username
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        val encodedUsername = encodingClass.encodeData(etLoginUsername.text.toString())

        for (u in userAccList) {
            userId = u.userId
            bool = encodedUsername == u.username
        }

        return bool
    }

    private fun updateAccountStatus() {                                                             // Update password wrong attempt
        val userStatusList: List<UserAccountStatusModelClass> =
                databaseHandlerClass.viewAccountStatus()
        var wrongAttempt = 0
        var timer = 30

        for (u in userStatusList) {
            val pwWrongAttempt = encodingClass.decodeData(u.pwWrongAttempt)

            if (pwWrongAttempt.isNotEmpty()) {
                wrongAttempt = Integer.parseInt(pwWrongAttempt)
            }
        }
        wrongAttempt++

        databaseHandlerClass.updateAccountStatus(
                "pw_wrong_attempt",
                encodingClass.encodeData(wrongAttempt.toString())
        )

        if (wrongAttempt % 3 == 0) {
            for (i in 1 until (wrongAttempt / 3)) {
                timer *= 2
            }

            databaseHandlerClass.updateAccountStatus(
                    "pw_lock_time",
                    encodingClass.encodeData(getCurrentDate())
            )

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
}