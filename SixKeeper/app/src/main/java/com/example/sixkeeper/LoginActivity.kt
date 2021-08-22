package com.example.sixkeeper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast

class LoginActivity : LoginValidationClass() {
    private var passwordVisibility: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeStatusBarColor()
        setVariables()
        setButtonOnClick()
        setEditTextOnChange()
        setImageViewOnClick()
    }

    private fun setButtonOnClick() {
        val acbLoginLogin: Button = findViewById(R.id.acbLoginLogin)
        val acbLoginCreateNewAccount: Button = findViewById(R.id.acbLoginCreateNewAccount)

        // TODO:
        acbLoginLogin.setOnClickListener {
//            if (isNotEmpty()) {
//                closeKeyboard()

                val goToMasterPINActivity = Intent(
                        this,
                        MasterPINActivity::class.java
                )

                @Suppress("DEPRECATION")
                startActivityForResult(goToMasterPINActivity, 16914)
                overridePendingTransition(
                        R.anim.anim_enter_bottom_to_top_2,
                        R.anim.anim_0
                )

                it.apply {
                    getEtLoginPassword().isClickable = false
                    postDelayed(
                            {
                                getEtLoginPassword().isClickable = true
                            }, 1000
                    )
                }
//            } else {
//                val toast: Toast = Toast.makeText(
//                    applicationContext,
//                    R.string.login_enter_credentials,
//                    Toast.LENGTH_SHORT
//                )
//                toast.apply {
//                    setGravity(Gravity.CENTER, 0, 0)
//                    show()
//                }
//            }
        }

        acbLoginCreateNewAccount.setOnClickListener {
            val goToCreateNewAccountActivity = Intent(this, CreateNewAccountActivity::class.java)

            startActivity(goToCreateNewAccountActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                acbLoginCreateNewAccount.isClickable = false
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
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {
                val goToIndexActivity = Intent(
                        this,
                        IndexActivity::class.java
                )

                startActivity(goToIndexActivity)
                overridePendingTransition(
                        R.anim.anim_enter_top_to_bottom_2,
                        R.anim.anim_exit_top_to_bottom_2
                )
                finish()
            }
        }
    }

    private fun setEditTextOnChange() {
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

    private fun setImageViewOnClick() {
        getIvLoginTogglePass().setOnClickListener {
            when (passwordVisibility) {
                1 -> {
                    getIvLoginTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtLoginPassword().apply {
                        transformationMethod = null
                        setSelection(getEtLoginPassword().text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {
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

    override fun onBackPressed() {                                                                  // Override back button
        super.onBackPressed()
        overridePendingTransition(
                R.anim.anim_enter_left_to_right_2,
                R.anim.anim_exit_left_to_right_2
        )
    }
}