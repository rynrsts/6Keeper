package com.example.sixkeeper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import android.widget.TextView
import java.util.*

open class CreateMasterPINProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var ivCreateMasterPINCircle1: ImageView
    private lateinit var ivCreateMasterPINCircle2: ImageView
    private lateinit var ivCreateMasterPINCircle3: ImageView
    private lateinit var ivCreateMasterPINCircle4: ImageView
    private lateinit var ivCreateMasterPINCircle5: ImageView
    private lateinit var ivCreateMasterPINCircle6: ImageView

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()
    private val savedPin: Stack<Int> = Stack()

    fun getPin(): Stack<Int> {
        return pin
    }

    fun setVariables() {
        ivCreateMasterPINCircle1 = findViewById(R.id.ivCreateMasterPINCircle1)
        ivCreateMasterPINCircle2 = findViewById(R.id.ivCreateMasterPINCircle2)
        ivCreateMasterPINCircle3 = findViewById(R.id.ivCreateMasterPINCircle3)
        ivCreateMasterPINCircle4 = findViewById(R.id.ivCreateMasterPINCircle4)
        ivCreateMasterPINCircle5 = findViewById(R.id.ivCreateMasterPINCircle5)
        ivCreateMasterPINCircle6 = findViewById(R.id.ivCreateMasterPINCircle6)
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
                ivCreateMasterPINCircle1.setImageResource(R.drawable.layout_blue_circle)
            2 ->
                ivCreateMasterPINCircle2.setImageResource(R.drawable.layout_blue_circle)
            3 ->
                ivCreateMasterPINCircle3.setImageResource(R.drawable.layout_blue_circle)
            4 ->
                ivCreateMasterPINCircle4.setImageResource(R.drawable.layout_blue_circle)
            5 ->
                ivCreateMasterPINCircle5.setImageResource(R.drawable.layout_blue_circle)
            6 ->
                ivCreateMasterPINCircle6.setImageResource(R.drawable.layout_blue_circle)
        }

        if (savedPin.size == pinSize && pin.size == pinSize) {
            if (savedPin == pin) {
                var tempS = ""

                for (i: Int in 1..pinSize)
                    temp.push(pin.pop())
                for (i: Int in 1..pinSize)
                    tempS = tempS + "" + temp.pop()
                val masterPin: Int = tempS.toInt()

                setResult(16914, Intent().putExtra("masterPin", masterPin))
                onBackPressed()
                overridePendingTransition(
                    R.anim.anim_0,
                    R.anim.anim_exit_top_to_bottom_2
                )

            } else {
                for (i: Int in 1..pinSize)
                    pin.pop()

                unShadeAllPin()
            }
        } else if (pin.size == pinSize) {
            var valid = true

            for (i: Int in 1..10) {
                var twos = 0

                for (n: Int in pin) {
                    if (i - 1 == n)
                        twos++

                    if (twos == 3) {
                        valid = false
                        break
                    }
                }
            }

            if (valid) {
                for (i: Int in 1..pinSize)
                    temp.push(pin.pop())
                for (i: Int in 1..pinSize)
                    savedPin.push(temp.pop())

                val tvCreateMasterPINHeading: TextView =
                        findViewById(R.id.tvCreateMasterPINHeading)
                tvCreateMasterPINHeading.setText(R.string.many_confirm_master_pin)
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

                for (i: Int in 1..pinSize)
                    pin.pop()
            }

            unShadeAllPin()
        }
    }

    fun unShadePin() {
        when (pin.size) {
            1 ->
                ivCreateMasterPINCircle1.setImageResource(R.drawable.layout_blue_border_circle)
            2 ->
                ivCreateMasterPINCircle2.setImageResource(R.drawable.layout_blue_border_circle)
            3 ->
                ivCreateMasterPINCircle3.setImageResource(R.drawable.layout_blue_border_circle)
            4 ->
                ivCreateMasterPINCircle4.setImageResource(R.drawable.layout_blue_border_circle)
            5 ->
                ivCreateMasterPINCircle5.setImageResource(R.drawable.layout_blue_border_circle)
            6 ->
                ivCreateMasterPINCircle6.setImageResource(R.drawable.layout_blue_border_circle)
        }
    }

    private fun unShadeAllPin() {
        ivCreateMasterPINCircle1.setImageResource(R.drawable.layout_blue_border_circle)
        ivCreateMasterPINCircle2.setImageResource(R.drawable.layout_blue_border_circle)
        ivCreateMasterPINCircle3.setImageResource(R.drawable.layout_blue_border_circle)
        ivCreateMasterPINCircle4.setImageResource(R.drawable.layout_blue_border_circle)
        ivCreateMasterPINCircle5.setImageResource(R.drawable.layout_blue_border_circle)
        ivCreateMasterPINCircle6.setImageResource(R.drawable.layout_blue_border_circle)
    }
}