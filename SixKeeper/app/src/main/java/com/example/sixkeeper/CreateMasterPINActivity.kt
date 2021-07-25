package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class CreateMasterPINActivity : CreateMasterPINProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_master_p_i_n)

        changeStatusBarColor()
        setVariables()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val acbCreateMasterPINButton1: Button = findViewById(R.id.acbCreateMasterPINButton1)
        val acbCreateMasterPINButton2: Button = findViewById(R.id.acbCreateMasterPINButton2)
        val acbCreateMasterPINButton3: Button = findViewById(R.id.acbCreateMasterPINButton3)
        val acbCreateMasterPINButton4: Button = findViewById(R.id.acbCreateMasterPINButton4)
        val acbCreateMasterPINButton5: Button = findViewById(R.id.acbCreateMasterPINButton5)
        val acbCreateMasterPINButton6: Button = findViewById(R.id.acbCreateMasterPINButton6)
        val acbCreateMasterPINButton7: Button = findViewById(R.id.acbCreateMasterPINButton7)
        val acbCreateMasterPINButton8: Button = findViewById(R.id.acbCreateMasterPINButton8)
        val acbCreateMasterPINButton9: Button = findViewById(R.id.acbCreateMasterPINButton9)
        val acbCreateMasterPINButton0: Button = findViewById(R.id.acbCreateMasterPINButton0)
        val acbCreateMasterPINButtonDelete: Button =
                findViewById(R.id.acbCreateMasterPINButtonDelete)
        val acbCreateMasterPINButtonCancel: Button =
                findViewById(R.id.acbCreateMasterPINButtonCancel)

        acbCreateMasterPINButton1.setOnClickListener {
            pushNumber(1)
        }

        acbCreateMasterPINButton2.setOnClickListener {
            pushNumber(2)
        }

        acbCreateMasterPINButton3.setOnClickListener {
            pushNumber(3)
        }

        acbCreateMasterPINButton4.setOnClickListener {
            pushNumber(4)
        }

        acbCreateMasterPINButton5.setOnClickListener {
            pushNumber(5)
        }

        acbCreateMasterPINButton6.setOnClickListener {
            pushNumber(6)
        }

        acbCreateMasterPINButton7.setOnClickListener {
            pushNumber(7)
        }

        acbCreateMasterPINButton8.setOnClickListener {
            pushNumber(8)
        }

        acbCreateMasterPINButton9.setOnClickListener {
            pushNumber(9)
        }

        acbCreateMasterPINButton0.setOnClickListener {
            pushNumber(0)
        }

        acbCreateMasterPINButtonDelete.setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        acbCreateMasterPINButtonCancel.setOnClickListener {
            onBackPressed()
            setResult(0, Intent().putExtra("finish", 0))
            overridePendingTransition(
                    R.anim.anim_0,
                    R.anim.anim_exit_top_to_bottom_2
            )
        }
    }
}