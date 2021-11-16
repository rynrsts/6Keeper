package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class PasswordGeneratorProcessClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var tvPassGeneratorGeneratedPass: TextView
    private lateinit var llPassGeneratorContainer: LinearLayout
    private lateinit var customInflatedLayout: View
    private lateinit var cbPassGeneratorLowercase: CheckBox
    private lateinit var cbPassGeneratorUppercase: CheckBox
    private lateinit var cbPassGeneratorNumber: CheckBox
    private lateinit var cbPassGeneratorSpecialChar: CheckBox

    private lateinit var key: ByteArray
    private lateinit var passwordType: String

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun setTvPassGeneratorGeneratedPass(s: String) {
        tvPassGeneratorGeneratedPass.text = s
    }

    fun getTvPassGeneratorGeneratedPass(): TextView {
        return tvPassGeneratorGeneratedPass
    }

    fun getCustomInflatedLayout(): View {
        return customInflatedLayout
    }

    fun getCbPassGeneratorLowercase(): CheckBox {
        return cbPassGeneratorLowercase
    }

    fun getCbPassGeneratorUppercase(): CheckBox {
        return cbPassGeneratorUppercase
    }

    fun getCbPassGeneratorNumber(): CheckBox {
        return cbPassGeneratorNumber
    }

    fun getCbPassGeneratorSpecialChar(): CheckBox {
        return cbPassGeneratorSpecialChar
    }

    fun setPasswordType(s: String) {
        passwordType = s
    }

    fun getPasswordType(): String {
        return passwordType
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()

        tvPassGeneratorGeneratedPass =
                appCompatActivity.findViewById(R.id.tvPassGeneratorGeneratedPass)
        llPassGeneratorContainer = appCompatActivity.findViewById(R.id.llPassGeneratorContainer)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()

        passwordType = "medium"
    }

    fun disableMenuItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = true
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = false
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
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

    @SuppressLint("InflateParams")
    fun populateMediumStrong(mediumOrStrong: String) {
        val inflatedLayout = layoutInflater.inflate(
                R.layout.layout_pass_gen_tab_medium_strong,
                null,
                true
        )
        val tvPassGenMediumStrongDesc1: TextView =
                inflatedLayout.findViewById(R.id.tvPassGenMediumStrongDesc1)
        val tvPassGenMediumStrongDesc2: TextView =
                inflatedLayout.findViewById(R.id.tvPassGenMediumStrongDesc2)

        if (mediumOrStrong == "medium") {                                                           // Show Medium description
            tvPassGenMediumStrongDesc1.setText(R.string.pass_generator_medium_desc_1)
            tvPassGenMediumStrongDesc2.setText(R.string.pass_generator_medium_desc_2)
        } else if (mediumOrStrong == "strong") {                                                    // Show Strong description
            tvPassGenMediumStrongDesc1.setText(R.string.pass_generator_strong_desc_1)
            tvPassGenMediumStrongDesc2.setText(R.string.pass_generator_strong_desc_2)
        }

        llPassGeneratorContainer.removeAllViews()
        llPassGeneratorContainer.addView(inflatedLayout)
    }

    @SuppressLint("InflateParams")
    fun populateCustom() {                                                                          // Show Custom content
        customInflatedLayout = layoutInflater.inflate(
                R.layout.layout_pass_gen_tab_custom,
                null,
                true
        )
        customInflatedLayout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val inflateContainer = LinearLayout(context)
        inflateContainer.apply {
            layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(customInflatedLayout)
        }

        llPassGeneratorContainer.apply {
            removeAllViews()
            addView(inflateContainer)
        }

        val acbPassGenPlus: Button = customInflatedLayout.findViewById(R.id.acbPassGenPlus)
        val acbPassGenMinus: Button = customInflatedLayout.findViewById(R.id.acbPassGenMinus)
        val etPassGeneratorLength: EditText =
                customInflatedLayout.findViewById(R.id.etPassGeneratorLength)
        cbPassGeneratorLowercase = customInflatedLayout.findViewById(R.id.cbPassGeneratorLowercase)
        cbPassGeneratorUppercase = customInflatedLayout.findViewById(R.id.cbPassGeneratorUppercase)
        cbPassGeneratorNumber = customInflatedLayout.findViewById(R.id.cbPassGeneratorNumber)
        cbPassGeneratorSpecialChar =
                customInflatedLayout.findViewById(R.id.cbPassGeneratorSpecialChar)

        acbPassGenPlus.setOnClickListener {
            val lengthString = etPassGeneratorLength.text.toString()

            if (lengthString.isNotEmpty()) {
                val length = Integer.parseInt(lengthString) + 1

                if (length in 4..99) {
                    etPassGeneratorLength.apply {
                        setText(length.toString())
                        setSelection(etPassGeneratorLength.text.length)
                    }
                } else if (length < 4) {
                    toastMinimumOf4(etPassGeneratorLength)
                }
            } else {
                toastMinimumOf4(etPassGeneratorLength)
            }

            etPassGeneratorLength.clearFocus()
            closeKeyboard()
        }

        acbPassGenMinus.setOnClickListener {
            val lengthString = etPassGeneratorLength.text.toString()

            if (lengthString.isNotEmpty()) {
                val length = Integer.parseInt(lengthString) - 1

                if (length in 4..98) {
                    etPassGeneratorLength.apply {
                        setText(length.toString())
                        setSelection(etPassGeneratorLength.text.length)
                    }
                } else {
                    toastMinimumOf4(etPassGeneratorLength)
                }
            } else {
                toastMinimumOf4(etPassGeneratorLength)
            }

            etPassGeneratorLength.clearFocus()
            closeKeyboard()
        }
    }

    fun toastMinimumOf4(etPassGeneratorLength: EditText) {
        etPassGeneratorLength.setText("4")

        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.pass_generator_length_of_4,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    fun generatePassword(i: Int): String {                                                          // Generate password
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val number = "0123456789"
        val specialChar = "!@#$%^&*()=+._-"
        val characters = ArrayList<String>()
        val include = booleanArrayOf(false, false, false, false)
        val boolChar = booleanArrayOf(false, false, false, false)
        var length = 4

        characters.add(lowercase)
        characters.add(uppercase)
        characters.add(number)
        characters.add(specialChar)

        when (passwordType) {
            "medium" -> {
                length = (8..11).random()
                include[0] = true
                include[1] = true
                include[2] = true
            }
            "strong" -> {
                length = (11..13).random()
                include[0] = true
                include[1] = true
                include[2] = true
                include[3] = true
            }
            "custom" -> {
                length = i

                if (cbPassGeneratorLowercase.isChecked) {
                    include[0] = true
                }
                if (cbPassGeneratorUppercase.isChecked) {
                    include[1] = true
                }
                if (cbPassGeneratorNumber.isChecked ) {
                    include[2] = true
                }
                if (cbPassGeneratorSpecialChar.isChecked) {
                    include[3] = true
                }
            }
        }

        val randomString = StringBuilder(length)

        for (x in 1..length) {
            var charRandom: Int

            when {
                include.elementAt(0) && !boolChar.elementAt(0) -> {
                    charRandom = 0
                    boolChar[0] = true
                }
                include.elementAt(1) && !boolChar.elementAt(1) -> {
                    charRandom = 1
                    boolChar[1] = true
                }
                include.elementAt(2) && !boolChar.elementAt(2) -> {
                    charRandom = 2
                    boolChar[2] = true
                }
                include.elementAt(3) && !boolChar.elementAt(3) -> {
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
            randomString.append(random.toString())
        }

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt(
                                "A $passwordType type password was generated.", key
                        ),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )

        return shuffle(randomString.toString())
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    private fun shuffle(input: String): String {                                                    // Shuffle for true randomness
        val characters: MutableList<Char> = ArrayList()

        for (c in input.toCharArray()) {
            characters.add(c)
        }

        val output = StringBuilder(input.length)

        while (characters.size != 0) {
            val randPicker = (Math.random() * characters.size).toInt()
            output.append(characters.removeAt(randPicker))
        }

        return output.toString()
    }

    @SuppressLint("ShowToast")
    fun saveGeneratedPass(generatedPass: String) {                                                  // Save generated password to database
        val encryptedDelete = encryptionClass.encrypt(0.toString(), key)
        val userSavedPass: List<UserSavedPassModelClass> =
                databaseHandlerClass.viewSavedPass(encryptedDelete)
        val encryptedGeneratedPass = encryptionClass.encrypt(generatedPass, key)
        var passId = 100001
        val lastId = databaseHandlerClass.getLastIdOfSavedPasswords()
        var existing = false
        var toast: Toast? = null

        if (lastId.isNotEmpty()) {
            passId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
        }

        for (u in userSavedPass) {
            if (encryptedGeneratedPass == u.generatedPassword) {
                existing = true
                break
            }
        }

        if (!existing) {
            val status = databaseHandlerClass.addGeneratedPass(
                    UserSavedPassModelClass(
                            encryptionClass.encrypt(passId.toString(), key),
                            encryptedGeneratedPass,
                            encryptionClass.encrypt(getCurrentDate(), key),
                            encryptedDelete,
                            ""
                    )
            )

            if (status > -1) {
                toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.pass_generator_pass_saved,
                        Toast.LENGTH_SHORT
                )
            }

            databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                    UserActionLogModelClass(
                            encryptionClass.encrypt(getLastActionLogId().toString(), key),
                            encryptionClass.encrypt("Generated password was saved.", key),
                            encryptionClass.encrypt(getCurrentDate(), key)
                    )
            )
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