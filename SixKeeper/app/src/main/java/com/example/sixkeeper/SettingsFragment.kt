@file:Suppress("DEPRECATION")

package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var scSettingsScreenCapture: SwitchCompat
    private lateinit var scSettingsAutoLock: SwitchCompat
    private lateinit var scSettingsFingerprint: SwitchCompat

    private lateinit var tvSettingsScreenCaptureDesc: TextView
    private lateinit var tvSettingsAutoLockDesc: TextView
    private lateinit var tvSettingsAutoLockTimer: TextView
    private lateinit var tvSettingsAutoLockTimerSeconds: TextView
    private lateinit var tvSettingsFingerprint: TextView
    private lateinit var tvSettingsFingerprintDesc: TextView

    private lateinit var ivSettingsAutoLockTimer: ImageView
    private lateinit var ivSettingsFingerprint: ImageView

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
        disableMenuItem()
        closeKeyboard()
        showSwitchState()
        setSwitchOnOff()
    }

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
        scSettingsFingerprint = appCompatActivity.findViewById(R.id.scSettingsFingerprint)

        tvSettingsScreenCaptureDesc =
                appCompatActivity.findViewById(R.id.tvSettingsScreenCaptureDesc)
        tvSettingsAutoLockDesc = appCompatActivity.findViewById(R.id.tvSettingsAutoLockDesc)
        tvSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.tvSettingsAutoLockTimer)
        tvSettingsAutoLockTimerSeconds =
                appCompatActivity.findViewById(R.id.tvSettingsAutoLockTimerSeconds)
        tvSettingsFingerprint = appCompatActivity.findViewById(R.id.tvSettingsFingerprint)
        tvSettingsFingerprintDesc = appCompatActivity.findViewById(R.id.tvSettingsFingerprintDesc)

        ivSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.ivSettingsAutoLockTimer)
        ivSettingsFingerprint = appCompatActivity.findViewById(R.id.ivSettingsFingerprint)

        clSettingsAutoLockTimer = appCompatActivity.findViewById(R.id.clSettingsAutoLockTimer)
    }

    private fun disableMenuItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = true
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = false
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
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

            tvSettingsAutoLockTimerSeconds.text = encodingClass.decodeData(u.autoLockTimer)         // Auto lock timer

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {                                   // Fingerprint
                try {
                    val keyguardManager =
                            appCompatActivity.getSystemService(AppCompatActivity.KEYGUARD_SERVICE)
                                    as KeyguardManager
                    val fingerprintManager =
                            appCompatActivity.getSystemService(
                                    AppCompatActivity.FINGERPRINT_SERVICE
                            ) as FingerprintManager

                    if (!fingerprintManager.isHardwareDetected) {                                   // If there is no fingerprint hardware
                        disableFingerprint()
                        return
                    }

                    if (                                                                            // If there is no fingerprint permission
                            ActivityCompat.checkSelfPermission(
                                    appCompatActivity,
                                    Manifest.permission.USE_FINGERPRINT
                            ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        disableFingerprint()
                        return
                    }

                    if (!fingerprintManager.hasEnrolledFingerprints()) {                            // If there is no enrolled fingerprint
                        disableFingerprint()
                        return
                    }

                    if (!keyguardManager.isKeyguardSecure) {                                        // If lock screen is not enabled
                        disableFingerprint()
                    } else {
                        ivSettingsFingerprint.apply {
                            setBackgroundResource(R.drawable.layout_orange_circle)
                            setImageResource(R.drawable.ic_fingerprint_white)
                        }
                        tvSettingsFingerprint.setTextColor(
                                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
                        )
                        tvSettingsFingerprintDesc.setTextColor(
                                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
                        )
                        scSettingsFingerprint.isEnabled = true

                        if (encodingClass.decodeData(u.fingerprint) == "1") {
                            scSettingsFingerprint.apply {
                                tag = "fingerprint"
                                isChecked = true
                            }

                            tvSettingsFingerprintDesc.setText(R.string.settings_enable_fingerprint)
                        } else if (encodingClass.decodeData(u.fingerprint) == "0") {
                            scSettingsFingerprint.apply {
                                tag = "fingerprint"
                                isChecked = false
                            }

                            tvSettingsFingerprintDesc.setText(R.string.settings_disable_fingerprint)
                        }
                    }
                } catch (e: NullPointerException) {
                    disableFingerprint()
                }
            } else {
                disableFingerprint()
            }
        }
    }

    private fun enableAutoLock() {
        ivSettingsAutoLockTimer.apply {
            setBackgroundResource(R.drawable.layout_blue_magenta_circle)
            setImageResource(R.drawable.ic_timer_white)
        }
        tvSettingsAutoLockTimer.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
        )
        tvSettingsAutoLockDesc.setText(R.string.settings_enable_auto_lock)
        tvSettingsAutoLockTimerSeconds.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.lightBlack)
        )
    }

    private fun disableAutoLock() {
        ivSettingsAutoLockTimer.apply {
            setBackgroundResource(0)
            setImageResource(R.drawable.ic_timer_gray)
        }
        tvSettingsAutoLockTimer.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.gray)
        )
        tvSettingsAutoLockDesc.setText(R.string.settings_disable_auto_lock)
        tvSettingsAutoLockTimerSeconds.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.gray)
        )
    }

    private fun disableFingerprint() {
        ivSettingsFingerprint.apply {
            setBackgroundResource(0)
            setImageResource(R.drawable.ic_fingerprint_gray)
        }
        tvSettingsFingerprint.setTextColor(
                ContextCompat.getColor(appCompatActivity, R.color.gray)
        )
        tvSettingsFingerprintDesc.apply {
            setText(R.string.settings_disable_fingerprint)
            setTextColor(ContextCompat.getColor(appCompatActivity, R.color.gray))
        }
        scSettingsFingerprint.isChecked = false

        databaseHandlerClass.updateSettings(
                "fingerprint",
                encodingClass.encodeData(0.toString())
        )
    }

    @SuppressLint("InflateParams")
    private fun setSwitchOnOff() {
        scSettingsScreenCapture.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val message: String

                if (scSettingsScreenCapture.isChecked) {
                    databaseHandlerClass.updateSettings(                                            // Update Screen Capture to 1
                            "screen_capture",
                            encodingClass.encodeData(1.toString())
                    )

                    tvSettingsScreenCaptureDesc.setText(R.string.settings_allow_screen_capture)
                    message = "Screen Capture was allowed."
                } else {
                    databaseHandlerClass.updateSettings(                                            // Update Screen Capture to 0
                            "screen_capture",
                            encodingClass.encodeData(0.toString())
                    )

                    tvSettingsScreenCaptureDesc.setText(R.string.settings_block_screen_capture)
                    message = "Screen Capture was blocked."
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData(message),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )
            } else {
                internetToast()
            }
        }

        scSettingsAutoLock.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val message: String

                if (scSettingsAutoLock.isChecked) {
                    databaseHandlerClass.updateSettings(                                            // Update Auto Lock to 1
                            "auto_lock",
                            encodingClass.encodeData(1.toString())
                    )

                    enableAutoLock()
                    message = "Auto Lock was enabled."
                } else {
                    databaseHandlerClass.updateSettings(                                            // Update Auto Lock to 0
                            "auto_lock",
                            encodingClass.encodeData(0.toString())
                    )

                    disableAutoLock()
                    message = "Auto Lock was disabled."
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData(message),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )
            } else {
                internetToast()
            }
        }

        clSettingsAutoLockTimer.setOnClickListener {
            if (scSettingsAutoLock.isChecked) {
                if (InternetConnectionClass().isConnected()) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    val inflater = this.layoutInflater
                    val dialogView = inflater.inflate(
                            R.layout.layout_settings_auto_lock_timer,
                            null
                    )

                    builder.apply {
                        setView(dialogView)
                        setCancelable(false)
                    }

                    val tvAutoLockTimerLength: TextView =
                            dialogView.findViewById(R.id.tvAutoLockTimerLength)
                    val acbAutoLockTimerMinus: Button =
                            dialogView.findViewById(R.id.acbAutoLockTimerMinus)
                    val acbAutoLockTimerPlus: Button =
                            dialogView.findViewById(R.id.acbAutoLockTimerPlus)

                    tvAutoLockTimerLength.text = tvSettingsAutoLockTimerSeconds.text.toString()
                            .replace(" sec", "")

                    acbAutoLockTimerMinus.setOnClickListener {
                        val length = Integer.parseInt(tvAutoLockTimerLength.text.toString())

                        if (length > 5) {
                            tvAutoLockTimerLength.text = (length - 1).toString()
                        }
                    }

                    acbAutoLockTimerPlus.setOnClickListener {
                        val length = Integer.parseInt(tvAutoLockTimerLength.text.toString())

                        if (length < 30) {
                            tvAutoLockTimerLength.text = (length + 1).toString()
                        }
                    }

                    builder.setPositiveButton("Save") { _: DialogInterface, _: Int ->
                        val length = Integer.parseInt(tvAutoLockTimerLength.text.toString())
                        val timer = "$length sec"

                        databaseHandlerClass.updateSettings(                                        // Update Auto Lock Timer
                                "auto_lock_timer",
                                encodingClass.encodeData(timer)
                        )
                        databaseHandlerClass.addEventToActionLog(                                   // Add event to Action Log
                                UserActionLogModelClass(
                                        encodingClass.encodeData(getLastActionLogId().toString()),
                                        encodingClass.encodeData(
                                                "Auto Lock Timer was modified to $timer."
                                        ),
                                        encodingClass.encodeData(getCurrentDate())
                                )
                        )
                        tvSettingsAutoLockTimerSeconds.text = timer

                        view?.apply {
                            postDelayed(
                                    {
                                        closeKeyboard()
                                    }, 50
                            )
                        }
                    }

                    builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()

                        view?.apply {
                            postDelayed(
                                    {
                                        closeKeyboard()
                                    }, 50
                            )
                        }
                    }

                    val alert: AlertDialog = builder.create()
                    alert.apply {
                        window?.setBackgroundDrawable(
                                ContextCompat.getDrawable(
                                        appCompatActivity, R.drawable.layout_alert_dialog
                                )
                        )
                        setTitle(R.string.settings_auto_lock_timer)
                        show()
                    }
                } else {
                    internetToast()
                }
            }
        }

        scSettingsFingerprint.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    val keyguardManager =
                            appCompatActivity.getSystemService(AppCompatActivity.KEYGUARD_SERVICE)
                                    as KeyguardManager
                    val fingerprintManager =
                            appCompatActivity.getSystemService(
                                    AppCompatActivity.FINGERPRINT_SERVICE
                            ) as FingerprintManager

                    if (!fingerprintManager.isHardwareDetected) {                                   // If there is no fingerprint hardware
                        disableFingerprintWithToast()
                        return@setOnClickListener
                    }

                    if (                                                                            // If there is no fingerprint permission
                            ActivityCompat.checkSelfPermission(
                                    appCompatActivity,
                                    Manifest.permission.USE_FINGERPRINT
                            ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        disableFingerprint()

                        val toast: Toast = Toast.makeText(
                                appCompatActivity,
                                R.string.fingerprint_enable_permission,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }

                        return@setOnClickListener
                    }

                    if (!fingerprintManager.hasEnrolledFingerprints()) {                            // If there is no enrolled fingerprint
                        disableFingerprint()

                        val toast: Toast = Toast.makeText(
                                appCompatActivity,
                                R.string.fingerprint_not_configured,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }

                        return@setOnClickListener
                    }

                    if (!keyguardManager.isKeyguardSecure) {                                        // If lock screen is not enabled
                        disableFingerprint()

                        val toast: Toast = Toast.makeText(
                                appCompatActivity,
                                R.string.fingerprint_enable_lock_screen,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    } else {
                        if (InternetConnectionClass().isConnected()) {
                            val message: String

                            if (scSettingsFingerprint.isChecked) {
                                databaseHandlerClass.updateSettings(                                // Update Fingerprint to 1
                                        "fingerprint",
                                        encodingClass.encodeData(1.toString())
                                )

                                tvSettingsFingerprintDesc.setText(R.string.settings_enable_fingerprint)
                                message = "Fingerprint authentication was enabled."
                            } else {
                                databaseHandlerClass.updateSettings(                                // Update Fingerprint to 0
                                        "fingerprint",
                                        encodingClass.encodeData(0.toString())
                                )

                                tvSettingsFingerprintDesc.setText(R.string.settings_disable_fingerprint)
                                message = "Fingerprint authentication was disabled."
                            }

                            databaseHandlerClass.addEventToActionLog(                               // Add event to Action Log
                                    UserActionLogModelClass(
                                            encodingClass.encodeData(getLastActionLogId().toString()),
                                            encodingClass.encodeData(message),
                                            encodingClass.encodeData(getCurrentDate())
                                    )
                            )
                        } else {
                            internetToast()
                        }
                    }
                } catch (e: NullPointerException) {
                    disableFingerprintWithToast()
                }
            } else {
                disableFingerprintWithToast()
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

    private fun disableFingerprintWithToast() {
        disableFingerprint()

        val toast: Toast = Toast.makeText(
                appCompatActivity,
                R.string.fingerprint_not_supported,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

        return dateFormat.format(calendar.time)
    }
}