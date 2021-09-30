package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class CreateNewAccountP4Fragment : CreateNewAccountP4ValidationClass() {
    private lateinit var attActivity: Activity

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

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    @SuppressLint("InflateParams")
    private fun setButtonOnClick() {
        val acbCreateNewAccP4MasterPIN: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP4MasterPIN)
        val tvCreateNewAccP4Terms: TextView =
                getAppCompatActivity().findViewById(R.id.tvCreateNewAccP4Terms)
        val tvCreateNewAccP4Privacy: TextView =
                getAppCompatActivity().findViewById(R.id.tvCreateNewAccP4Privacy)
        val acbCreateNewAccP4Register: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP4Register)

        tvCreateNewAccP4Terms.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.fragment_terms_conditions, null)

            builder.apply {
                setView(dialogView)
                setCancelable(false)
            }
            builder.setNegativeButton("Ok") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                context, R.drawable.layout_alert_dialog
                        )
                )
                show()
            }
        }

        tvCreateNewAccP4Privacy.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.fragment_privacy_policy, null)

            builder.apply {
                setView(dialogView)
                setCancelable(false)
            }
            builder.setNegativeButton("Ok") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                context, R.drawable.layout_alert_dialog
                        )
                )
                show()
            }
        }

        acbCreateNewAccP4MasterPIN.setOnClickListener {
            val goToCreateMasterPINActivity = Intent(
                    activity,
                    CreateMasterPINActivity::class.java
            )

            @Suppress("DEPRECATION")
            startActivityForResult(goToCreateMasterPINActivity, 14523)

            getAppCompatActivity().overridePendingTransition(
                    R.anim.anim_enter_bottom_to_top_2,
                    R.anim.anim_0
            )

            it.apply {
                acbCreateNewAccP4MasterPIN.isClickable = false                                      // Set button un-clickable for 1 second
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
                    val builder: AlertDialog.Builder = AlertDialog.Builder(attActivity)
                    builder.setMessage(R.string.create_new_acc_message)
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        getCreateNewAccountActivity().saveAccount()

                        getAppCompatActivity().finish()
                        getAppCompatActivity().overridePendingTransition(
                                R.anim.anim_enter_left_to_right_2,
                                R.anim.anim_exit_left_to_right_2
                        )
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()
                    }

                    val alert: AlertDialog = builder.create()
                    alert.setTitle(R.string.many_alert_title_confirm)
                    alert.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {               // Get value from intent
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        val masterPin: Int = data?.getIntExtra("masterPin", 0)!!

        when {
            requestCode == 14523 && resultCode == 14523 -> {
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