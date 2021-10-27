package com.example.sixkeeper

import android.Manifest
import android.os.Environment
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import java.io.File

open class LoginValidationClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

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
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
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

    fun updateUserStatus() {                                                                        // Update account status to 1
        databaseHandlerClass.updateUserStatus(
                userId,
                encodingClass.encodeData(1.toString())
        )
    }
}