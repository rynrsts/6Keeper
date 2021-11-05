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
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandlerClass(
        private val context: Activity,
        private val label: String,
) : FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null

    private lateinit var fingerprintManagerTemp: FingerprintManager
    private var cryptoObjectTemp: FingerprintManager.CryptoObject? = null

    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

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

        fingerprintManagerTemp = manager
        cryptoObjectTemp = cryptoObject

        encodingClass = EncodingClass()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseHandlerClass = DatabaseHandlerClass(context)

        val userAccList = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encodingClass.decodeData(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {                    // Authentication error
        if (errString != "Fingerprint operation canceled.") {
            if (!locked()) {
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
    }

    override fun onAuthenticationFailed() {                                                         // Authentication failed
        if (!locked()) {
            val fWrongAttemptRef = databaseReference.child("fwrongAttempt")
            var fWrongAttempt = ""
            var count = 0
            val button = Button(context)
            var wrongAttempt = 0
            var timer = 30

            fWrongAttemptRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    fWrongAttempt = encodingClass.decodeData(value)
                    count++

                    if (count == 1) {
                        button.performClick()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            button.setOnClickListener {
                if (fWrongAttempt.isNotEmpty()) {
                    wrongAttempt = Integer.parseInt(fWrongAttempt)
                }

                wrongAttempt++

                val encodedWrongAttempt = encodingClass.encodeData(wrongAttempt.toString())

                databaseReference.child("fwrongAttempt").setValue(encodedWrongAttempt)

                if (wrongAttempt % 6 == 0) {
                    for (i in 1 until (wrongAttempt / 6)) {
                        timer *= 2
                    }

                    val encodedCurrentDate = encodingClass.encodeData(getCurrentDate())
                    val encodedMPinWrongAttempt =
                            encodingClass.encodeData((wrongAttempt / 2).toString())

                    databaseReference.child("mpinLockTime").setValue(encodedCurrentDate)
                    databaseReference.child("mpinWrongAttempt")
                            .setValue(encodedMPinWrongAttempt)

                    val toast: Toast = Toast.makeText(
                            context,
                            "Account is locked. Please wait for $timer seconds",
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                } else {
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

                vibrator()
            }
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {                   // Authentication help
        if (!locked()) {
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
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {       // Authentication succeeded
        if (!locked()) {
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
        } else {
            startAuth(fingerprintManagerTemp, cryptoObjectTemp)
        }
    }

    fun stopFingerAuth() {
        if (cancellationSignal != null && !cancellationSignal!!.isCanceled) {
            cancellationSignal!!.cancel()
        }
    }

    private fun locked(): Boolean {
        val mPinWrongAttemptRef = databaseReference.child("mpinWrongAttempt")
        val mPinLockTimeRef = databaseReference.child("mpinLockTime")

        var mPinWrongAttempt = ""
        var mPinLockTime = ""

        mPinWrongAttemptRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                mPinWrongAttempt = encodingClass.decodeData(value)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        mPinLockTimeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                mPinLockTime = encodingClass.decodeData(value)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val waitingTime = waitingTime(mPinWrongAttempt, mPinLockTime)
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
    fun waitingTime(mPinWrongAttempt: String, mPinLockTime: String): Long {
        var waitingTime: Long = 0

        if (mPinWrongAttempt.isNotEmpty()) {
            val wrongAttempts = Integer.parseInt(mPinWrongAttempt)

            if (wrongAttempts % 3 == 0) {
                if (mPinLockTime.isNotEmpty()) {
                    val dateToday: Date = dateFormat.parse(getCurrentDate())
                    val lockeDate: Date = dateFormat.parse(mPinLockTime)
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

        return waitingTime
    }

    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
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
}