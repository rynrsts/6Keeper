package com.example.sixkeeper

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
//    private lateinit var dpCreateNewAccP1BirthDate: DatePicker

    private lateinit var firstName: String
    private lateinit var lastName: String
//    private var month: Int = 0
//    private var day: Int = 0
//    private var year: Int = 0
//    private lateinit var birthDate: String

    private lateinit var validFields: ArrayList<Boolean>

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etCreateNewAccP1FirstName = appCompatActivity.findViewById(R.id.etCreateNewAccP1FirstName)
        etCreateNewAccP1LastName =
                appCompatActivity.findViewById(R.id.etCreateNewAccP1LastName)

        validFields = ArrayList(2)
        for (b: Int in 0..1)
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

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts to not empty
        firstName = etCreateNewAccP1FirstName.text.toString()
        lastName = etCreateNewAccP1LastName.text.toString()

        return firstName.isNotEmpty() && lastName.isNotEmpty()
    }

    fun isValid(): Boolean {                                                                        // Validate EditTexts
        return validFields[0] && validFields[1]
    }
}