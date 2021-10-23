package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class ResetMasterPINFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var tvResetMasterPINMes: TextView

    private var masterPin = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_master_p_i_n, container, false)
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
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        tvResetMasterPINMes = appCompatActivity.findViewById(R.id.tvResetMasterPINMes)
    }

    private fun setMasterPINText() {
        tvResetMasterPINMes.apply {
            if (masterPin != 0) {
                setText(R.string.many_setup_complete)
                setTextColor(ContextCompat.getColor(context, R.color.blue))
            } else {
                setText(R.string.many_required_mes)
                setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setButtonOnClick() {
        val acbResetMasterPIN: Button = appCompatActivity.findViewById(R.id.acbResetMasterPIN)
        val acbResetPasswordReset: Button =
                appCompatActivity.findViewById(R.id.acbResetPasswordReset)

        acbResetMasterPIN.setOnClickListener {
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

            it.apply {
                acbResetMasterPIN.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbResetMasterPIN.isClickable = true
                        }, 1000
                )
            }
        }

        acbResetPasswordReset.setOnClickListener {
            if (masterPin != 0) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                builder.setMessage(R.string.reset_master_pin_alert)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    val encryptionClass = EncryptionClass()
                    val encodedInput = encodingClass.encodeData(masterPin.toString())

                    var actionLogId = 1000001
                    val lastId = databaseHandlerClass.getLastIdOfActionLog()

                    val calendar: Calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                    val date = dateFormat.format(calendar.time)

                    if (lastId.isNotEmpty()) {
                        actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
                    }

                    databaseHandlerClass.updateUserAcc(                                             // Reset Master PIN
                            "master_pin", encryptionClass.hashData(encodedInput), date
                    )

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encodingClass.encodeData(actionLogId.toString()),
                                    encodingClass.encodeData(
                                            "App account master pin was changed."
                                    ),
                                    encodingClass.encodeData(date)
                            )
                    )

                    it.apply {
                        postDelayed(
                                {
                                    appCompatActivity.finish()
                                    appCompatActivity.overridePendingTransition(
                                            R.anim.anim_enter_left_to_right_2,
                                            R.anim.anim_exit_left_to_right_2
                                    )
                                }, 250
                        )
                    }

                    val toast: Toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.reset_master_pin_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title_confirm)
                alert.show()

                acbResetPasswordReset.apply {
                    acbResetPasswordReset.isClickable = false                                       // Set button un-clickable for 1 second
                    postDelayed(
                            {
                                acbResetPasswordReset.isClickable = true
                            }, 1000
                    )
                }
            } else {
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {               // Get value from intent
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        masterPin = data?.getIntExtra("masterPin", 0)!!

        when {
            requestCode == 14523 && resultCode == 14523 -> {
                tvResetMasterPINMes.apply {
                    setText(R.string.many_setup_complete)
                    setTextColor(ContextCompat.getColor(context, R.color.blue))
                }
            }
        }
    }
}