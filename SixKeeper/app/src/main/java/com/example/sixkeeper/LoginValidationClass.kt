package com.example.sixkeeper

import android.widget.EditText
import android.widget.ImageView

open class LoginValidationClass : ChangeStatusBarToWhiteClass() {
    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var ivLoginTogglePass: ImageView

    private lateinit var username: String
    private lateinit var password: String

    private var userId = 0

    fun setVariables() {
        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        ivLoginTogglePass = findViewById(R.id.ivLoginTogglePass)
    }

    fun setEtLoginUsername(s: String) {
        etLoginUsername.setText(s)
    }

    fun getEtLoginUsername(): EditText {
        return etLoginUsername
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

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts to not empty
        username = etLoginUsername.text.toString()
        password = etLoginPassword.text.toString()

        return username.isNotEmpty() && password.isNotEmpty()
    }

    fun validateUserCredential(): Boolean {
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        for (u in userAccList) {
            userId = u.userId
            bool = etLoginUsername.text.toString() == u.username &&
                    etLoginPassword.text.toString() == u.password
        }

        return bool
    }

    fun updateUserStatus() {
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userUsername = ""
        val userPassword = ""
        val userMasterPIN = 0
        val userAccountStatus = 1

        databaseHandlerClass.updateUserStatus(
                UserAccModelClass(
                        userId,
                        userUsername,
                        userPassword,
                        userMasterPIN,
                        userAccountStatus
                )
        )
    }
}