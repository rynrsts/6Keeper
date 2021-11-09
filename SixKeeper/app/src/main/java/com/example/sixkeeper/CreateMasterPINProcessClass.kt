package com.example.sixkeeper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*

open class CreateMasterPINProcessClass : ChangeStatusBarToWhiteClass() {
    private lateinit var ivCreateMasterPINCircle1: ImageView
    private lateinit var ivCreateMasterPINCircle2: ImageView
    private lateinit var ivCreateMasterPINCircle3: ImageView
    private lateinit var ivCreateMasterPINCircle4: ImageView
    private lateinit var ivCreateMasterPINCircle5: ImageView
    private lateinit var ivCreateMasterPINCircle6: ImageView

    private lateinit var acbCreateMasterPINButton1: Button
    private lateinit var acbCreateMasterPINButton2: Button
    private lateinit var acbCreateMasterPINButton3: Button
    private lateinit var acbCreateMasterPINButton4: Button
    private lateinit var acbCreateMasterPINButton5: Button
    private lateinit var acbCreateMasterPINButton6: Button
    private lateinit var acbCreateMasterPINButton7: Button
    private lateinit var acbCreateMasterPINButton8: Button
    private lateinit var acbCreateMasterPINButton9: Button
    private lateinit var acbCreateMasterPINButton0: Button
    private lateinit var acbCreateMasterPINButtonDelete: Button
    private lateinit var acbCreateMasterPINButtonCancel: Button

    private val pinSize = 6
    private val pin: Stack<Int> = Stack()
    private val temp: Stack<Int> = Stack()
    private val savedPin: Stack<Int> = Stack()

    fun getAcbCreateMasterPINButton1(): Button {
        return acbCreateMasterPINButton1
    }

    fun getAcbCreateMasterPINButton2(): Button {
        return acbCreateMasterPINButton2
    }

    fun getAcbCreateMasterPINButton3(): Button {
        return acbCreateMasterPINButton3
    }

    fun getAcbCreateMasterPINButton4(): Button {
        return acbCreateMasterPINButton4
    }

    fun getAcbCreateMasterPINButton5(): Button {
        return acbCreateMasterPINButton5
    }

    fun getAcbCreateMasterPINButton6(): Button {
        return acbCreateMasterPINButton6
    }

    fun getAcbCreateMasterPINButton7(): Button {
        return acbCreateMasterPINButton7
    }

    fun getAcbCreateMasterPINButton8(): Button {
        return acbCreateMasterPINButton8
    }

    fun getAcbCreateMasterPINButton9(): Button {
        return acbCreateMasterPINButton9
    }

    fun getAcbCreateMasterPINButton0(): Button {
        return acbCreateMasterPINButton0
    }

    fun getAcbCreateMasterPINButtonDelete(): Button {
        return acbCreateMasterPINButtonDelete
    }

    fun getAcbCreateMasterPINButtonCancel(): Button {
        return acbCreateMasterPINButtonCancel
    }

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

