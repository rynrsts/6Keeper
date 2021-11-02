package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

open class MasterPINProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var ivMasterPINCircle1: ImageView
    private lateinit var ivMasterPINCircle2: ImageView
    private lateinit var ivMasterPINCircle3: ImageView
    private lateinit var ivMasterPINCircle4: ImageView
    private lateinit var ivMasterPINCircle5: ImageView
    private lateinit var ivMasterPINCircle6: ImageView

    private lateinit var acbMasterPINButton1: Button
    private lateinit var acbMasterPINButton2: Button
    private lateinit var acbMasterPINButton3: Button
    private lateinit var acbMasterPINButton4: Button
    private lateinit var acbMasterPINButton5: Button
    private lateinit var acbMasterPINButton6: Button
    private lateinit var acbMasterPINButton7: Button
    private lateinit var acbMasterPINButton8: Button
    private lateinit var acbMasterPINButton9: Button
    private lateinit var acbMasterPINButton0: Button
    private lateinit var acbMasterPINButtonDelete: Button
    private lateinit var acbMasterPINButtonCancel: Button

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()

    private var userId: String = ""

    fun getAcbMasterPINButton1(): Button {
        return acbMasterPINButton1
    }

    fun getAcbMasterPINButton2(): Button {
        return acbMasterPINButton2
    }

    fun getAcbMasterPINButton3(): Button {
        return acbMasterPINButton3
    }

    fun getAcbMasterPINButton4(): Button {
        return acbMasterPINButton4
    }

    fun getAcbMasterPINButton5(): Button {
        return acbMasterPINButton5
    }

    fun getAcbMasterPINButton6(): Button {
        return acbMasterPINButton6
    }

    fun getAcbMasterPINButton7(): Button {
        return acbMasterPINButton7
    }

    fun getAcbMasterPINButton8(): Button {
        return acbMasterPINButton8
    }

    fun getAcbMasterPINButton9(): Button {
        return acbMasterPINButton9
    }

    fun getAcbMasterPINButton0(): Button {
        return acbMasterPINButton0
    }

    fun getAcbMasterPINButtonDelete(): Button {
        return acbMasterPINButtonDelete
    }

    fun getAcbMasterPINButtonCancel(): Button {
        return acbMasterPINButtonCancel
    }

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        ivMasterPINCircle1 = findViewById(R.id.ivMasterPINCircle1)
        ivMasterPINCircle2 = findViewById(R.id.ivMasterPINCircle2)
        ivMasterPINCircle3 = findViewById(R.id.ivMasterPINCircle3)
        ivMasterPINCircle4 = findViewById(R.id.ivMasterPINCircle4)
        ivMasterPINCircle5 = findViewById(R.id.ivMasterPINCircle5)
        ivMasterPINCircle6 = findViewById(R.id.ivMasterPINCircle6)

        acbMasterPINButton1 = findViewById(R.id.acbMasterPINButton1)
        acbMasterPINButton2 = findViewById(R.id.acbMasterPINButton2)
        acbMasterPINButton3 = findViewById(R.id.acbMasterPINButton3)
        acbMasterPINButton4 = findViewById(R.id.acbMasterPINButton4)
        acbMasterPINButton5 = findViewById(R.id.acbMasterPINButton5)
        acbMasterPINButton6 = findViewById(R.id.acbMasterPINButton6)
        acbMasterPINButton7 = findViewById(R.id.acbMasterPINButton7)
        acbMasterPINButton8 = findViewById(R.id.acbMasterPINButton8)
        acbMasterPINButton9 = findViewById(R.id.acbMasterPINButton9)
        acbMasterPINButton0 = findViewById(R.id.acbMasterPINButton0)
        acbMasterPINButtonDelete = findViewById(R.id.acbMasterPINButtonDelete)
        acbMasterPINButtonCancel = findViewById(R.id.acbMasterPINButtonCancel)
    }

    fun setFingerprintOff() {
        databaseHandlerClass.updateSettings(
                "fingerprint",
                encodingClass.encodeData(0.toString())
        )
    }

    fun getFingerprintStatus(): Int {
        val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()
        var fingerprintStatus = 0

        for (u in userSettings) {
            fingerprintStatus = Integer.parseInt(encodingClass.decodeData(u.fingerprint))
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
                ivMasterPINCircle1.setImageResource(R.drawable.layout_blue_circle)
            2 ->
                ivMasterPINCircle2.setImageResource(R.drawable.layout_blue_circle)
            3 ->
                ivMasterPINCircle3.setImageResource(R.drawable.layout_blue_circle)
            4 ->
                ivMasterPINCircle4.setImageResource(R.drawable.layout_blue_circle)
            5 ->
                ivMasterPINCircle5.setImageResource(R.drawable.layout_blue_circle)
            6 ->
                ivMasterPINCircle6.setImageResource(R.drawable.layout_blue_circle)
        }

        if (pin.size == pinSize) {
            var tempS = ""
            for (i: Int in 1..pinSize)
                temp.push(pin.pop())
            for (i: Int in 1..pinSize)
                tempS = tempS + "" + temp.pop()
            val pinI: Int = tempS.toInt()

            if (InternetConnectionClass().isConnected()) {
                validateUserMasterPIN(pinI, view)
            } else {
                disableButtons()

                view.apply {
                    postDelayed(
                            {
                                internetToast()
                                enableButtons()
                            }, 200
                    )
                }
            }
        }
    }

    fun internetToast() {
        val toast: Toast = Toast.makeText(
                applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun disableButtons() {
        acbMasterPINButton1.isClickable = false
        acbMasterPINButton2.isClickable = false
        acbMasterPINButton3.isClickable = false
        acbMasterPINButton4.isClickable = false
        acbMasterPINButton5.isClickable = false
        acbMasterPINButton6.isClickable = false
        acbMasterPINButton7.isClickable = false
        acbMasterPINButton8.isClickable = false
        acbMasterPINButton9.isClickable = false
        acbMasterPINButton0.isClickable = false
        acbMasterPINButtonDelete.isClickable = false
        acbMasterPINButtonCancel.isClickable = false
    }

    private fun enableButtons() {
        acbMasterPINButton1.isClickable = true
        acbMasterPINButton2.isClickable = true
        acbMasterPINButton3.isClickable = true
        acbMasterPINButton4.isClickable = true
        acbMasterPINButton5.isClickable = true
        acbMasterPINButton6.isClickable = true
        acbMasterPINButton7.isClickable = true
        acbMasterPINButton8.isClickable = true
        acbMasterPINButton9.isClickable = true
        acbMasterPINButton0.isClickable = true
        acbMasterPINButtonDelete.isClickable = true
        acbMasterPINButtonCancel.isClickable = true

        unShadeAllPin()
    }

    fun locked(): Boolean {
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
                    applicationContext,
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

    fun unShadePin() {
        when (pin.size) {
            1 ->
                ivMasterPINCircle1.setImageResource(R.drawable.layout_blue_border_circle)
            2 ->
                ivMasterPINCircle2.setImageResource(R.drawable.layout_blue_border_circle)
            3 ->
                ivMasterPINCircle3.setImageResource(R.drawable.layout_blue_border_circle)
            4 ->
                ivMasterPINCircle4.setImageResource(R.drawable.layout_blue_border_circle)
            5 ->
                ivMasterPINCircle5.setImageResource(R.drawable.layout_blue_border_circle)
            6 ->
                ivMasterPINCircle6.setImageResource(R.drawable.layout_blue_border_circle)
        }
    }

    private fun unShadeAllPin() {
        ivMasterPINCircle1.setImageResource(R.drawable.layout_blue_border_circle)
        ivMasterPINCircle2.setImageResource(R.drawable.layout_blue_border_circle)
        ivMasterPINCircle3.setImageResource(R.drawable.layout_blue_border_circle)
        ivMasterPINCircle4.setImageResource(R.drawable.layout_blue_border_circle)
        ivMasterPINCircle5.setImageResource(R.drawable.layout_blue_border_circle)
        ivMasterPINCircle6.setImageResource(R.drawable.layout_blue_border_circle)
    }

    @SuppressLint("ShowToast")
    private fun validateUserMasterPIN(pinI: Int, view: View) {                                      // Validate Master PIN
        val encryptionClass = EncryptionClass()

        val encodedMasterPIN = encodingClass.encodeData(pinI.toString())
        val encryptedMasterPIN = encryptionClass.hashData(encodedMasterPIN)
        var uMasterPIN: ByteArray? = null
        val masterPINString = encodingClass.decodeSHA(encryptedMasterPIN)
        userAccList = databaseHandlerClass.validateUserAcc()

        val dataList = ArrayList<String>(0)
        val button = Button(this)

        for (u in userAccList) {
            userId = u.userId
            uMasterPIN = u.masterPin
        }

        val decodedUserId = encodingClass.decodeData(userId)
        databaseReference = firebaseDatabase.getReference(decodedUserId)

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataList.add(snapshot.getValue(String::class.java).toString())

                if (dataList.size == 9) {
                    button.performClick()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        button.setOnClickListener {
            if (encryptedMasterPIN.contentEquals(uMasterPIN) && masterPINString == dataList[4]) {
                databaseHandlerClass.updateAccountStatus(
                        "m_pin_wrong_attempt",
                        ""
                )

                databaseHandlerClass.updateAccountStatus(
                        "f_wrong_attempt",
                        ""
                )

                databaseHandlerClass.updateAccountStatus(
                        "m_pin_lock_time",
                        ""
                )

                val goToIndexActivity = Intent(this, IndexActivity::class.java)
                startActivity(goToIndexActivity)
                overridePendingTransition(
                        R.anim.anim_enter_top_to_bottom_2,
                        R.anim.anim_exit_top_to_bottom_2
                )

                this.finish()
            } else {
                val userStatusList: List<UserAccountStatusModelClass> =
                        databaseHandlerClass.viewAccountStatus()
                var wrongAttempt = 0
                var timer = 30

                for (u in userStatusList) {
                    val mPinWrongAttempt = encodingClass.decodeData(u.mPinWrongAttempt)

                    if (mPinWrongAttempt.isNotEmpty()) {
                        wrongAttempt = Integer.parseInt(mPinWrongAttempt)
                    }
                }
                wrongAttempt++

                databaseHandlerClass.updateAccountStatus(
                        "m_pin_wrong_attempt",
                        encodingClass.encodeData(wrongAttempt.toString())
                )

                var toast: Toast = Toast.makeText(
                        applicationContext,
                        R.string.many_incorrect_master_pin,
                        Toast.LENGTH_SHORT
                )

                if (wrongAttempt % 3 == 0) {
                    for (i in 1 until (wrongAttempt / 3)) {
                        timer *= 2
                    }

                    databaseHandlerClass.updateAccountStatus(
                            "m_pin_lock_time",
                            encodingClass.encodeData(getCurrentDate())
                    )

                    toast = Toast.makeText(
                            applicationContext,
                            "Account is locked. Please wait for $timer seconds",
                            Toast.LENGTH_SHORT
                    )
                }

                disableButtons()

                view.apply {
                    postDelayed(
                            {
                                toast.apply {
                                    setGravity(Gravity.CENTER, 0, 0)
                                    show()
                                }

                                val vibrator: Vibrator =
                                        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                                @Suppress("DEPRECATION")
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {           // If android version is Oreo and above
                                    vibrator.vibrate(                                           // Vibrate for wrong confirmation
                                            VibrationEffect.createOneShot(
                                                    350,
                                                    VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                    )
                                } else {
                                    vibrator.vibrate(350)
                                }

                                enableButtons()
                            }, 200
                    )
                }
            }
        }
    }

    fun updateUserStatus() {                                                                        // Update account status to 0
        userAccList = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = u.userId
        }

        databaseHandlerClass.updateUserStatus(
                userId,
                encodingClass.encodeData(0.toString())
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun updateLastLogin() {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        databaseHandlerClass.updateLastLogin(
                userId,
                encodingClass.encodeData(date)
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    fun goToLoginActivity() {                                                                       // Go to login (Username and Password)
        val goToLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        overridePendingTransition(
                R.anim.anim_enter_top_to_bottom_2,
                R.anim.anim_exit_top_to_bottom_2
        )
        this.finish()
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.many_exit_mes)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            finish()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }
}