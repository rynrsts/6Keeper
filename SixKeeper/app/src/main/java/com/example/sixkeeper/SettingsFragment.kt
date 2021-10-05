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
    private lateinit var scSettingsScreenshot: SwitchCompat
    private lateinit var scSettingsScreenRecord: SwitchCompat
    private lateinit var scSettingsTextualCopy: SwitchCompat

    private lateinit var tvSettingsNotifDesc: TextView
    private lateinit var tvSettingsScreenshotDesc: TextView
    private lateinit var tvSettingsScreenRecordDesc: TextView
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
        scSettingsScreenshot = appCompatActivity.findViewById(R.id.scSettingsScreenshot)
        scSettingsScreenRecord = appCompatActivity.findViewById(R.id.scSettingsScreenRecord)
        scSettingsTextualCopy = appCompatActivity.findViewById(R.id.scSettingsTextualCopy)

        tvSettingsNotifDesc = appCompatActivity.findViewById(R.id.tvSettingsNotifDesc)
        tvSettingsScreenshotDesc = appCompatActivity.findViewById(R.id.tvSettingsScreenshotDesc)
        tvSettingsScreenRecordDesc = appCompatActivity.findViewById(R.id.tvSettingsScreenRecordDesc)
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

            if (encodingClass.decodeData(u.screenshot) == "1") {
                scSettingsScreenshot.apply {
                    tag = "notifications"
                    isChecked = true
                }

                tvSettingsScreenshotDesc.setText(R.string.settings_allow_screenshot)
            } else if (encodingClass.decodeData(u.screenshot) == "0") {
                scSettingsScreenshot.apply {
                    tag = "notifications"
                    isChecked = false
                }

                tvSettingsScreenshotDesc.setText(R.string.settings_block_screenshot)
            }

            if (encodingClass.decodeData(u.screenRecord) == "1") {
                scSettingsScreenRecord.apply {
                    tag = "notifications"
                    isChecked = true
                }

                tvSettingsScreenRecordDesc.setText(R.string.settings_allow_screen_record)
            } else if (encodingClass.decodeData(u.screenRecord) == "0") {
                scSettingsScreenRecord.apply {
                    tag = "notifications"
                    isChecked = false
                }

                tvSettingsScreenRecordDesc.setText(R.string.settings_block_screen_record)
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

        scSettingsScreenshot.setOnClickListener {
            if (scSettingsScreenshot.isChecked) {
                databaseHandlerClass.updateSettings(
                        "screenshot",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsScreenshotDesc.setText(R.string.settings_allow_screenshot)
            } else {
                databaseHandlerClass.updateSettings(
                        "screenshot",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsScreenshotDesc.setText(R.string.settings_block_screenshot)
            }
        }

        scSettingsScreenRecord.setOnClickListener {
            if (scSettingsScreenRecord.isChecked) {
                databaseHandlerClass.updateSettings(
                        "screen_record",
                        encodingClass.encodeData(1.toString())
                )

                tvSettingsScreenRecordDesc.setText(R.string.settings_allow_screen_record)
            } else {
                databaseHandlerClass.updateSettings(
                        "screen_record",
                        encodingClass.encodeData(0.toString())
                )

                tvSettingsScreenRecordDesc.setText(R.string.settings_block_screen_record)
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