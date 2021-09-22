package com.example.sixkeeper

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule

open class ConfirmActionProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var userAccList: List<UserAccModelClass>

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

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()

    private var userId: String = ""

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

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()
        userAccList = databaseHandlerClass.validateUserAcc()

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

            if (validateUserMasterPin(pinI)) {
                setResult(16914)
                onBackPressed()
            } else {
                val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.many_incorrect_master_pin,
                    Toast.LENGTH_SHORT
                )

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
            }
        }
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

    private fun validateUserMasterPin(pinI: Int): Boolean {                                         // Validate Master PIN
        val encryptionClass = EncryptionClass()
        var bool = false

        val encodedConfirmAction = encodingClass.encodeData(pinI.toString())
        val encryptedConfirmAction = encryptionClass.encrypt(encodedConfirmAction)

        for (u in userAccList) {
            bool = encryptedConfirmAction.contentEquals(u.masterPin)
            userId = u.userId
        }

        return bool
    }

    override fun onBackPressed() {                                                                  // Override back button function
        super.onBackPressed()
        overridePendingTransition(
            R.anim.anim_0,
            R.anim.anim_exit_top_to_bottom_2
        )
    }
}