        acbCreateMasterPINButton1 = findViewById(R.id.acbCreateMasterPINButton1)
        acbCreateMasterPINButton2 = findViewById(R.id.acbCreateMasterPINButton2)
        acbCreateMasterPINButton3 = findViewById(R.id.acbCreateMasterPINButton3)
        acbCreateMasterPINButton4 = findViewById(R.id.acbCreateMasterPINButton4)
        acbCreateMasterPINButton5 = findViewById(R.id.acbCreateMasterPINButton5)
        acbCreateMasterPINButton6 = findViewById(R.id.acbCreateMasterPINButton6)
        acbCreateMasterPINButton7 = findViewById(R.id.acbCreateMasterPINButton7)
        acbCreateMasterPINButton8 = findViewById(R.id.acbCreateMasterPINButton8)
        acbCreateMasterPINButton9 = findViewById(R.id.acbCreateMasterPINButton9)
        acbCreateMasterPINButton0 = findViewById(R.id.acbCreateMasterPINButton0)
        acbCreateMasterPINButtonDelete = findViewById(R.id.acbCreateMasterPINButtonDelete)
        acbCreateMasterPINButtonCancel = findViewById(R.id.acbCreateMasterPINButtonCancel)
    }

    fun blockCapture() {
        window.setFlags(                                                                            // Block capture
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun pushNumber(i: Int, view: View) {
        if (pin.size == 0 && i == 0) {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.create_master_pin_not_start_with_0,
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }

            return
        }

        if (pin.size < 6) {
            pin.push(i)
            shadePin(view)
        }
    }

    private fun shadePin(view: View) {
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

                setResult(14523, Intent().putExtra("masterPin", masterPin))                     // Return the created Master PIN
                finish()
                overridePendingTransition(
                    R.anim.anim_0,
                    R.anim.anim_exit_top_to_bottom_2
                )
            } else {
                acbCreateMasterPINButton1.isClickable = false
                acbCreateMasterPINButton2.isClickable = false
                acbCreateMasterPINButton3.isClickable = false
                acbCreateMasterPINButton4.isClickable = false
                acbCreateMasterPINButton5.isClickable = false
                acbCreateMasterPINButton6.isClickable = false
                acbCreateMasterPINButton7.isClickable = false
                acbCreateMasterPINButton8.isClickable = false
                acbCreateMasterPINButton9.isClickable = false
                acbCreateMasterPINButton0.isClickable = false
                acbCreateMasterPINButtonDelete.isClickable = false
                acbCreateMasterPINButtonCancel.isClickable = false

                view.apply {
                    postDelayed(
                        {
                            val vibrator: Vibrator =
                                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            @Suppress("DEPRECATION")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                   // If android version is Oreo and above
                                vibrator.vibrate(                                                   // Vibrate for wrong confirmation
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

                            acbCreateMasterPINButton1.isClickable = true
                            acbCreateMasterPINButton2.isClickable = true
                            acbCreateMasterPINButton3.isClickable = true
                            acbCreateMasterPINButton4.isClickable = true
                            acbCreateMasterPINButton5.isClickable = true
                            acbCreateMasterPINButton6.isClickable = true
                            acbCreateMasterPINButton7.isClickable = true
                            acbCreateMasterPINButton8.isClickable = true
                            acbCreateMasterPINButton9.isClickable = true
                            acbCreateMasterPINButton0.isClickable = true
                            acbCreateMasterPINButtonDelete.isClickable = true
                            acbCreateMasterPINButtonCancel.isClickable = true

                            unShadeAllPin()
                        }, 200
                    )
                }
            }
        } else if (pin.size == pinSize) {
            val tvCreateMasterPINHeading: TextView = findViewById(R.id.tvCreateMasterPINHeading)
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

            acbCreateMasterPINButton1.isClickable = false
            acbCreateMasterPINButton2.isClickable = false
            acbCreateMasterPINButton3.isClickable = false
            acbCreateMasterPINButton4.isClickable = false
            acbCreateMasterPINButton5.isClickable = false
            acbCreateMasterPINButton6.isClickable = false
            acbCreateMasterPINButton7.isClickable = false
            acbCreateMasterPINButton8.isClickable = false
            acbCreateMasterPINButton9.isClickable = false
            acbCreateMasterPINButton0.isClickable = false
            acbCreateMasterPINButtonDelete.isClickable = false
            acbCreateMasterPINButtonCancel.isClickable = false

            view.apply {
                postDelayed(
                    {
                        if (valid) {
                            for (i: Int in 1..pinSize)
                                temp.push(pin.pop())
                            for (i: Int in 1..pinSize)
                                savedPin.push(temp.pop())
                            tvCreateMasterPINHeading.setText(R.string.many_confirm_master_pin)
                        } else {
                            val vibrator: Vibrator =
                                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            @Suppress("DEPRECATION")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                   // If android version is Oreo and above
                                vibrator.vibrate(                                                   // Vibrate for wrong confirmation
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

                        acbCreateMasterPINButton1.isClickable = true
                        acbCreateMasterPINButton2.isClickable = true
                        acbCreateMasterPINButton3.isClickable = true
                        acbCreateMasterPINButton4.isClickable = true
                        acbCreateMasterPINButton5.isClickable = true
                        acbCreateMasterPINButton6.isClickable = true
                        acbCreateMasterPINButton7.isClickable = true
                        acbCreateMasterPINButton8.isClickable = true
                        acbCreateMasterPINButton9.isClickable = true
                        acbCreateMasterPINButton0.isClickable = true
                        acbCreateMasterPINButtonDelete.isClickable = true
                        acbCreateMasterPINButtonCancel.isClickable = true

                        unShadeAllPin()
                    }, 200
                )
            }
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

    override fun onBackPressed() {                                                                  // Override back button function
        super.finish()
        overridePendingTransition(
            R.anim.anim_0,
            R.anim.anim_exit_top_to_bottom_2
        )
    }
}