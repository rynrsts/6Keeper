package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var scSettingsScreenCapture: SwitchCompat
    private lateinit var scSettingsAutoLock: SwitchCompat

    private lateinit var tvSettingsScreenCaptureDesc: TextView
    private lateinit var tvSettingsAutoLockDesc: TextView
    private lateinit var tvSettingsAutoLockTimer: TextView
    private lateinit var tvSettingsAutoLockTimerSeconds: TextView

    private lateinit var ivSettingsAutoLockTimer: ImageView

    private lateinit var clSettingsAutoLockTimer: ConstraintLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        showSwitchState()
        setSwitchOnOff()
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

        scSettingsScreenCapture = appCompatActivity.findViewById(R.id.scSettingsScreenCapture)
        scSettingsAutoLock = appCompatActivity.findViewById(R.id.scSettingsAutoLock)

        tvSettingsScreenCaptureDesc =
                appCompatActivity.findViewById(R.id.tvSettingsScreenCaptureDesc)
        tvSettingsAutoLockDesc = appCompatActivity.findViewById(R.id.tvSettingsAutoLockDesc)
        tvSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.tvSettingsAutoLockTimer)
        tvSettingsAutoLockTimerSeconds =
                appCompatActivity.findViewById(R.id.tvSettingsAutoLockTimerSeconds)

        ivSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.ivSettingsAutoLockTimer)

        clSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.clSettingsAutoLockTimer)
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    appCompatActivity.currentFocus?.windowToken,
                    0
            )
        }
    }

    private fun showSwitchState() {
        val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()

        for (u in userSettings) {
            if (encodingClass.decodeData(u.screenCapture) == "1") {                                 // Screen Capture
                scSettingsScreenCapture.apply {
                    tag = "screen capture"
                    isChecked = true
                }

                tvSettingsScreenCaptureDesc.setText(R.string.settings_allow_screen_capture)
            } else if (encodingClass.decodeData(u.screenCapture) == "0") {
                scSettingsScreenCapture.apply {
                    tag = "screen capture"
                    isChecked = false
                }

                tvSettingsScreenCaptureDesc.setText(R.string.settings_block_screen_capture)
            }

            if (encodingClass.decodeData(u.autoLock) == "1") {                                      // Auto Lock
                scSettingsAutoLock.apply {
                    tag = "auto lock"
                    isChecked = true
                }

                enableAutoLock()
            } else if (encodingClass.decodeData(u.autoLock) == "0") {
                scSettingsAutoLock.apply {
                    tag = "auto lock"
                    isChecked = false
                }

                disableAutoLock()
            }

            tvSettingsAutoLockTimerSeconds.text = encodingClass.decodeData(u.autoLockTimer)
        }
    }

    private fun enableAutoLock() {
        tvSettingsAutoLockDesc.setText(R.string.settings_enable_auto_lock)

        ivSettingsAutoLockTimer.apply {
            setBackgroundResource(R.drawable.layout_blue_magenta_circle)
            setImageResource(R.drawable.ic_timer_white)
        }
        tvSettingsAutoLockTimer.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
        )
        tvSettingsAutoLockTimerSeconds.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
        )
    }

    private fun disableAutoLock() {
        tvSettingsAutoLockDesc.setText(R.string.settings_disable_auto_lock)

        ivSettingsAutoLockTimer.apply {
            setBackgroundResource(0)
            setImageResource(R.drawable.ic_timer_gray)
        }
        tvSettingsAutoLockTimer.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.gray)
        )
        tvSettingsAutoLockTimerSeconds.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.gray)
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun setSwitchOnOff() {
        scSettingsScreenCapture.setOnClickListener {
            var actionLogId = 1000001
            val lastId = databaseHandlerClass.getLastIdOfActionLog()
            var message = ""

            val calendar: Calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
            val date: String = dateFormat.format(calendar.time)

            if (lastId.isNotEmpty()) {
                actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
            }

            if (scSettingsScreenCapture.isChecked) {
                databaseHandlerClass.updateSettings(
                        "screen_capture",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsScreenCaptureDesc.setText(R.string.settings_allow_screen_capture)
                message = "Screen Capture was allowed."
            } else {
                databaseHandlerClass.updateSettings(
                        "screen_capture",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsScreenCaptureDesc.setText(R.string.settings_block_screen_capture)
                message = "Screen Capture was blocked."
            }

            databaseHandlerClass.addEventToActionLog(                                               // Add event to Action Log
                    UserActionLogModelClass(
                            encodingClass.encodeData(actionLogId.toString()),
                            encodingClass.encodeData(message),
                            encodingClass.encodeData(date)
                    )
            )
        }

        scSettingsAutoLock.setOnClickListener {
            if (scSettingsAutoLock.isChecked) {
                databaseHandlerClass.updateSettings(
                        "auto_lock",
                        encodingClass.encodeData(1.toString())
                )

                enableAutoLock()
            } else {
                databaseHandlerClass.updateSettings(
                        "auto_lock",
                        encodingClass.encodeData(0.toString())
                )

                disableAutoLock()
            }
        }

        clSettingsAutoLockTimer.setOnClickListener {
            //
        }
    }
}