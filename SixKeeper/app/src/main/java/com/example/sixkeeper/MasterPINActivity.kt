package com.example.sixkeeper

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
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
        val acbMasterPINButton1: Button = findViewById(R.id.acbMasterPINButton1)
        val acbMasterPINButton2: Button = findViewById(R.id.acbMasterPINButton2)
        val acbMasterPINButton3: Button = findViewById(R.id.acbMasterPINButton3)
        val acbMasterPINButton4: Button = findViewById(R.id.acbMasterPINButton4)
        val acbMasterPINButton5: Button = findViewById(R.id.acbMasterPINButton5)
        val acbMasterPINButton6: Button = findViewById(R.id.acbMasterPINButton6)
        val acbMasterPINButton7: Button = findViewById(R.id.acbMasterPINButton7)
        val acbMasterPINButton8: Button = findViewById(R.id.acbMasterPINButton8)
        val acbMasterPINButton9: Button = findViewById(R.id.acbMasterPINButton9)
        val acbMasterPINButton0: Button = findViewById(R.id.acbMasterPINButton0)
        val acbMasterPINButtonDelete: Button =
                findViewById(R.id.acbMasterPINButtonDelete)
        val acbMasterPINButtonCancel: Button =
                findViewById(R.id.acbMasterPINButtonCancel)

        val tvMasterPINLogout: TextView = findViewById(R.id.tvMasterPINLogout)

        acbMasterPINButton1.setOnClickListener {
            pushNumber(1)
        }

        acbMasterPINButton2.setOnClickListener {
            pushNumber(2)
        }

        acbMasterPINButton3.setOnClickListener {
            pushNumber(3)
        }

        acbMasterPINButton4.setOnClickListener {
            pushNumber(4)
        }

        acbMasterPINButton5.setOnClickListener {
            pushNumber(5)
        }

        acbMasterPINButton6.setOnClickListener {
            pushNumber(6)
        }

        acbMasterPINButton7.setOnClickListener {
            pushNumber(7)
        }

        acbMasterPINButton8.setOnClickListener {
            pushNumber(8)
        }

        acbMasterPINButton9.setOnClickListener {
            pushNumber(9)
        }

        acbMasterPINButton0.setOnClickListener {
            pushNumber(0)
        }

        acbMasterPINButtonDelete.setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        acbMasterPINButtonCancel.setOnClickListener {
            onBackPressed()
        }

        tvMasterPINLogout.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.many_logout_mes)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                updateUserStatus()
                goToLoginActivity()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title)
            alert.show()
        }
    }
}