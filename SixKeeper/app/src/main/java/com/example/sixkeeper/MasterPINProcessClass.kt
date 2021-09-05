package com.example.sixkeeper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import java.util.*

open class MasterPINProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var ivMasterPINCircle1: ImageView
    private lateinit var ivMasterPINCircle2: ImageView
    private lateinit var ivMasterPINCircle3: ImageView
    private lateinit var ivMasterPINCircle4: ImageView
    private lateinit var ivMasterPINCircle5: ImageView
    private lateinit var ivMasterPINCircle6: ImageView

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()
    private val masterPin = 123456

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        ivMasterPINCircle1 = findViewById(R.id.ivMasterPINCircle1)
        ivMasterPINCircle2 = findViewById(R.id.ivMasterPINCircle2)
        ivMasterPINCircle3 = findViewById(R.id.ivMasterPINCircle3)
        ivMasterPINCircle4 = findViewById(R.id.ivMasterPINCircle4)
        ivMasterPINCircle5 = findViewById(R.id.ivMasterPINCircle5)
        ivMasterPINCircle6 = findViewById(R.id.ivMasterPINCircle6)
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

            if (validateUserMasterPIN(pinI)) {
                setResult(16914, Intent().putExtra("masterPin", masterPin))
                onBackPressed()
                overridePendingTransition(
                    R.anim.anim_0,
                    R.anim.anim_exit_top_to_bottom_2
                )
            } else {
                val vibrator: Vibrator =
                        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(                                                               // Vibrate for wrong confirmation
                            VibrationEffect.createOneShot(
                                    350,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                            )
                    )
                } else {
                    vibrator.vibrate(350)
                }

                unShadeAllPin()
            }
        }
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

    private fun validateUserMasterPIN(pinI: Int): Boolean {
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        for (u in userAccList) {
            bool = pinI == u.masterPin
        }

        return bool
    }
}