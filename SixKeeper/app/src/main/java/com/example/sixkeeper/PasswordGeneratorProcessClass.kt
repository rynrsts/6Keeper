package com.example.sixkeeper

import android.app.Activity
import android.view.Gravity
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.regex.Pattern

open class PasswordGeneratorProcessClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity

    private lateinit var etPassGeneratorLength: EditText
    private lateinit var cbPassGeneratorLowercase: CheckBox
    private lateinit var cbPassGeneratorUppercase: CheckBox
    private lateinit var cbPassGeneratorSpecialChar: CheckBox
    private lateinit var cbPassGeneratorNumber: CheckBox
    private lateinit var tvPassGeneratorGeneratedPass: TextView

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
    }

//    TODO: Generate Password: require all checked boxes
    fun generatePassword(length: Int): String {                                                     // Generate password
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val specialChar = "!@#$%^&*()=+._-"
        val number = "0123456789"
//        val exp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])(?=.*[._-])(?=\\S+\$)(^[a-zA-Z0-9._-]+\$)"
//        val pattern = Pattern.compile(exp)
        val characters = ArrayList<String>()
        val stringBuilder = StringBuilder(length)

        if (cbPassGeneratorLowercase.isChecked) {
            characters.add(lowercase)
        }
        if (cbPassGeneratorUppercase.isChecked) {
            characters.add(uppercase)
        }
        if (cbPassGeneratorSpecialChar.isChecked) {
            characters.add(specialChar)
        }
        if (cbPassGeneratorNumber.isChecked) {
            characters.add(number)
        }

        for (x in 1..length) {
            val charRandom = (characters.indices).random()
            val random = characters.random().elementAt(charRandom)
            stringBuilder.append(random)
        }

//        do {
//            for (x in 1..length) {
//                val random = (characters.indices).random()
//                stringBuilder.append(characters[random])
//            }
//        } while(!pattern.matcher(stringBuilder.toString()).matches())

        return stringBuilder.toString()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    // Save generated password to database
    fun saveGeneratedPass(generatedPass: String) {                                                  // Save generated password to database
        val passId: Int = (100000..999999).random()
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)

        val status = databaseHandlerClass.addGeneratedPass(
                UserSavedPassModelClass(passId, generatedPass)
        )

        if (status > -1) {
            val toast: Toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.pass_generator_pass_saved, Toast.LENGTH_LONG
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }
}