@file:Suppress("DEPRECATION")

package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandlerClass(
        private val context: Activity,
        private val label: String,
) : FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject?) {    // Authentication start
        cancellationSignal = CancellationSignal()

        if (
                ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.USE_FINGERPRINT
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {                    // Authentication error
        if (errString != "Fingerprint operation canceled.") {
            vibrator()

            val toast: Toast = Toast.makeText(
                    context,
                    "Authentication error: $errString",
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    override fun onAuthenticationFailed() {                                                         // Authentication failed
        vibrator()

        val toast: Toast = Toast.makeText(
                context,
                R.string.fingerprint_authentication_failed,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {                   // Authentication help
        vibrator()

        val toast: Toast = Toast.makeText(
                context,
                "Authentication help: $helpString",
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {       // Authentication succeeded
        when (label) {
            "login" -> {
                val goToIndexActivity = Intent(context, IndexActivity::class.java)
                context.startActivity(goToIndexActivity)
                context.overridePendingTransition(
                        R.anim.anim_enter_top_to_bottom_2,
                        R.anim.anim_exit_top_to_bottom_2
                )

                context.finish()
            }
            "confirm action" -> {
                context.setResult(16914)
                context.onBackPressed()
            }
            "auto lock" -> {
                context.setResult(1215311)
                context.finish()
                context.overridePendingTransition(
                        R.anim.anim_0,
                        R.anim.anim_exit_top_to_bottom_2
                )
            }
        }
    }

    private fun vibrator() {
        val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                                       // If android version is Oreo and above
            vibrator.vibrate(                                                                       // Vibrate for wrong confirmation
                    VibrationEffect.createOneShot(
                            350,
                            VibrationEffect.DEFAULT_AMPLITUDE
                    )
            )
        } else {
            vibrator.vibrate(350)
        }
    }

    private fun locked(): Boolean {
        val waitingTime = waitingTime()
        var locked = false

        if (waitingTime > 0.toLong()) {
            var sec = ""

            if (waitingTime == 1.toLong()) {
                sec = "second"
            } else if (waitingTime > 1.toLong()) {
                sec = "seconds"
            }

            val toast: Toast = Toast.makeText(
                    context,
                    "Account is locked. Please wait for $waitingTime $sec",
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }

            locked = true
        }

        return locked
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun waitingTime(): Long {
        val databaseHandlerClass = DatabaseHandlerClass(context)
        val encodingClass = EncodingClass()
        val userStatusList: List<UserAccountStatusModelClass> =
                databaseHandlerClass.viewAccountStatus()
        var waitingTime: Long = 0

        for (u in userStatusList) {
            val mPinWrongAttempt = encodingClass.decodeData(u.mPinWrongAttempt)

            if (mPinWrongAttempt.isNotEmpty()) {
                val wrongAttempts = Integer.parseInt(mPinWrongAttempt)

                if (wrongAttempts % 3 == 0) {
                    val mPinLockDate = encodingClass.decodeData(u.mPinLockTime)

                    if (mPinLockDate.isNotEmpty()) {
                        val dateToday: Date = dateFormat.parse(getCurrentDate())
                        val lockeDate: Date = dateFormat.parse(mPinLockDate)
                        val timeDifference: Long = dateToday.time - lockeDate.time
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference)
                        val loop = wrongAttempts / 3
                        var timer = 30

                        for (i in 1 until loop) {
                            timer *= 2
                        }

                        if (seconds < timer) {
                            waitingTime = timer - seconds
                        }
                    }
                }
            }
        }

        return waitingTime
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }
}