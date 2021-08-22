package com.example.sixkeeper

import android.util.Patterns
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.regex.Pattern

open class CreateNewAccountP1ValidationClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etCreateNewAccP1FirstName: EditText
    private lateinit var etCreateNewAccP1LastName: EditText
    private lateinit var etCreateNewAccP1BirthDate: EditText
    private lateinit var etCreateNewAccP1Email: EditText
    private lateinit var etCreateNewAccP1MobileNumber: EditText

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private lateinit var email: String
    private lateinit var mobileNumber: String

    private lateinit var validFields: ArrayList<Boolean>

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etCreateNewAccP1FirstName = appCompatActivity.findViewById(R.id.etCreateNewAccP1FirstName)
        etCreateNewAccP1LastName = appCompatActivity.findViewById(R.id.etCreateNewAccP1LastName)
        etCreateNewAccP1BirthDate = appCompatActivity.findViewById(R.id.etCreateNewAccP1BirthDate)
        etCreateNewAccP1Email = appCompatActivity.findViewById(R.id.etCreateNewAccP1Email)
        etCreateNewAccP1MobileNumber =
                appCompatActivity.findViewById(R.id.etCreateNewAccP1MobileNumber)

        validFields = ArrayList(5)
        for (b: Int in 0..4)
            validFields.add(false)
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtCreateNewAccP1FirstName(): EditText {
        return etCreateNewAccP1FirstName
    }

    fun getEtCreateNewAccP1LastName(): EditText {
        return etCreateNewAccP1LastName
    }

    fun getEtCreateNewAccP1BirthDate(): EditText {
        return etCreateNewAccP1BirthDate
    }

    fun getEtCreateNewAccP1Email(): EditText {
        return etCreateNewAccP1Email
    }

    fun getEtCreateNewAccP1MobileNumber(): EditText {
        return etCreateNewAccP1MobileNumber
    }

    fun setFirstName(s: String) {
        firstName = s
    }

    fun getFirstName(): String {
        return firstName
    }

    fun setLastName(s: String) {
        lastName = s
    }

    fun getLastName(): String {
        return lastName
    }

    fun setBirthDate(s: String) {
        birthDate = s
    }

    fun getBirthDate(): String {
        return birthDate
    }

    fun setEmail(s: String) {
        email = s
    }

    fun getEmail(): String {
        return email
    }

    fun setMobileNumber(s: String) {
        mobileNumber = s
    }

    fun getMobileNumber(): String {
        return mobileNumber
    }

    fun validateFirstName() {                                                                       // Validate first name
        val llCreateNewAccP1FirstName: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP1FirstName)

        when {
            isNameValid(firstName) -> {
                llCreateNewAccP1FirstName.removeAllViews()
                validFields[0] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_letters_only_message)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                llCreateNewAccP1FirstName.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[0] = false
            }
        }
    }

    fun validateLastName() {                                                                        // Validate last name
        val llCreateNewAccP1LastName: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP1LastName)

        when {
            isNameValid(lastName) -> {
                llCreateNewAccP1LastName.removeAllViews()
                validFields[1] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_letters_only_message)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                llCreateNewAccP1LastName.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[1] = false
            }
        }
    }

    private fun isNameValid(s: String): Boolean {                                                   // Only accept letters, (.), (-)
        val exp = "[a-zA-Z .-]+"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    fun validateBirthDate() {
        val llCreateNewAccP1BirthDate: LinearLayout =
            appCompatActivity.findViewById(R.id.llCreateNewAccP1BirthDate)

        when {
            isBirthDateValid(birthDate) -> {
                llCreateNewAccP1BirthDate.removeAllViews()
                validFields[2] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_birth_date_format_message)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }

                llCreateNewAccP1BirthDate.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[2] = false
            }
        }
    }

    private fun isBirthDateValid(s: String): Boolean {
        //val exp = "^(0[0-9]|1[0-2])/([0-2][0-9]|3[0-1])/([0-9][0-9])?[0-9][0-9]$"
        val exp = "^(0[0-9]|1[0-2])/([0-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])?$"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    fun validateEmail() {                                                                           // Validate email
        val llCreateNewAccP1Email: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP1Email)

        when {
            Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() -> {                                    // Check if email is valid
                llCreateNewAccP1Email.removeAllViews()
                validFields[3] = true
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

                llCreateNewAccP1Email.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[3] = false
            }
        }
    }

    fun validateMobileNumber() {                                                                    // Validate mobile number
        val llCreateNewAccP1MobileNumber: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP1MobileNumber)

        when (mobileNumber.length) {
            11 -> {
                llCreateNewAccP1MobileNumber.removeAllViews()
                validFields[4] = true
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

                llCreateNewAccP1MobileNumber.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[4] = false
            }
        }
    }

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts to not empty
        firstName = etCreateNewAccP1FirstName.text.toString()
        lastName = etCreateNewAccP1LastName.text.toString()
        birthDate = etCreateNewAccP1BirthDate.text.toString()
        email = etCreateNewAccP1Email.text.toString()
        mobileNumber = etCreateNewAccP1MobileNumber.text.toString()

        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                birthDate.isNotEmpty() &&
                email.isNotEmpty() &&
                mobileNumber.isNotEmpty()
    }

    fun isValid(): Boolean {                                                                        // Validate EditTexts
        return validFields[0] && validFields[1] && validFields[2] && validFields[3] && validFields[4]
    }
}