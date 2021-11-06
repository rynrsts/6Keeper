package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ConfirmWithCredentialsActivity : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var etConfirmCredentialsUsername: EditText
    private lateinit var etConfirmCredentialsPassword: EditText
    private lateinit var ivConfirmCredentialsTogglePass: ImageView

    private lateinit var username: String
    private lateinit var password: String
    private var passwordVisibility: Int = 0
    private lateinit var userId: String

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_with_credentials)

        IndexActivity().setBackgroundDate()
        changeStatusBarColor()
        setVariables()
        blockCapture()
        setEditTextOnChange()
        setImageViewOnClick()
        setButtonOnClick()
    }

    private fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        etConfirmCredentialsUsername = findViewById(R.id.etConfirmCredentialsUsername)
        etConfirmCredentialsPassword = findViewById(R.id.etConfirmCredentialsPassword)
        ivConfirmCredentialsTogglePass = findViewById(R.id.ivConfirmCredentialsTogglePass)

        val userAccList = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = encodingClass.decodeData(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)
    }

    fun blockCapture() {
        window.setFlags(                                                                            // Block capture
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        etConfirmCredentialsPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                password = etConfirmCredentialsPassword.text.toString()

                if (password.isNotEmpty()) {
                    if (passwordVisibility == 0) {
                        ivConfirmCredentialsTogglePass.setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        passwordVisibility = 1

                        etConfirmCredentialsPassword.apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(etConfirmCredentialsPassword.text.length)
                        }
                    }
                } else {
                    ivConfirmCredentialsTogglePass.setImageResource(0)
                    passwordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        ivConfirmCredentialsTogglePass.setOnClickListener {
            when (passwordVisibility) {
                1 -> {                                                                              // Show password
                    ivConfirmCredentialsTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    etConfirmCredentialsPassword.apply {
                        transformationMethod = null
                        setSelection(etConfirmCredentialsPassword.text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    ivConfirmCredentialsTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    etConfirmCredentialsPassword.apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(etConfirmCredentialsPassword.text.length)
                    }
                    passwordVisibility = 1
                }
            }
        }
    }

    private fun setButtonOnClick() {
        val acbConfirmCredentialsConfirm: Button =
                findViewById(R.id.acbConfirmCredentialsConfirm)
        val tvConfirmCredentialsCancel: TextView = findViewById(R.id.tvConfirmCredentialsCancel)

        acbConfirmCredentialsConfirm.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (isNotEmpty()) {
                    validateUserCredential()
                } else {
                    val toast: Toast = Toast.makeText(
                            applicationContext,
                            R.string.login_enter_credentials,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            } else {
                val toast: Toast = Toast.makeText(
                        applicationContext,
                        R.string.many_internet_connection,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }

            it.apply {
                acbConfirmCredentialsConfirm.isClickable = false                                    // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbConfirmCredentialsConfirm.isClickable = true
                        }, 1000
                )
            }
        }

        tvConfirmCredentialsCancel.setOnClickListener { it ->
            it.apply {
                tvConfirmCredentialsCancel.isClickable = false                                      // Set un-clickable for 1 second
                postDelayed(
                        {
                            tvConfirmCredentialsCancel.isClickable = true
                        }, 1000
                )
            }

            val button = Button(this)

            val pwWrongAttemptRef = databaseReference.child("pwWrongAttempt")
            val pwLockTimeRef = databaseReference.child("pwLockTime")
            var pwWrongAttempt = ""
            var pwLockTime = ""
            var count = 0

            pwWrongAttemptRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    pwWrongAttempt = encodingClass.decodeData(value)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            pwLockTimeRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    pwLockTime = encodingClass.decodeData(value)
                    count++

                    if (count == 1) {
                        button.performClick()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            button.setOnClickListener {
                val waitingTime = waitingTime(pwWrongAttempt, pwLockTime)

                if (waitingTime == 0.toLong()) {
                    onBackPressed()
                } else {
                    val encodedInactiveStatus = encodingClass.encodeData(0.toString())
                    databaseReference.child("status").setValue(encodedInactiveStatus)
                }
            }
        }
    }

    private fun isNotEmpty(): Boolean {                                                             // Validate EditTexts are not empty
        username = etConfirmCredentialsUsername.text.toString()
        password = etConfirmCredentialsPassword.text.toString()

        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun validateUserCredential() {                                                          // Validate username and password
        val encryptionClass = EncryptionClass()

        val encodedUsername = encodingClass.encodeData(etConfirmCredentialsUsername.text.toString())
        val encodedPassword = encodingClass.encodeData(etConfirmCredentialsPassword.text.toString())
        val encryptedPassword = encryptionClass.hashData(encodedPassword)
        val passwordString = encodingClass.decodeSHA(encryptedPassword)
        val button = Button(this)

        val usernameRef = databaseReference.child("username")
        val passwordRef = databaseReference.child("password")
        val pwWrongAttemptRef = databaseReference.child("pwWrongAttempt")
        val pwLockTimeRef = databaseReference.child("pwLockTime")

        var username = ""
        var password = ""
        var pwWrongAttempt = ""
        var pwLockTime = ""
        var count = 0

        passwordRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                password = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        pwWrongAttemptRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                pwWrongAttempt = encodingClass.decodeData(value)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        pwLockTimeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                pwLockTime = encodingClass.decodeData(value)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        usernameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                username = dataSnapshot.getValue(String::class.java).toString()
                count++

                if (count == 1) {
                    button.performClick()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val waitingTime = waitingTime(pwWrongAttempt, pwLockTime)

            if (waitingTime == 0.toLong()) {
                if (encodedUsername == username && passwordString == password) {
                    databaseReference.child("pwWrongAttempt").setValue("")
                    databaseReference.child("pwLockTime").setValue("")

                    val immKeyboard: InputMethodManager = getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager

                    when {
                        immKeyboard.isActive ->
                            immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)       // Close keyboard
                    }

                    setResult(451320)
                    onBackPressed()

                    this.finish()
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

                    updateAccountStatus(pwWrongAttempt)

                    etConfirmCredentialsPassword.setText("")
                    etConfirmCredentialsPassword.requestFocus()
                }
            } else {
                lockToast(waitingTime)

                etConfirmCredentialsPassword.setText("")
                etConfirmCredentialsPassword.requestFocus()
            }
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

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
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

    private fun updateAccountStatus(pwWrongAttempt: String) {                                       // Update password wrong attempt
        var wrongAttempt = 0
        var timer = 30

        if (pwWrongAttempt.isNotEmpty()) {
            wrongAttempt = Integer.parseInt(pwWrongAttempt)
        }

        wrongAttempt++

        val encodedWrongAttempt = encodingClass.encodeData(wrongAttempt.toString())

        databaseReference.child("pwWrongAttempt").setValue(encodedWrongAttempt)

        if (wrongAttempt % 3 == 0) {
            for (i in 1 until (wrongAttempt / 3)) {
                timer *= 2
            }

            val encodedCurrentDate = encodingClass.encodeData(getCurrentDate())

            databaseReference.child("pwLockTime").setValue(encodedCurrentDate)

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

    override fun onBackPressed() {                                                                  // Override back button function
        finish()
        overridePendingTransition(R.anim.anim_0, R.anim.anim_exit_top_to_bottom_2)
    }
}