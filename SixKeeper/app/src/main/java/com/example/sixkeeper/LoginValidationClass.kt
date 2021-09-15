package com.example.sixkeeper

import android.util.Base64
import android.widget.EditText
import android.widget.ImageView

open class LoginValidationClass : ChangeStatusBarToWhiteClass() {
    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var userId: String

    fun setVariables() {
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

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts are not empty
        username = etLoginUsername.text.toString()
        password = etLoginPassword.text.toString()

        return username.isNotEmpty() && password.isNotEmpty()
    }

    fun validateUserCredential(): Boolean {                                                         // Validate username and password
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        for (u in userAccList) {
            userId = u.userId
            bool = etLoginUsername.text.toString() == decodeData(u.username) &&
                    etLoginPassword.text.toString() == decodeData(u.password)
        }

        return bool
    }

    private fun decodeData(data: String): String {                                                  // Decode data using Base64
        val decrypt = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        return String(decrypt)
    }

    fun updateUserStatus() {                                                                        // Update account status to 1
        val accountStatus = 1
        val databaseHandlerClass = DatabaseHandlerClass(this)
        databaseHandlerClass.updateUserStatus(
                userId,
                encodeData(accountStatus.toString())
        )
    }

    private fun encodeData(data: String): String {                                                  // Encode data using Base64
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)
        return String(encrypt)
    }
}