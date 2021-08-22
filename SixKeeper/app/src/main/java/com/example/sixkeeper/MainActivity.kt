package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle

class MainActivity : ChangeStatusBarToWhiteClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor()
        //setButtonOnClick()

        val goToLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        this.finish()
    }

    /*private fun setButtonOnClick() {
        val acbMainLogin: Button = findViewById(R.id.acbMainLogin)
        val acbMainCreateNewAccount: Button = findViewById(R.id.acbMainCreateNewAccount)

        acbMainLogin.setOnClickListener {
            val goToLoginActivity = Intent(this, LoginActivity::class.java)

            startActivity(goToLoginActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                acbMainLogin.isClickable = false
                postDelayed(
                        {
                            acbMainLogin.isClickable = true
                        }, 1000
                )
            }
        }

        acbMainCreateNewAccount.setOnClickListener {
            val goToCreateNewAccountActivity = Intent(
                    this,
                    CreateNewAccountActivity::class.java
            )

            startActivity(goToCreateNewAccountActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                acbMainCreateNewAccount.isClickable = false
                postDelayed(
                        {
                            acbMainCreateNewAccount.isClickable = true
                        }, 1000
                )
            }
        }
    }*/
}