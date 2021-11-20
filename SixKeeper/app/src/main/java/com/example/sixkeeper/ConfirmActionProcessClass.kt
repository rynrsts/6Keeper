package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

open class ConfirmActionProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>
    lateinit var fingerprintHandlerClass: FingerprintHandlerClass

    private lateinit var ivConfirmActionCircle1: ImageView
    private lateinit var ivConfirmActionCircle2: ImageView
    private lateinit var ivConfirmActionCircle3: ImageView
    private lateinit var ivConfirmActionCircle4: ImageView
    private lateinit var ivConfirmActionCircle5: ImageView
    private lateinit var ivConfirmActionCircle6: ImageView

    private lateinit var acbConfirmActionButton1: Button
    private lateinit var acbConfirmActionButton2: Button
    private lateinit var acbConfirmActionButton3: Button
    private lateinit var acbConfirmActionButton4: Button
    private lateinit var acbConfirmActionButton5: Button
    private lateinit var acbConfirmActionButton6: Button
    private lateinit var acbConfirmActionButton7: Button
    private lateinit var acbConfirmActionButton8: Button
    private lateinit var acbConfirmActionButton9: Button
    private lateinit var acbConfirmActionButton0: Button
    private lateinit var acbConfirmActionButtonDelete: Button
    private lateinit var acbConfirmActionButtonCancel: Button
    private lateinit var button: Button

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()

    private lateinit var key: ByteArray
    private var userId: String = ""

    fun getDatabaseReference(): DatabaseReference {
        return databaseReference
    }

    fun getEncryptionClass(): EncryptionClass {
        return encryptionClass
    }

    fun getAcbConfirmActionButton1(): Button {
        return acbConfirmActionButton1
    }

    fun getAcbConfirmActionButton2(): Button {
        return acbConfirmActionButton2
    }

    fun getAcbConfirmActionButton3(): Button {
        return acbConfirmActionButton3
    }

    fun getAcbConfirmActionButton4(): Button {
        return acbConfirmActionButton4
    }

    fun getAcbConfirmActionButton5(): Button {
        return acbConfirmActionButton5
    }

    fun getAcbConfirmActionButton6(): Button {
        return acbConfirmActionButton6
    }

    fun getAcbConfirmActionButton7(): Button {
        return acbConfirmActionButton7
    }

    fun getAcbConfirmActionButton8(): Button {
        return acbConfirmActionButton8
    }

    fun getAcbConfirmActionButton9(): Button {
        return acbConfirmActionButton9
    }

    fun getAcbConfirmActionButton0(): Button {
        return acbConfirmActionButton0
    }

    fun getAcbConfirmActionButtonDelete(): Button {
        return acbConfirmActionButtonDelete
    }

    fun getAcbConfirmActionButtonCancel(): Button {
        return acbConfirmActionButtonCancel
    }
    
    fun getKey(): ByteArray {
        return key
    }

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        ivConfirmActionCircle1 = findViewById(R.id.ivConfirmActionCircle1)
        ivConfirmActionCircle2 = findViewById(R.id.ivConfirmActionCircle2)
        ivConfirmActionCircle3 = findViewById(R.id.ivConfirmActionCircle3)
        ivConfirmActionCircle4 = findViewById(R.id.ivConfirmActionCircle4)
        ivConfirmActionCircle5 = findViewById(R.id.ivConfirmActionCircle5)
        ivConfirmActionCircle6 = findViewById(R.id.ivConfirmActionCircle6)

        acbConfirmActionButton1 = findViewById(R.id.acbConfirmActionButton1)
        acbConfirmActionButton2 = findViewById(R.id.acbConfirmActionButton2)
        acbConfirmActionButton3 = findViewById(R.id.acbConfirmActionButton3)
        acbConfirmActionButton4 = findViewById(R.id.acbConfirmActionButton4)
        acbConfirmActionButton5 = findViewById(R.id.acbConfirmActionButton5)
        acbConfirmActionButton6 = findViewById(R.id.acbConfirmActionButton6)
        acbConfirmActionButton7 = findViewById(R.id.acbConfirmActionButton7)
        acbConfirmActionButton8 = findViewById(R.id.acbConfirmActionButton8)
        acbConfirmActionButton9 = findViewById(R.id.acbConfirmActionButton9)
        acbConfirmActionButton0 = findViewById(R.id.acbConfirmActionButton0)
        acbConfirmActionButtonDelete = findViewById(R.id.acbConfirmActionButtonDelete)
        acbConfirmActionButtonCancel = findViewById(R.id.acbConfirmActionButtonCancel)
        button = Button(this)

        userAccList = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
        databaseReference = firebaseDatabase.getReference(userId)
    }

    fun getFingerprintStatus(): Int {
        val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()
        var fingerprintStatus = 0

        for (u in userSettings) {
            fingerprintStatus = Integer.parseInt(encryptionClass.decrypt(u.fingerprint, key))
        }

        return fingerprintStatus
    }

    fun blockCapture() {
        window.setFlags(                                                                            // Block capture
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun pushNumber(i: Int, view: View) {
        if (pin.size < 6) {
            pin.push(i)
            shadePin(view)
        }
    }

    private fun shadePin(view: View) {
        when (pin.size) {
            1 ->
                ivConfirmActionCircle1.setImageResource(R.drawable.layout_blue_circle)
            2 ->
                ivConfirmActionCircle2.setImageResource(R.drawable.layout_blue_circle)
            3 ->
                ivConfirmActionCircle3.setImageResource(R.drawable.layout_blue_circle)
            4 ->
                ivConfirmActionCircle4.setImageResource(R.drawable.layout_blue_circle)
            5 ->
                ivConfirmActionCircle5.setImageResource(R.drawable.layout_blue_circle)
            6 ->
                ivConfirmActionCircle6.setImageResource(R.drawable.layout_blue_circle)
        }

        if (pin.size == pinSize) {
            var tempS = ""
            for (i: Int in 1..pinSize)
                temp.push(pin.pop())
            for (i: Int in 1..pinSize)
                tempS = tempS + "" + temp.pop()
            val pinI: Int = tempS.toInt()

            if (InternetConnectionClass().isConnected()) {
                val mPinWrongAttemptRef = databaseReference.child("mpinWrongAttempt")
                val mPinLockTimeRef = databaseReference.child("mpinLockTime")
                val masterPinRef = databaseReference.child("masterPin")

                var mPinWrongAttempt = ""
                var mPinLockTime = ""
                var masterPin = ""
                var count = 0

                mPinWrongAttemptRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        mPinWrongAttempt = encryptionClass.decrypt(value, key)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                mPinLockTimeRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        mPinLockTime = encryptionClass.decrypt(value, key)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                masterPinRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        masterPin = dataSnapshot.getValue(String::class.java).toString()
                        count++

                        if (count == 1) {
                            button.performClick()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                button.setOnClickListener {
                    if (!locked("", mPinWrongAttempt, mPinLockTime, view)) {
                        validateUserMasterPin(pinI, masterPin, mPinWrongAttempt)
                    }
                }
            } else {
                disableButtons()
                internetToast()

                Timer().schedule(200) {
                    enableButtons()
                }
            }
        }
    }

    fun locked(
            button: String, mPinWrongAttempt: String, mPinLockTime: String, view: View
    ): Boolean {
        val waitingTime = waitingTime(mPinWrongAttempt, mPinLockTime)
        var locked = false

        if (waitingTime > 0.toLong()) {
            if (button.isEmpty()) {
                var sec = ""

                if (waitingTime == 1.toLong()) {
                    sec = "second"
                } else if (waitingTime > 1.toLong()) {
                    sec = "seconds"
                }
                val toast: Toast = Toast.makeText(
                        applicationContext,
                        "Account is locked. Please wait for $waitingTime $sec",
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }

                disableButtons()

                view.apply {
                    postDelayed({ enableButtons() }, 200)
                }
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
        val elapsedRealtime = SystemClock.elapsedRealtime()
        return dateFormat.format(Date(elapsedRealtime)).toString()
    }

    @SuppressLint("ShowToast")
    private fun validateUserMasterPin(pinI: Int, masterPin: String, mPinWrongAttempt: String) {     // Validate Master PIN
        val encryptionClass = EncryptionClass()

        val encryptedConfirmAction = encryptionClass.hash(pinI.toString())

        if (encryptedConfirmAction == masterPin) {
            databaseReference.child("mpinWrongAttempt").setValue("")
            databaseReference.child("fwrongAttempt").setValue("")
            databaseReference.child("mpinLockTime").setValue("")

            fingerprintHandlerClass.stopFingerAuth()

            setResult(16914)
            onBackPressed()
        } else {
            var wrongAttempt = 0
            var timer = 30

            if (mPinWrongAttempt.isNotEmpty()) {
                wrongAttempt = Integer.parseInt(mPinWrongAttempt)
            }
            wrongAttempt++

            val encryptedWrongAttempt = encryptionClass.encrypt(wrongAttempt.toString(), key)
            databaseReference.child("mpinWrongAttempt").setValue(encryptedWrongAttempt)

            var toast: Toast = Toast.makeText(
                    applicationContext, R.string.many_incorrect_master_pin, Toast.LENGTH_SHORT
            )

            if (wrongAttempt % 3 == 0) {
                for (i in 1 until (wrongAttempt / 3)) {
                    timer *= 2
                }

                val encryptedCurrentDate = encryptionClass.encrypt(getCurrentDate(), key)
                val encryptedFWrongAttempt =
                        encryptionClass.encrypt((wrongAttempt * 2).toString(), key)

                databaseReference.child("mpinLockTime").setValue(encryptedCurrentDate)
                databaseReference.child("fwrongAttempt")
                        .setValue(encryptedFWrongAttempt)

                toast = Toast.makeText(
                        applicationContext,
                        "Account is locked. Please wait for $timer seconds",
                        Toast.LENGTH_SHORT
                )
            }

            disableButtons()

            Timer().schedule(200) {
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }

                val vibrator: Vibrator =
                        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                               // If android version is Oreo and above
                        vibrator.vibrate(                                                           // Vibrate for wrong confirmation
                            VibrationEffect.createOneShot(
                                    350,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                            )
                    )
                } else {
                    vibrator.vibrate(350)
                }

                enableButtons()
            }
        }
    }

    fun internetToast() {
        val toast: Toast = Toast.makeText(
                applicationContext, R.string.many_internet_connection, Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun disableButtons() {
        acbConfirmActionButton1.isClickable = false
        acbConfirmActionButton2.isClickable = false
        acbConfirmActionButton3.isClickable = false
        acbConfirmActionButton4.isClickable = false
        acbConfirmActionButton5.isClickable = false
        acbConfirmActionButton6.isClickable = false
        acbConfirmActionButton7.isClickable = false
        acbConfirmActionButton8.isClickable = false
        acbConfirmActionButton9.isClickable = false
        acbConfirmActionButton0.isClickable = false
        acbConfirmActionButtonDelete.isClickable = false
        acbConfirmActionButtonCancel.isClickable = false
    }

    private fun enableButtons() {
        acbConfirmActionButton1.isClickable = true
        acbConfirmActionButton2.isClickable = true
        acbConfirmActionButton3.isClickable = true
        acbConfirmActionButton4.isClickable = true
        acbConfirmActionButton5.isClickable = true
        acbConfirmActionButton6.isClickable = true
        acbConfirmActionButton7.isClickable = true
        acbConfirmActionButton8.isClickable = true
        acbConfirmActionButton9.isClickable = true
        acbConfirmActionButton0.isClickable = true
        acbConfirmActionButtonDelete.isClickable = true
        acbConfirmActionButtonCancel.isClickable = true

        unShadeAllPin()
    }

    fun unShadePin() {
        when (pin.size) {
            1 ->
                ivConfirmActionCircle1.setImageResource(R.drawable.layout_blue_border_circle)
            2 ->
                ivConfirmActionCircle2.setImageResource(R.drawable.layout_blue_border_circle)
            3 ->
                ivConfirmActionCircle3.setImageResource(R.drawable.layout_blue_border_circle)
            4 ->
                ivConfirmActionCircle4.setImageResource(R.drawable.layout_blue_border_circle)
            5 ->
                ivConfirmActionCircle5.setImageResource(R.drawable.layout_blue_border_circle)
            6 ->
                ivConfirmActionCircle6.setImageResource(R.drawable.layout_blue_border_circle)
        }
    }

    private fun unShadeAllPin() {
        ivConfirmActionCircle1.setImageResource(R.drawable.layout_blue_border_circle)
        ivConfirmActionCircle2.setImageResource(R.drawable.layout_blue_border_circle)
        ivConfirmActionCircle3.setImageResource(R.drawable.layout_blue_border_circle)
        ivConfirmActionCircle4.setImageResource(R.drawable.layout_blue_border_circle)
        ivConfirmActionCircle5.setImageResource(R.drawable.layout_blue_border_circle)
        ivConfirmActionCircle6.setImageResource(R.drawable.layout_blue_border_circle)
    }

    override fun onBackPressed() {                                                                  // Override back button function
        finish()
        overridePendingTransition(R.anim.anim_0, R.anim.anim_exit_top_to_bottom_2)
    }
}