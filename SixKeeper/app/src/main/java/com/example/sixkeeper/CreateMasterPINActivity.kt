package com.example.sixkeeper

import android.os.Bundle
import android.view.View

class CreateMasterPINActivity : CreateMasterPINProcessClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_master_p_i_n)

        changeStatusBarColor()
        setVariables()
//        blockCapture()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        getAcbCreateMasterPINButton1().setOnClickListener {
            clickEffect(it)
            pushNumber(1, it)
        }

        getAcbCreateMasterPINButton2().setOnClickListener {
            clickEffect(it)
            pushNumber(2, it)
        }

        getAcbCreateMasterPINButton3().setOnClickListener {
            clickEffect(it)
            pushNumber(3, it)
        }

        getAcbCreateMasterPINButton4().setOnClickListener {
            clickEffect(it)
            pushNumber(4, it)
        }

        getAcbCreateMasterPINButton5().setOnClickListener {
            clickEffect(it)
            pushNumber(5, it)
        }

        getAcbCreateMasterPINButton6().setOnClickListener {
            clickEffect(it)
            pushNumber(6, it)
        }

        getAcbCreateMasterPINButton7().setOnClickListener {
            clickEffect(it)
            pushNumber(7, it)
        }

        getAcbCreateMasterPINButton8().setOnClickListener {
            clickEffect(it)
            pushNumber(8, it)
        }

        getAcbCreateMasterPINButton9().setOnClickListener {
            clickEffect(it)
            pushNumber(9, it)
        }

        getAcbCreateMasterPINButton0().setOnClickListener {
            clickEffect(it)
            pushNumber(0, it)
        }

        getAcbCreateMasterPINButtonDelete().setOnClickListener {
            clickEffect(it)

            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbCreateMasterPINButtonCancel().setOnClickListener {
            clickEffect(it)

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

    private fun clickEffect(view: View) {
        view.apply {
            view.setBackgroundResource(R.drawable.layout_button_light_gray_quadrilateral)
            postDelayed(
                    {
                        view.setBackgroundResource(R.drawable.layout_button_white_quadrilateral)
                    }, 100
            )
        }
    }
}