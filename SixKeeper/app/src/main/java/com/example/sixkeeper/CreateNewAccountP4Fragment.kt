package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat

class CreateNewAccountP4Fragment : CreateNewAccountP4ValidationClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_create_new_account_p4,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP4MasterPIN: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP4MasterPIN)
        val acbCreateNewAccP4Register: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP4Register)

        acbCreateNewAccP4MasterPIN.setOnClickListener {
            val goToCreateMasterPINActivity = Intent(
                    activity,
                    CreateMasterPINActivity::class.java
            )

            @Suppress("DEPRECATION")
            startActivityForResult(goToCreateMasterPINActivity, 16914)

            getAppCompatActivity().overridePendingTransition(
                    R.anim.anim_enter_bottom_to_top_2,
                    R.anim.anim_0
            )

            it.apply {
                acbCreateNewAccP4MasterPIN.isClickable = false
                postDelayed(
                        {
                            acbCreateNewAccP4MasterPIN.isClickable = true
                        }, 1000
                )
            }
        }

        acbCreateNewAccP4Register.setOnClickListener {
            when {
                isMasterPINSetup() && isTermsChecked() -> {
                    getCreateNewAccountActivity().saveToDatabase()

                    getAppCompatActivity().finish()
                    getAppCompatActivity().overridePendingTransition(
                            R.anim.anim_enter_left_to_right_2,
                            R.anim.anim_exit_left_to_right_2
                    )
                }
                isMasterPINSetup() && !isTermsChecked() -> {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.create_new_acc_p4_agree_to_terms_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        val masterPin: Int = data?.getIntExtra("masterPin", 0)!!

        when {
            requestCode == 16914 && resultCode == 16914 -> {
                getTvCreateNewAccP4MasterPinMes().apply {
                    setText(R.string.many_setup_complete)
                    setTextColor(ContextCompat.getColor(context, R.color.blue))
                }

                getCreateNewAccountActivity().apply {
                    setCreateNewAccountP4Data(masterPin)
                }
            }
        }
    }
}