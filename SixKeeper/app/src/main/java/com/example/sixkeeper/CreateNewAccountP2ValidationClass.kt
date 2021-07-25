package com.example.sixkeeper

import android.util.Patterns
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class CreateNewAccountP2ValidationClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var etCreateNewAccP2Email: EditText
    private lateinit var etCreateNewAccP2EmailEnterCode: EditText
    private lateinit var etCreateNewAccP2MobileNumber: EditText
    private lateinit var etCreateNewAccP2MobileNumEnterCode: EditText

    private lateinit var email: String
    private lateinit var emailEnterCode: String
    private lateinit var mobileNumber: String
    private lateinit var mobileNumEnterCode: String

    private lateinit var validFields: ArrayList<Boolean>

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etCreateNewAccP2Email = appCompatActivity.findViewById(R.id.etCreateNewAccP2Email)
        etCreateNewAccP2EmailEnterCode =
                appCompatActivity.findViewById(R.id.etCreateNewAccP2EmailEnterCode)
        etCreateNewAccP2MobileNumber =
                appCompatActivity.findViewById(R.id.etCreateNewAccP2MobileNumber)
        etCreateNewAccP2MobileNumEnterCode =
                appCompatActivity.findViewById(R.id.etCreateNewAccP2MobileNumEnterCode)

        validFields = ArrayList(2)
        for (b: Int in 0..1)
            validFields.add(false)
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtCreateNewAccP2Email(): EditText {
        return etCreateNewAccP2Email
    }

    fun getEtCreateNewAccP2EmailEnterCode(): EditText {
        return etCreateNewAccP2EmailEnterCode
    }

    fun getEtCreateNewAccP2MobileNumber(): EditText {
        return etCreateNewAccP2MobileNumber
    }

    fun getEtCreateNewAccP2MobileNumEnterCode(): EditText {
        return etCreateNewAccP2MobileNumEnterCode
    }

    fun setEmail(s: String) {
        email = s
    }

    fun getEmail(): String {
        return email
    }

    fun getEmailEnterCode(): String {
        return emailEnterCode
    }

    fun setMobileNumber(s: String) {
        mobileNumber = s
    }

    fun getMobileNumber(): String {
        return mobileNumber
    }

    fun getMobileNumEnterCode(): String {
        return mobileNumEnterCode
    }

    fun validateEmail() {                                                                           // Validate email
        val llCreateNewAccP2Email: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP2Email)

        when {
            Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() -> {                                    // Check if email is valid
                llCreateNewAccP2Email.removeAllViews()
                validFields[0] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_validate_email)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                llCreateNewAccP2Email.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[0] = false
            }
        }
    }

    fun validateMobileNumber() {                                                                    // Validate mobile number
        val llCreateNewAccP2MobileNumber: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP2MobileNumber)

        when (mobileNumber.length) {
            11 -> {
                llCreateNewAccP2MobileNumber.removeAllViews()
                validFields[1] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_validate_mobile_num)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                llCreateNewAccP2MobileNumber.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[1] = false
            }
        }
    }

    fun emailIsNotEmpty(): Boolean {
        email = etCreateNewAccP2Email.text.toString()
        return email.isNotEmpty()
    }

    fun emailEnterCodeIsNotEmpty(): Boolean {
        emailEnterCode = etCreateNewAccP2EmailEnterCode.text.toString()
        return emailEnterCode.isNotEmpty()
    }

    fun mobileNumberIsNotEmpty(): Boolean {
        mobileNumber = etCreateNewAccP2MobileNumber.text.toString()
        return mobileNumber.isNotEmpty()
    }

    fun mobileNumEnterCodeIsNotEmpty(): Boolean {
        mobileNumEnterCode = etCreateNewAccP2MobileNumEnterCode.text.toString()
        return mobileNumEnterCode.isNotEmpty()
    }

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts to not empty
        return emailIsNotEmpty() && mobileNumberIsNotEmpty()
    }

    fun isValid(): Boolean {                                                                        // Validate EditTexts
        return validFields[0] && validFields[1]
    }
}