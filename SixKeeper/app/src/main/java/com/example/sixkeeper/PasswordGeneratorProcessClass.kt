package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class PasswordGeneratorProcessClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etPassGeneratorLength: EditText
    private lateinit var cbPassGeneratorLowercase: CheckBox
    private lateinit var cbPassGeneratorUppercase: CheckBox
    private lateinit var cbPassGeneratorSpecialChar: CheckBox
    private lateinit var cbPassGeneratorNumber: CheckBox
    private lateinit var tvPassGeneratorGeneratedPass: TextView

    private lateinit var passwordGeneratorLength: String

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

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
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        etPassGeneratorLength = appCompatActivity.findViewById(R.id.etPassGeneratorLength)
        cbPassGeneratorLowercase = appCompatActivity.findViewById(R.id.cbPassGeneratorLowercase)
        cbPassGeneratorUppercase = appCompatActivity.findViewById(R.id.cbPassGeneratorUppercase)
        cbPassGeneratorSpecialChar = appCompatActivity.findViewById(R.id.cbPassGeneratorSpecialChar)
        cbPassGeneratorNumber = appCompatActivity.findViewById(R.id.cbPassGeneratorNumber)
        tvPassGeneratorGeneratedPass =
                appCompatActivity.findViewById(R.id.tvPassGeneratorGeneratedPass)
    }

    fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    appCompatActivity.currentFocus?.windowToken,
                    0
            )
        }
    }

    fun generatePassword(length: Int): String {                                                     // Generate password
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val specialChar = "!@#$%^&*()=+._-"
        val number = "0123456789"
        val characters = ArrayList<String>()
        val boolChar = booleanArrayOf(false, false, false, false)
        val stringBuilder = StringBuilder(length)

        characters.add(lowercase)
        characters.add(uppercase)
        characters.add(specialChar)
        characters.add(number)

        for (x in 1..length) {
            var charRandom: Int

            when {
                cbPassGeneratorLowercase.isChecked && !boolChar.elementAt(0) -> {
                    charRandom = 0
                    boolChar[0] = true
                }
                cbPassGeneratorUppercase.isChecked && !boolChar.elementAt(1) -> {
                    charRandom = 1
                    boolChar[1] = true
                }
                cbPassGeneratorSpecialChar.isChecked && !boolChar.elementAt(2) -> {
                    charRandom = 2
                    boolChar[2] = true
                }
                cbPassGeneratorNumber.isChecked && !boolChar.elementAt(3) -> {
                    charRandom = 3
                    boolChar[3] = true
                }
                else -> {
                    do {
                        charRandom = (characters.indices).random()
                    } while(!boolChar[charRandom])
                }
            }

            val randomTemp: String = characters.elementAt(charRandom)
            val random = randomTemp.random()
            stringBuilder.append(random)
        }

        return shuffle(stringBuilder.toString())
    }

    private fun shuffle(input: String): String {
        val characters: MutableList<Char> = ArrayList()

        for (c in input.toCharArray()) {
            characters.add(c)
        }

        val output: java.lang.StringBuilder = java.lang.StringBuilder(input.length)

        while (characters.size != 0) {
            val randPicker = (Math.random() * characters.size).toInt()
            output.append(characters.removeAt(randPicker))
        }

        return output.toString()
    }

    // Save generated password to database
    @SuppressLint("SimpleDateFormat", "ShowToast")
    fun saveGeneratedPass(generatedPass: String) {                                                  // Save generated password to database
        val userSavedPass: List<UserSavedPassModelClass> = databaseHandlerClass.viewSavedPass()
        var passId = 100001
        val encodedGeneratedPass = encodingClass.encodeData(generatedPass)
        var existing = false
        var toast: Toast? = null

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        for (u in userSavedPass) {
            if (encodedGeneratedPass == u.generatedPassword) {
                existing = true
                break
            }

            if (!userSavedPass.isNullOrEmpty()) {
                passId = Integer.parseInt(encodingClass.decodeData(u.passId)) + 1
            }
        }

        if (!existing) {
            val status = databaseHandlerClass.addGeneratedPass(
                    UserSavedPassModelClass(
                            encodingClass.encodeData(passId.toString()),
                            encodedGeneratedPass,
                            encodingClass.encodeData(date)
                    )
            )

            if (status > -1) {
                toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.pass_generator_pass_saved,
                        Toast.LENGTH_SHORT
                )
            }
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.pass_generator_pass_already_saved,
                    Toast.LENGTH_SHORT
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}