package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class ConfirmActionActivity : ConfirmActionProcessClass(), LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_action)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        IndexActivity().setBackgroundDate()
        changeStatusBarColor()
        setVariables()
        blockCapture()
        setButtonOnClick()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        this.finish()
    }

    private fun setButtonOnClick() {
        val tvConfirmActionForgotPass: TextView = findViewById(R.id.tvConfirmActionForgotPass)

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

        tvConfirmActionForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                    Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                tvConfirmActionForgotPass.isClickable = false                                       // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvConfirmActionForgotPass.isClickable = true
                        }, 1000
                )
            }
        }
    }
}