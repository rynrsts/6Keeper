package com.example.sixkeeper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var scSettingsNotifications: SwitchCompat
    private lateinit var scSettingsScreenCapture: SwitchCompat
    private lateinit var scSettingsTextualCopy: SwitchCompat

    private lateinit var tvSettingsNotifDesc: TextView
    private lateinit var tvSettingsScreenCaptureDesc: TextView
    private lateinit var tvSettingsTextualCopyDesc: TextView

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

        scSettingsNotifications = appCompatActivity.findViewById(R.id.scSettingsNotifications)
        scSettingsScreenCapture = appCompatActivity.findViewById(R.id.scSettingsScreenCapture)
        scSettingsTextualCopy = appCompatActivity.findViewById(R.id.scSettingsTextualCopy)

        tvSettingsNotifDesc = appCompatActivity.findViewById(R.id.tvSettingsNotifDesc)
        tvSettingsScreenCaptureDesc =
                appCompatActivity.findViewById(R.id.tvSettingsScreenCaptureDesc)
        tvSettingsTextualCopyDesc = appCompatActivity.findViewById(R.id.tvSettingsTextualCopyDesc)
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
            if (encodingClass.decodeData(u.notifications) == "1") {
                scSettingsNotifications.apply {
                    tag = "notifications"
                    isChecked = true
                }

                tvSettingsNotifDesc.setText(R.string.settings_enable_notifications)
            } else if (encodingClass.decodeData(u.notifications) == "0") {
                scSettingsNotifications.apply {
                    tag = "notifications"
                    isChecked = false
                }

                tvSettingsNotifDesc.setText(R.string.settings_disable_notifications)
            }

            if (encodingClass.decodeData(u.screenCapture) == "1") {
                scSettingsScreenCapture.apply {
                    tag = "notifications"
                    isChecked = true
                }

                tvSettingsScreenCaptureDesc.setText(R.string.settings_allow_screen_capture)
            } else if (encodingClass.decodeData(u.screenCapture) == "0") {
                scSettingsScreenCapture.apply {
                    tag = "notifications"
                    isChecked = false
                }

                tvSettingsScreenCaptureDesc.setText(R.string.settings_block_screen_capture)
            }

            if (encodingClass.decodeData(u.copy) == "1") {
                scSettingsTextualCopy.apply {
                    tag = "notifications"
                    isChecked = true
                }

                tvSettingsTextualCopyDesc.setText(R.string.settings_allow_copy)
            } else if (encodingClass.decodeData(u.copy) == "0") {
                scSettingsTextualCopy.apply {
                    tag = "notifications"
                    isChecked = false
                }

                tvSettingsTextualCopyDesc.setText(R.string.settings_block_copy)
            }
        }
    }

    private fun setSwitchOnOff() {
        scSettingsNotifications.setOnClickListener {
            if (scSettingsNotifications.isChecked) {
                databaseHandlerClass.updateSettings(
                        "notifications",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsNotifDesc.setText(R.string.settings_enable_notifications)
            } else {
                databaseHandlerClass.updateSettings(
                        "notifications",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsNotifDesc.setText(R.string.settings_disable_notifications)
            }
        }

        scSettingsScreenCapture.setOnClickListener {
            if (scSettingsScreenCapture.isChecked) {
                databaseHandlerClass.updateSettings(
                        "screen_capture",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsScreenCaptureDesc.setText(R.string.settings_allow_screen_capture)
            } else {
                databaseHandlerClass.updateSettings(
                        "screen_capture",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsScreenCaptureDesc.setText(R.string.settings_block_screen_capture)
            }
        }

        scSettingsTextualCopy.setOnClickListener {
            if (scSettingsTextualCopy.isChecked) {
                databaseHandlerClass.updateSettings(
                        "copy",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsTextualCopyDesc.setText(R.string.settings_allow_copy)
            } else {
                databaseHandlerClass.updateSettings(
                        "copy",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsTextualCopyDesc.setText(R.string.settings_block_copy)
            }
        }
    }
}