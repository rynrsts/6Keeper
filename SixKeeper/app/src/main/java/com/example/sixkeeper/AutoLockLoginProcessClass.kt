package com.example.sixkeeper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule

open class AutoLockLoginProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var ivAutoLockLoginCircle1: ImageView
    private lateinit var ivAutoLockLoginCircle2: ImageView
    private lateinit var ivAutoLockLoginCircle3: ImageView
    private lateinit var ivAutoLockLoginCircle4: ImageView
    private lateinit var ivAutoLockLoginCircle5: ImageView
    private lateinit var ivAutoLockLoginCircle6: ImageView

    private lateinit var acbAutoLockLoginButton1: Button
    private lateinit var acbAutoLockLoginButton2: Button
    private lateinit var acbAutoLockLoginButton3: Button
    private lateinit var acbAutoLockLoginButton4: Button
    private lateinit var acbAutoLockLoginButton5: Button
    private lateinit var acbAutoLockLoginButton6: Button
    private lateinit var acbAutoLockLoginButton7: Button
    private lateinit var acbAutoLockLoginButton8: Button
    private lateinit var acbAutoLockLoginButton9: Button
    private lateinit var acbAutoLockLoginButton0: Button
    private lateinit var acbAutoLockLoginButtonDelete: Button
    private lateinit var acbAutoLockLoginButtonCancel: Button

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()

    private var userId: String = ""

    fun getAcbAutoLockLoginButton1(): Button {
        return acbAutoLockLoginButton1
    }

    fun getAcbAutoLockLoginButton2(): Button {
        return acbAutoLockLoginButton2
    }

    fun getAcbAutoLockLoginButton3(): Button {
        return acbAutoLockLoginButton3
    }

    fun getAcbAutoLockLoginButton4(): Button {
        return acbAutoLockLoginButton4
    }

    fun getAcbAutoLockLoginButton5(): Button {
        return acbAutoLockLoginButton5
    }

    fun getAcbAutoLockLoginButton6(): Button {
        return acbAutoLockLoginButton6
    }

    fun getAcbAutoLockLoginButton7(): Button {
        return acbAutoLockLoginButton7
    }

    fun getAcbAutoLockLoginButton8(): Button {
        return acbAutoLockLoginButton8
    }

    fun getAcbAutoLockLoginButton9(): Button {
        return acbAutoLockLoginButton9
    }

    fun getAcbAutoLockLoginButton0(): Button {
        return acbAutoLockLoginButton0
    }

    fun getAcbAutoLockLoginButtonDelete(): Button {
        return acbAutoLockLoginButtonDelete
    }

    fun getAcbAutoLockLoginButtonCancel(): Button {
        return acbAutoLockLoginButtonCancel
    }

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()
        userAccList = databaseHandlerClass.validateUserAcc()

        ivAutoLockLoginCircle1 = findViewById(R.id.ivAutoLockLoginCircle1)
        ivAutoLockLoginCircle2 = findViewById(R.id.ivAutoLockLoginCircle2)
        ivAutoLockLoginCircle3 = findViewById(R.id.ivAutoLockLoginCircle3)
        ivAutoLockLoginCircle4 = findViewById(R.id.ivAutoLockLoginCircle4)
        ivAutoLockLoginCircle5 = findViewById(R.id.ivAutoLockLoginCircle5)
        ivAutoLockLoginCircle6 = findViewById(R.id.ivAutoLockLoginCircle6)

        acbAutoLockLoginButton1 = findViewById(R.id.acbAutoLockLoginButton1)
        acbAutoLockLoginButton2 = findViewById(R.id.acbAutoLockLoginButton2)
        acbAutoLockLoginButton3 = findViewById(R.id.acbAutoLockLoginButton3)
        acbAutoLockLoginButton4 = findViewById(R.id.acbAutoLockLoginButton4)
        acbAutoLockLoginButton5 = findViewById(R.id.acbAutoLockLoginButton5)
        acbAutoLockLoginButton6 = findViewById(R.id.acbAutoLockLoginButton6)
        acbAutoLockLoginButton7 = findViewById(R.id.acbAutoLockLoginButton7)
        acbAutoLockLoginButton8 = findViewById(R.id.acbAutoLockLoginButton8)
        acbAutoLockLoginButton9 = findViewById(R.id.acbAutoLockLoginButton9)
        acbAutoLockLoginButton0 = findViewById(R.id.acbAutoLockLoginButton0)
        acbAutoLockLoginButtonDelete = findViewById(R.id.acbAutoLockLoginButtonDelete)
        acbAutoLockLoginButtonCancel = findViewById(R.id.acbAutoLockLoginButtonCancel)
    }

    fun blockCapture() {
        window.setFlags(                                                                            // Block capture
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun pushNumber(i: Int) {
        if (pin.size < 6) {
            pin.push(i)
            shadePin()
        }
    }

    private fun shadePin() {
        when (pin.size) {
            1 ->
                ivAutoLockLoginCircle1.setImageResource(R.drawable.layout_blue_circle)
            2 ->
                ivAutoLockLoginCircle2.setImageResource(R.drawable.layout_blue_circle)
            3 ->
                ivAutoLockLoginCircle3.setImageResource(R.drawable.layout_blue_circle)
            4 ->
                ivAutoLockLoginCircle4.setImageResource(R.drawable.layout_blue_circle)
            5 ->
                ivAutoLockLoginCircle5.setImageResource(R.drawable.layout_blue_circle)
            6 ->
                ivAutoLockLoginCircle6.setImageResource(R.drawable.layout_blue_circle)
        }

        if (pin.size == pinSize) {
            var tempS = ""
            for (i: Int in 1..pinSize)
                temp.push(pin.pop())
            for (i: Int in 1..pinSize)
                tempS = tempS + "" + temp.pop()
            val pinI: Int = tempS.toInt()

            if (validateUserMasterPin(pinI)) {
                setResult(1215311)

                this.finish()
                overridePendingTransition(
                    R.anim.anim_0,
                    R.anim.anim_exit_top_to_bottom_2
                )
            } else {
                val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.many_incorrect_master_pin,
                    Toast.LENGTH_SHORT
                )

                acbAutoLockLoginButton1.isClickable = false
                acbAutoLockLoginButton2.isClickable = false
                acbAutoLockLoginButton3.isClickable = false
                acbAutoLockLoginButton4.isClickable = false
                acbAutoLockLoginButton5.isClickable = false
                acbAutoLockLoginButton6.isClickable = false
                acbAutoLockLoginButton7.isClickable = false
                acbAutoLockLoginButton8.isClickable = false
                acbAutoLockLoginButton9.isClickable = false
                acbAutoLockLoginButton0.isClickable = false
                acbAutoLockLoginButtonDelete.isClickable = false
                acbAutoLockLoginButtonCancel.isClickable = false

                Timer().schedule(200) {
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }

                    val vibrator: Vibrator =
                        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    @Suppress("DEPRECATION")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                           // If android version is Oreo and above
                        vibrator.vibrate(                                                           // Vibrate for wrong confirmation
                            VibrationEffect.createOneShot(
                                350,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        vibrator.vibrate(350)
                    }

                    acbAutoLockLoginButton1.isClickable = true
                    acbAutoLockLoginButton2.isClickable = true
                    acbAutoLockLoginButton3.isClickable = true
                    acbAutoLockLoginButton4.isClickable = true
                    acbAutoLockLoginButton5.isClickable = true
                    acbAutoLockLoginButton6.isClickable = true
                    acbAutoLockLoginButton7.isClickable = true
                    acbAutoLockLoginButton8.isClickable = true
                    acbAutoLockLoginButton9.isClickable = true
                    acbAutoLockLoginButton0.isClickable = true
                    acbAutoLockLoginButtonDelete.isClickable = true
                    acbAutoLockLoginButtonCancel.isClickable = true

                    unShadeAllPin()
                }
            }
        }
    }

    fun unShadePin() {
        when (pin.size) {
            1 ->
                ivAutoLockLoginCircle1.setImageResource(R.drawable.layout_blue_border_circle)
            2 ->
                ivAutoLockLoginCircle2.setImageResource(R.drawable.layout_blue_border_circle)
            3 ->
                ivAutoLockLoginCircle3.setImageResource(R.drawable.layout_blue_border_circle)
            4 ->
                ivAutoLockLoginCircle4.setImageResource(R.drawable.layout_blue_border_circle)
            5 ->
                ivAutoLockLoginCircle5.setImageResource(R.drawable.layout_blue_border_circle)
            6 ->
                ivAutoLockLoginCircle6.setImageResource(R.drawable.layout_blue_border_circle)
        }
    }

    private fun unShadeAllPin() {
        ivAutoLockLoginCircle1.setImageResource(R.drawable.layout_blue_border_circle)
        ivAutoLockLoginCircle2.setImageResource(R.drawable.layout_blue_border_circle)
        ivAutoLockLoginCircle3.setImageResource(R.drawable.layout_blue_border_circle)
        ivAutoLockLoginCircle4.setImageResource(R.drawable.layout_blue_border_circle)
        ivAutoLockLoginCircle5.setImageResource(R.drawable.layout_blue_border_circle)
        ivAutoLockLoginCircle6.setImageResource(R.drawable.layout_blue_border_circle)
    }

    private fun validateUserMasterPin(pinI: Int): Boolean {                                         // Validate Master PIN
        val encryptionClass = EncryptionClass()
        var bool = false

        val encodedAutoLockLogin = encodingClass.encodeData(pinI.toString())
        val encryptedAutoLockLogin = encryptionClass.hashData(encodedAutoLockLogin)

        for (u in userAccList) {
            bool = encryptedAutoLockLogin.contentEquals(u.masterPin)
            userId = u.userId
        }

        return bool
    }

    override fun onBackPressed() {                                                                  // Override back button function
        setResult(31143512)

        val homeClick = Intent(Intent.ACTION_MAIN)
        homeClick.addCategory(Intent.CATEGORY_HOME)
        startActivity(homeClick)
    }
}