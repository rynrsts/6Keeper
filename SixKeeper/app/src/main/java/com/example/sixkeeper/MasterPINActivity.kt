package com.example.sixkeeper

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MasterPINActivity : MasterPINProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_p_i_n)

        changeStatusBarColor()
        setVariables()
        blockCapture()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val llMasterPinLogout: LinearLayout = findViewById(R.id.llMasterPinLogout)
        val tvMasterPINForgotPass: TextView = findViewById(R.id.tvMasterPINForgotPass)

        getAcbMasterPINButton1().setOnClickListener {
            pushNumber(1, it)
        }

        getAcbMasterPINButton2().setOnClickListener {
            pushNumber(2, it)
        }

        getAcbMasterPINButton3().setOnClickListener {
            pushNumber(3, it)
        }

        getAcbMasterPINButton4().setOnClickListener {
            pushNumber(4, it)
        }

        getAcbMasterPINButton5().setOnClickListener {
            pushNumber(5, it)
        }

        getAcbMasterPINButton6().setOnClickListener {
            pushNumber(6, it)
        }

        getAcbMasterPINButton7().setOnClickListener {
            pushNumber(7, it)
        }

        getAcbMasterPINButton8().setOnClickListener {
            pushNumber(8, it)
        }

        getAcbMasterPINButton9().setOnClickListener {
            pushNumber(9, it)
        }

        getAcbMasterPINButton0().setOnClickListener {
            pushNumber(0, it)
        }

        getAcbMasterPINButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbMasterPINButtonCancel().setOnClickListener {
            it.apply {
                getAcbMasterPINButtonCancel().isClickable = false                                   // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getAcbMasterPINButtonCancel().isClickable = true
                        }, 1000
                )
            }

            onBackPressed()
        }

        llMasterPinLogout.setOnClickListener {
            it.apply {
                llMasterPinLogout.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            llMasterPinLogout.isClickable = true
                        }, 1000
                )
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.many_logout_mes)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                updateUserStatus()
                updateLastLogin()
                goToLoginActivity()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title_confirm)
            alert.show()
        }

        tvMasterPINForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                    Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                tvMasterPINForgotPass.isClickable = false                                           // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvMasterPINForgotPass.isClickable = true
                        }, 1000
                )
            }
        }
    }
}