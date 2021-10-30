@file:Suppress("DEPRECATION")

package com.example.sixkeeper

import android.Manifest
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
import android.widget.Toast
import androidx.core.app.ActivityCompat

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandler(
        private val context: Activity,
        private val label: String,
) : FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null

    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject?) {
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

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        if (errString != "Fingerprint operation canceled.") {
            Toast.makeText(
                context,
                "Authentication error: $errString",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()

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

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        Toast.makeText(context, "Authentication help: $helpString", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
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
}