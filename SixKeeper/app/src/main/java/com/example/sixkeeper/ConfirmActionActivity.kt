package com.example.sixkeeper

import android.os.Bundle

class ConfirmActionActivity : ConfirmActionProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_action)

        changeStatusBarColor()
        setVariables()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        getAcbConfirmActionButton1().setOnClickListener {
            pushNumber(1)
        }

        getAcbConfirmActionButton2().setOnClickListener {
            pushNumber(2)
        }

        getAcbConfirmActionButton3().setOnClickListener {
            pushNumber(3)
        }

        getAcbConfirmActionButton4().setOnClickListener {
            pushNumber(4)
        }

        getAcbConfirmActionButton5().setOnClickListener {
            pushNumber(5)
        }

        getAcbConfirmActionButton6().setOnClickListener {
            pushNumber(6)
        }

        getAcbConfirmActionButton7().setOnClickListener {
            pushNumber(7)
        }

        getAcbConfirmActionButton8().setOnClickListener {
            pushNumber(8)
        }

        getAcbConfirmActionButton9().setOnClickListener {
            pushNumber(9)
        }

        getAcbConfirmActionButton0().setOnClickListener {
            pushNumber(0)
        }

        getAcbConfirmActionButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbConfirmActionButtonCancel().setOnClickListener {
            it.apply {
                getAcbConfirmActionButtonCancel().isClickable = false                               // Set button un-clickable for 1 second
                postDelayed(
                    {
                        getAcbConfirmActionButtonCancel().isClickable = true
                    }, 1000
                )
            }

            onBackPressed()
        }
    }
}