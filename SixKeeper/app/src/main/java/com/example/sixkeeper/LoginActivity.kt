package com.example.sixkeeper

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog

class LoginActivity : LoginValidationClass() {
    private var passwordVisibility: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeStatusBarColor()
        setVariables()
        blockCapture()
        setButtonOnClick()
        setEditTextOnChange()
        setImageViewOnClick()
    }

    private fun setButtonOnClick() {
        val acbLoginLogin: Button = findViewById(R.id.acbLoginLogin)
        val acbLoginCreateNewAccount: Button = findViewById(R.id.acbLoginCreateNewAccount)

        acbLoginLogin.setOnClickListener {
            if (isNotEmpty()) {
                if (validateUserCredential()) {
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
                    val toast: Toast = Toast.makeText(
                            applicationContext,
                            R.string.login_invalid_credentials,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }

                    setEtLoginPassword("")
                    getEtLoginPassword().requestFocus()
                }
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

            it.apply {
                getEtLoginPassword().isClickable = false                                            // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getEtLoginPassword().isClickable = true
                        }, 1000
                )
            }
        }

        acbLoginCreateNewAccount.setOnClickListener {
            val goToCreateNewAccountActivity =
                    Intent(this, CreateNewAccountActivity::class.java)

            startActivity(goToCreateNewAccountActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                acbLoginCreateNewAccount.isClickable = false                                        // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbLoginCreateNewAccount.isClickable = true
                        }, 1000
                )
            }
        }
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)                   // Close keyboard
        }
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        getEtLoginPassword().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setPassword(getEtLoginPassword().text.toString())

                if (getPassword().isNotEmpty()) {
                    if (passwordVisibility == 0) {
                        getIvLoginTogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        passwordVisibility = 1

                        getEtLoginPassword().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtLoginPassword().text.length)
                        }
                    }
                } else {
                    getIvLoginTogglePass().setImageResource(0)
                    passwordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        getIvLoginTogglePass().setOnClickListener {
            when (passwordVisibility) {
                1 -> {                                                                              // Show password
                    getIvLoginTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtLoginPassword().apply {
                        transformationMethod = null
                        setSelection(getEtLoginPassword().text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    getIvLoginTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtLoginPassword().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtLoginPassword().text.length)
                    }
                    passwordVisibility = 1
                }
            }
        }
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.many_exit_mes)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }
}