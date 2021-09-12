package com.example.sixkeeper

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.regex.Pattern

open class CreateNewAccountP3ValidationClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etCreateNewAccP3Username: EditText
    private lateinit var etCreateNewAccP3Password: EditText
    private lateinit var etCreateNewAccP3ConfirmPassword: EditText

    private lateinit var ivCreateNewAccP3TogglePass: ImageView
    private lateinit var ivCreateNewAccP3ToggleConfirmPass: ImageView

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    private lateinit var validFields: ArrayList<Boolean>

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etCreateNewAccP3Username = appCompatActivity.findViewById(R.id.etCreateNewAccP3Username)
        etCreateNewAccP3Password = appCompatActivity.findViewById(R.id.etCreateNewAccP3Password)
        etCreateNewAccP3ConfirmPassword =
                appCompatActivity.findViewById(R.id.etCreateNewAccP3ConfirmPassword)

        ivCreateNewAccP3TogglePass = appCompatActivity.findViewById(R.id.ivCreateNewAccP3TogglePass)
        ivCreateNewAccP3ToggleConfirmPass =
                appCompatActivity.findViewById(R.id.ivCreateNewAccP3ToggleConfirmPass)

        validFields = ArrayList(3)
        for (b: Int in 0..2)
            validFields.add(false)
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtCreateNewAccP3Username(): EditText {
        return etCreateNewAccP3Username
    }

    fun getEtCreateNewAccP3Password(): EditText {
        return etCreateNewAccP3Password
    }

    fun getEtCreateNewAccP3ConfirmPassword(): EditText {
        return etCreateNewAccP3ConfirmPassword
    }

    fun getIvCreateNewAccP3TogglePass(): ImageView {
        return ivCreateNewAccP3TogglePass
    }

    fun getIvCreateNewAccP3ToggleConfirmPass(): ImageView {
        return ivCreateNewAccP3ToggleConfirmPass
    }

    fun setUsername(s: String) {
        username = s
    }

    fun getUsername(): String {
        return username
    }

    fun setPassword(s: String) {
        password = s
    }

    fun getPassword(): String {
        return password
    }

    fun setConfirmPassword(s: String) {
        confirmPassword = s
    }

    fun getConfirmPassword(): String {
        return confirmPassword
    }

    fun validateUsername() {                                                                        // Validate username
        val llCreateNewAccP3Username: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP3Username)

        when {
            isUsernameValid() -> {
                llCreateNewAccP3Username.removeAllViews()
                validFields[0] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_validate_username)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                    textSize = 14F
                }

                llCreateNewAccP3Username.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[0] = false
            }
        }
    }

    private fun isUsernameValid(): Boolean {                                                        // Accept letters, numbers, (.), (_) and (-) only
        val exp = "[a-zA-Z0-9._-]{6,}"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(username).matches()
    }

    fun validatePassword() {                                                                        // Validate password
        val llCreateNewAccP3Password: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP3Password)

        when {
            isPasswordValid() -> {
                llCreateNewAccP3Password.removeAllViews()
                validFields[1] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_validate_password)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                    textSize = 14F
                }

                llCreateNewAccP3Password.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[1] = false
            }
        }
    }

    private fun isPasswordValid(): Boolean {                                                        // Accept 1 lowercase, uppercase, number, (.), (_) and (-) only
        val exp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])(?=.*[._-])(?=\\S+\$)(?=.{8,})(^[a-zA-Z0-9._-]+\$)"
        val pattern = Pattern.compile(exp)
        return pattern.matcher(password).matches()
    }

    fun validateConfirmPassword() {                                                                 // Validate confirm password
        val llCreateNewAccP3ConfirmPassword: LinearLayout =
                appCompatActivity.findViewById(R.id.llCreateNewAccP3ConfirmPassword)
        password = etCreateNewAccP3Password.text.toString()

        when (confirmPassword) {
            password -> {
                llCreateNewAccP3ConfirmPassword.removeAllViews()
                validFields[2] = true
            }
            else -> {
                val tvMessage = TextView(context)
                tvMessage.apply {
                    layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setText(R.string.many_validate_confirm_pass)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                    textSize = 14F
                }

                llCreateNewAccP3ConfirmPassword.apply {
                    removeAllViews()
                    addView(tvMessage)
                }
                validFields[2] = false
            }
        }
    }

    fun isNotEmpty(): Boolean {                                                                     // Validate EditTexts are not empty
        username = etCreateNewAccP3Username.text.toString()
        password = etCreateNewAccP3Password.text.toString()
        confirmPassword = etCreateNewAccP3ConfirmPassword.text.toString()

        return username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    fun isValid(): Boolean {                                                                        // Validate EditTexts
        return validFields[0] && validFields[1] && validFields[2]
    }
}