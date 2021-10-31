package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.os.Environment
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

open class LoginValidationClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var userId: String

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()

        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        ivLoginTogglePass = findViewById(R.id.ivLoginTogglePass)
    }

    fun setEtLoginPassword(s: String) {
        etLoginPassword.setText(s)
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

    fun validateUserCredential(): Boolean {                                                         // Validate username and password
        val encryptionClass = EncryptionClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        val encodedUsername = encodingClass.encodeData(etLoginUsername.text.toString())
        val encodedPassword = encodingClass.encodeData(etLoginPassword.text.toString())
        val encryptedPassword = encryptionClass.hashData(encodedPassword)

        for (u in userAccList) {
            userId = u.userId
            bool = encodedUsername == u.username && encryptedPassword.contentEquals(u.password)
        }

        return bool
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

    fun updateUserStatus() {                                                                        // Update account status to 1
        databaseHandlerClass.updateUserStatus(
                userId,
                encodingClass.encodeData(1.toString())
        )
    }
}