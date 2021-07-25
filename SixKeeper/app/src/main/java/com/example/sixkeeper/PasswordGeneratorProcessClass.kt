package com.example.sixkeeper

import android.text.InputType
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class PasswordGeneratorProcessClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etPassGeneratorLength: EditText
    private lateinit var cbPassGeneratorLowercase: CheckBox
    private lateinit var cbPassGeneratorUppercase: CheckBox
    private lateinit var cbPassGeneratorSpecialChar: CheckBox
    private lateinit var cbPassGeneratorNumber: CheckBox
    private lateinit var tvPassGeneratorGeneratedPass: TextView

    private lateinit var llSpecialCharacter: LinearLayout

    private lateinit var passwordGeneratorLength: String

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtPassGeneratorLength(): EditText {
        return etPassGeneratorLength
    }

    fun getCbPassGeneratorLowercase(): CheckBox {
        return cbPassGeneratorLowercase
    }

    fun getCbPassGeneratorUppercase(): CheckBox {
        return cbPassGeneratorUppercase
    }

    fun getCbPassGeneratorSpecialChar(): CheckBox {
        return cbPassGeneratorSpecialChar
    }

    fun getCbPassGeneratorNumber(): CheckBox {
        return cbPassGeneratorNumber
    }

    fun getTvPassGeneratorGeneratedPass(): TextView {
        return tvPassGeneratorGeneratedPass
    }

    fun getLlSpecialCharacter(): LinearLayout {
        return llSpecialCharacter
    }

    fun setPasswordGeneratorLength(s: String) {
        passwordGeneratorLength = s
    }

    fun getPassGeneratorLength(): String {
        return passwordGeneratorLength
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etPassGeneratorLength = appCompatActivity.findViewById(R.id.etPassGeneratorLength)
        cbPassGeneratorLowercase = appCompatActivity.findViewById(R.id.cbPassGeneratorLowercase)
        cbPassGeneratorUppercase = appCompatActivity.findViewById(R.id.cbPassGeneratorUppercase)
        cbPassGeneratorSpecialChar = appCompatActivity.findViewById(R.id.cbPassGeneratorSpecialChar)
        cbPassGeneratorNumber = appCompatActivity.findViewById(R.id.cbPassGeneratorNumber)
        tvPassGeneratorGeneratedPass =
                appCompatActivity.findViewById(R.id.tvPassGeneratorGeneratedPass)

        llSpecialCharacter = LinearLayout(context)
    }

    fun createSpecialCharViews() {
        val tvSpecialCharacter = TextView(context)
        val etSpecialCharacter = EditText(context)

        tvSpecialCharacter.apply {
            layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setText(R.string.pass_generator_enter_special_char)
            setTextColor(ContextCompat.getColor(context, R.color.lightBlack))
            textSize = 13F
        }

        etSpecialCharacter.apply {
            layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    84
            )
            background = ContextCompat.getDrawable(context, R.drawable.layout_edit_text)
            inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }

        llSpecialCharacter.apply {
            layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            background = ContextCompat.getDrawable(
                    context,
                    R.drawable.layout_constraint_white_quadrilateral
            )
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            addView(tvSpecialCharacter)
            addView(etSpecialCharacter)

            val topToBottom: Animation = AnimationUtils.loadAnimation(
                    context,
                    R.anim.anim_enter_top_to_bottom_2
            )
            startAnimation(topToBottom)
        }
    }

    fun generatePassword(length: Int): String {
        val stringBuilder = StringBuilder(length)
        var characters = ""

        if (cbPassGeneratorLowercase.isChecked) {
            characters += "abcdefghijklmnopqrstuvwxyz"
        }
        if (cbPassGeneratorUppercase.isChecked) {
            characters += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        }
        if (cbPassGeneratorSpecialChar.isChecked) {
            characters += "!@#$%^&*()=+-_"
        }
        if (cbPassGeneratorNumber.isChecked) {
            characters += "0123456789"
        }

        for (x in 0..length){
            val random = (characters.indices).random()
            stringBuilder.append(characters[random])
        }

        return stringBuilder.toString()
    }
}