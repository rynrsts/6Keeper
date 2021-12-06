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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ResetMasterPINFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var tvResetMasterPINMes: TextView

    private var userId = ""
    private var masterPin = 0
    private var masterPinVal = ""

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
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userAccList = databaseHandlerClass.validateUserAcc()

        tvResetMasterPINMes = appCompatActivity.findViewById(R.id.tvResetMasterPINMes)

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)

        val masterPinRef = databaseReference.child("masterPin")

        masterPinRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                masterPinVal = dataSnapshot.getValue(String::class.java).toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
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
                acbResetMasterPIN.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbResetMasterPIN.isClickable = true
                        }, 1000
                )
            }
        }

        acbResetPasswordReset.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (masterPin != 0) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    builder.setMessage(R.string.reset_master_pin_alert)
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        if (InternetConnectionClass().isConnected()) {
                            val encryptedInput =
                                    encryptionClass.encryptOnly(masterPin.toString(), userId)

                            var actionLogId = 1000001
                            val lastId = databaseHandlerClass.getLastIdOfActionLog()

                            val calendar: Calendar = Calendar.getInstance()
                            val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                            val date = dateFormat.format(calendar.time)

                            if (lastId.isNotEmpty()) {
                                actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, userId)) + 1
                            }

                            databaseReference.child("masterPin").setValue(encryptedInput)
                            databaseReference.child("mpinWrongAttempt").setValue("")
                            databaseReference.child("fwrongAttempt").setValue("")
                            databaseReference.child("mpinLockTime").setValue("")

                            databaseHandlerClass.addEventToActionLog(                               // Add event to Action Log
                                    UserActionLogModelClass(
                                            encryptionClass.encrypt(actionLogId.toString(), userId),
                                            encryptionClass.encrypt(
                                                    "App account master pin was modified.",
                                                    userId
                                            ),
                                            encryptionClass.encrypt(date, userId)
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

                    acbResetPasswordReset.apply {
                        acbResetPasswordReset.isClickable = false                                   // Set button un-clickable for 1 second
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
            } else {
                internetToast()
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {               // Get value from intent
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 14523 && resultCode == 14523 -> {
                if (data != null) {
                    val input = data.getIntExtra("masterPin", 0)
                    val encryptedInput = encryptionClass.encryptOnly(input.toString(), userId)

                    if (!masterPinVal.contentEquals(encryptedInput)) {
                        masterPin = input

                        tvResetMasterPINMes.apply {
                            setText(R.string.many_setup_complete)
                            setTextColor(ContextCompat.getColor(context, R.color.blue))
                        }
                    } else {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.reset_master_pin_not_the_same,
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
    }
}