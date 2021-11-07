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
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CreateNewAccountP4Fragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var createNewAccountActivity: CreateNewAccountActivity

    private lateinit var tvCreateNewAccP4MasterPinMes: TextView
    private lateinit var cbCreateNewAccP4AgreeToTerms: CheckBox

    private var masterPin = 0

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
        setMasterPINText()
        setButtonOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        createNewAccountActivity = activity as CreateNewAccountActivity

        tvCreateNewAccP4MasterPinMes =
                appCompatActivity.findViewById(R.id.tvCreateNewAccP4MasterPINMes)
        cbCreateNewAccP4AgreeToTerms =
                appCompatActivity.findViewById(R.id.cbCreateNewAccP4AgreeToTerms)
    }

    private fun setMasterPINText() {
        if (masterPin != 0) {
            tvCreateNewAccP4MasterPinMes.apply {
                setText(R.string.many_setup_complete)
                setTextColor(ContextCompat.getColor(context, R.color.blue))
            }

            createNewAccountActivity.setCreateNewAccountP4Data(masterPin)
        } else {
            tvCreateNewAccP4MasterPinMes.apply {
                setText(R.string.many_required_mes)
                setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setButtonOnClick() {
        val acbCreateNewAccP4MasterPIN: Button =
                appCompatActivity.findViewById(R.id.acbCreateNewAccP4MasterPIN)
        val tvCreateNewAccP4Terms: TextView =
                appCompatActivity.findViewById(R.id.tvCreateNewAccP4Terms)
        val tvCreateNewAccP4Privacy: TextView =
                appCompatActivity.findViewById(R.id.tvCreateNewAccP4Privacy)
        val acbCreateNewAccP4Register: Button =
                appCompatActivity.findViewById(R.id.acbCreateNewAccP4Register)

        acbCreateNewAccP4MasterPIN.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val goToCreateMasterPINActivity = Intent(
                        activity,
                        CreateMasterPINActivity::class.java
                )

                @Suppress("DEPRECATION")
                startActivityForResult(goToCreateMasterPINActivity, 14523)

                appCompatActivity.overridePendingTransition(
                        R.anim.anim_enter_bottom_to_top_2,
                        R.anim.anim_0
                )
            } else {
                internetToast()
            }

            it.apply {
                acbCreateNewAccP4MasterPIN.isClickable = false                                      // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbCreateNewAccP4MasterPIN.isClickable = true
                        }, 1000
                )
            }
        }

        tvCreateNewAccP4Terms.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.fragment_terms_conditions, null)
            showTermsOrPrivacy(dialogView)

            it.apply {
                tvCreateNewAccP4Terms.isClickable = false                                           // Set un-clickable for 1 second
                postDelayed(
                        {
                            tvCreateNewAccP4Terms.isClickable = true
                        }, 1000
                )
            }
        }

        tvCreateNewAccP4Privacy.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.fragment_privacy_policy, null)
            showTermsOrPrivacy(dialogView)

            it.apply {
                tvCreateNewAccP4Privacy.isClickable = false                                         // Set un-clickable for 1 second
                postDelayed(
                        {
                            tvCreateNewAccP4Privacy.isClickable = true
                        }, 1000
                )
            }
        }

        acbCreateNewAccP4Register.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                when {
                    masterPin != 0 && cbCreateNewAccP4AgreeToTerms.isChecked -> {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                        builder.setMessage(R.string.create_new_acc_message)
                        builder.setCancelable(false)

                        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                            if (InternetConnectionClass().isConnected()) {
                                createNewAccountActivity.saveAccount()
                            } else {
                                internetToast()
                            }
                        }
                        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                            dialog.cancel()
                        }

                        val alert: AlertDialog = builder.create()
                        alert.setTitle(R.string.many_alert_title_confirm)
                        alert.show()

                        it.apply {
                            acbCreateNewAccP4Register.isClickable = false                           // Set button un-clickable for 1 second
                            postDelayed(
                                    {
                                        acbCreateNewAccP4Register.isClickable = true
                                    }, 1000
                            )
                        }
                    }
                    masterPin == 0 -> {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.create_new_acc_p4_master_pin_mes,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                    masterPin != 0 && !cbCreateNewAccP4AgreeToTerms.isChecked -> {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.create_new_acc_p4_agree_to_terms_mes,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                }
            } else {
                internetToast()
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun showTermsOrPrivacy(dialogView: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {               // Get value from intent
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            masterPin = data.getIntExtra("masterPin", 0)
        }

        when {
            requestCode == 14523 && resultCode == 14523 -> {
                tvCreateNewAccP4MasterPinMes.apply {
                    setText(R.string.many_setup_complete)
                    setTextColor(ContextCompat.getColor(context, R.color.blue))
                }

                createNewAccountActivity.setCreateNewAccountP4Data(masterPin)
            }
        }
    }
}