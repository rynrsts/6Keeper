package com.example.sixkeeper

import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MasterPINActivity : MasterPINProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_p_i_n)

        changeStatusBarColor()
        setVariables()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val tvMasterPINLogout: TextView = findViewById(R.id.tvMasterPINLogout)

        getAcbMasterPINButton1().setOnClickListener {
            pushNumber(1)
        }

        getAcbMasterPINButton2().setOnClickListener {
            pushNumber(2)
        }

        getAcbMasterPINButton3().setOnClickListener {
            pushNumber(3)
        }

        getAcbMasterPINButton4().setOnClickListener {
            pushNumber(4)
        }

        getAcbMasterPINButton5().setOnClickListener {
            pushNumber(5)
        }

        getAcbMasterPINButton6().setOnClickListener {
            pushNumber(6)
        }

        getAcbMasterPINButton7().setOnClickListener {
            pushNumber(7)
        }

        getAcbMasterPINButton8().setOnClickListener {
            pushNumber(8)
        }

        getAcbMasterPINButton9().setOnClickListener {
            pushNumber(9)
        }

        getAcbMasterPINButton0().setOnClickListener {
            pushNumber(0)
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

        tvMasterPINLogout.setOnClickListener {
            it.apply {
                tvMasterPINLogout.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvMasterPINLogout.isClickable = true
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
    }
}