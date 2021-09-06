package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle

class MainActivity : ChangeStatusBarToWhiteClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor()
        goTo()
        //setButtonOnClick()
    }

    private fun goTo() {
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var status = 0

        for (u in userAccList) {
            status = u.accountStatus
        }

        if (status == 0) {
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            startActivity(goToLoginActivity)
        } else if (status == 1) {
            val goToMasterPINActivity = Intent(this, MasterPINActivity::class.java)
            startActivity(goToMasterPINActivity)
            overridePendingTransition(
                R.anim.anim_enter_bottom_to_top_2,
                R.anim.anim_0
            )
        }

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