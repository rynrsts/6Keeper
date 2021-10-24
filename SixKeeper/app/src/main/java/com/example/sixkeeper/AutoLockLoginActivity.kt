package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class AutoLockLoginActivity : AutoLockLoginProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_lock_login)

        changeStatusBarColor()
        setVariables()
        blockCapture()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val tvAutoLockLoginForgotPass: TextView = findViewById(R.id.tvAutoLockLoginForgotPass)

        getAcbAutoLockLoginButton1().setOnClickListener {
            pushNumber(1)
        }

        getAcbAutoLockLoginButton2().setOnClickListener {
            pushNumber(2)
        }

        getAcbAutoLockLoginButton3().setOnClickListener {
            pushNumber(3)
        }

        getAcbAutoLockLoginButton4().setOnClickListener {
            pushNumber(4)
        }

        getAcbAutoLockLoginButton5().setOnClickListener {
            pushNumber(5)
        }

        getAcbAutoLockLoginButton6().setOnClickListener {
            pushNumber(6)
        }

        getAcbAutoLockLoginButton7().setOnClickListener {
            pushNumber(7)
        }

        getAcbAutoLockLoginButton8().setOnClickListener {
            pushNumber(8)
        }

        getAcbAutoLockLoginButton9().setOnClickListener {
            pushNumber(9)
        }

        getAcbAutoLockLoginButton0().setOnClickListener {
            pushNumber(0)
        }

        getAcbAutoLockLoginButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbAutoLockLoginButtonCancel().setOnClickListener {
            it.apply {
                getAcbAutoLockLoginButtonCancel().isClickable = false                               // Set button un-clickable for 1 second
                postDelayed(
                    {
                        getAcbAutoLockLoginButtonCancel().isClickable = true
                    }, 1000
                )
            }

            onBackPressed()
        }

        tvAutoLockLoginForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                R.anim.anim_enter_right_to_left_2,
                R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                tvAutoLockLoginForgotPass.isClickable = false                                       // Set button un-clickable for 1 second
                postDelayed(
                    {
                        tvAutoLockLoginForgotPass.isClickable = true
                    }, 1000
                )
            }
        }
    }
}