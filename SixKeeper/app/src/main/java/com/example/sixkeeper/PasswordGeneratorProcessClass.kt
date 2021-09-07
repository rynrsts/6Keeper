package com.example.sixkeeper

import android.app.Activity
import android.view.Gravity
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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

        for (x in 1..length){
            val random = (characters.indices).random()
            stringBuilder.append(characters[random])
        }

        return stringBuilder.toString()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        attActivity = activity
    }

    // Save generated password to database
    fun saveGeneratedPass(generatedPass: String) {
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

    // View saved password
    fun viewSavedPass() {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val userSavedPassList: List<UserSavedPassModelClass> = databaseHandlerClass.viewSavedPass()

        val userArrayId = Array(userSavedPassList.size){"0"}
        val userArrayPass = Array(userSavedPassList.size){"null"}

        for ((index, u) in userSavedPassList.withIndex()) {
            userArrayId[index] = u.passId.toString()
            userArrayPass[index] = u.generatedPassword
        }

//        val myListAdapter = MyListAdapter(this,empArrayId,empArrayName,empArrayEmail)
//        listView.adapter = myListAdapter
    }
}