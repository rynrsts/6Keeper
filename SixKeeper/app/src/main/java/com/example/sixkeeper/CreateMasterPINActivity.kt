package com.example.sixkeeper

import android.os.Bundle

class CreateMasterPINActivity : CreateMasterPINProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_master_p_i_n)

        changeStatusBarColor()
        setVariables()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        getAcbCreateMasterPINButton1().setOnClickListener {
            pushNumber(1, it)
        }

        getAcbCreateMasterPINButton2().setOnClickListener {
            pushNumber(2, it)
        }

        getAcbCreateMasterPINButton3().setOnClickListener {
            pushNumber(3, it)
        }

        getAcbCreateMasterPINButton4().setOnClickListener {
            pushNumber(4, it)
        }

        getAcbCreateMasterPINButton5().setOnClickListener {
            pushNumber(5, it)
        }

        getAcbCreateMasterPINButton6().setOnClickListener {
            pushNumber(6, it)
        }

        getAcbCreateMasterPINButton7().setOnClickListener {
            pushNumber(7, it)
        }

        getAcbCreateMasterPINButton8().setOnClickListener {
            pushNumber(8, it)
        }

        getAcbCreateMasterPINButton9().setOnClickListener {
            pushNumber(9, it)
        }

        getAcbCreateMasterPINButton0().setOnClickListener {
            pushNumber(0, it)
        }

        getAcbCreateMasterPINButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbCreateMasterPINButtonCancel().setOnClickListener {
            it.apply {
                getAcbCreateMasterPINButtonCancel().isClickable = false                             // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getAcbCreateMasterPINButtonCancel().isClickable = true
                        }, 1000
                )
            }

            onBackPressed()
        }
    }
}