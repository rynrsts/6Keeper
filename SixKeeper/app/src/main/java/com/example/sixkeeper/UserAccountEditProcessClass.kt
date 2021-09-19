package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.InputFilter
import android.text.InputType
import android.util.Patterns
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

open class UserAccountEditProcessClass : Fragment() {
    private val args: UserAccountEditFragmentArgs by navArgs()
    private lateinit var viewId: String
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var clUserAccountEditButton: ConstraintLayout

    private lateinit var tvUserEditLabel: TextView
    private lateinit var etUserEditTextBox: EditText
    private lateinit var ivUserEditIcon: ImageView
    private lateinit var tvUserEditNote: TextView

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun setVariables() {
        viewId = args.userAccountEditId
        appCompatActivity = activity as AppCompatActivity

        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()
    }

    fun getViewId(): String {
        return viewId
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    @SuppressLint("ResourceType")
    fun setView1() {
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)
        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_user_edit_1,
                view as ViewGroup?,
                false
        )
        clUserAccountEditContainer.addView(layoutContainer)

        clUserAccountEditButton = appCompatActivity.findViewById(R.id.clUserAccountEditButton)
        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_1,
                view as ViewGroup?,
                false
        )
        clUserAccountEditButton.addView(layoutButton)

        tvUserEditLabel = appCompatActivity.findViewById(R.id.tvUserEditLabel)
        etUserEditTextBox = appCompatActivity.findViewById(R.id.etUserEditTextBox)
        ivUserEditIcon = appCompatActivity.findViewById(R.id.ivUserEditIcon)
        tvUserEditNote = appCompatActivity.findViewById(R.id.tvUserEditNote)
    }

    fun setFirstName() {                                                                            // View for First Name
        tvUserEditLabel.setText(R.string.many_first_name)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setLastName() {                                                                             // View for Last Name
        tvUserEditLabel.setText(R.string.many_last_name)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setBirthDate() {                                                                            // View for Birth Date
        tvUserEditLabel.setText(R.string.many_birth_date)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_CLASS_TEXT
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_date_range_gray)
    }

    fun setEmail() {                                                                                // View for Email
        tvUserEditLabel.setText(R.string.many_email)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(60))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_email_gray)
    }

    fun setMobileNumber() {                                                                         // View for Mobile Number
        tvUserEditLabel.setText(R.string.many_mobile_number)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_phone_gray)
    }

    fun setUsername() {                                                                             // View for Username
        tvUserEditLabel.setText(R.string.many_username)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setInfoContent() {                                                                          // Set user information data
        etUserEditTextBox.setText(viewUserInformation())
    }

    private fun viewUserInformation(): String {                                                     // View desired user information
        val userInfoList: List<UserInfoModelClass> = databaseHandlerClass.viewUserInfo()
        val userUsername: String = databaseHandlerClass.viewUsername()
        var returnValue = ""

        var firstName = ""
        var lastName = ""
        var birthDate = ""
        var email = ""
        var mobileNumber = ""
        val username = encodingClass.decodeData(userUsername)

        for (u in userInfoList) {
            firstName = encodingClass.decodeData(u.firstName)
            lastName = encodingClass.decodeData(u.lastName)
            birthDate = encodingClass.decodeData(u.birthDate)
            email = encodingClass.decodeData(u.email)
            mobileNumber = encodingClass.decodeData(u.mobileNumber)
        }

        when (viewId) {
            "first name" ->
                returnValue = firstName
            "last name" ->
                returnValue = lastName
            "birth date" ->
                returnValue = birthDate
            "email" ->
                returnValue = email
            "mobile number" ->
                returnValue = mobileNumber
            "username" ->
                returnValue = username
        }

        return returnValue
    }

    fun setEditOnClick() {
        val clUserEditEdit: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditEdit)

        clUserEditEdit.setOnClickListener {
            if (
                    getViewId() == "first name" ||
                    getViewId() == "last name" ||
                    getViewId() == "birth date" ||
                    getViewId() == "email" ||
                    getViewId() == "mobile number" ||
                    getViewId() == "username"
            ) {
                if (getViewId() == "first name" || getViewId() == "last name") {
                    enterEditModeName()
                } else if (getViewId() == "birth date") {
                    enterEditModeBirthDate()
                } else if (getViewId() == "email") {
                    enterEditModeEmail()
                } else if (getViewId() == "mobile number") {
                    enterEditModeMobileNum()
                } else if (getViewId() == "username") {
                    enterEditModeUsername()
                }

                enterEditMode()
            }
        }
    }

    private fun enterEditModeName() {
        tvUserEditNote.setText(R.string.many_letters_only_message)
    }

    private fun enterEditModeBirthDate() {
        tvUserEditNote.setText(R.string.many_birth_date_format_message)
    }

    private fun enterEditModeEmail() {
        tvUserEditNote.setText(R.string.many_validate_email)
    }

    private fun enterEditModeMobileNum() {
        tvUserEditNote.setText(R.string.many_validate_mobile_num)
    }

    private fun enterEditModeUsername() {
        tvUserEditNote.setText(R.string.many_validate_username)
    }

    private fun enterEditMode() {                                                                   // Enter edit mode
        etUserEditTextBox.apply {
            setBackgroundResource(R.drawable.layout_edit_text)
            isFocusableInTouchMode = true
            requestFocus()
            setSelection(etUserEditTextBox.text.toString().length)
        }

        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_2,
                view as ViewGroup?,
                false
        )
        clUserAccountEditButton.apply {
            removeAllViews()
            addView(layoutButton)
        }

        setEditModeOnClick()
    }

    private fun setEditModeOnClick() {
        val clUserEditCancel: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditCancel)
        val clUserEditSave: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditSave)

        clUserEditCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            builder.setMessage(R.string.user_edit_alert_message)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                goBackToViewMode()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title)
            alert.show()
        }

        clUserEditSave.setOnClickListener {
            val text = etUserEditTextBox.text.toString()

            if (getViewId() == "first name" && isNameValid(text)) {
                updateInfo("first_name")
                setInfoContent()
                goBackToViewMode()
            } else if (getViewId() == "last name" && isNameValid(text)) {
                updateInfo("last_name")
                setInfoContent()
                goBackToViewMode()
            } else if (getViewId() == "birth date" && isBirthDateValid(text)) {
                updateInfo("birth_date")
                setInfoContent()
                goBackToViewMode()
            } else if (getViewId() == "email" && Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                updateInfo("email")
                setInfoContent()
                goBackToViewMode()
            } else if (getViewId() == "mobile number" && text.length == 11) {
                updateInfo("mobile_number")
                setInfoContent()
                goBackToViewMode()
            } else if (getViewId() == "username" && isUsernameValid(text)) {
                updateUsername()
                setInfoContent()
                goBackToViewMode()
                setUsernameInMenu()
            }
        }
    }

    private fun isNameValid(s: String): Boolean {                                                   // Accept letters, (.) and (-) only
        val exp = "[a-zA-Z .-]+"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun isBirthDateValid(s: String): Boolean {                                              // Accept MM//DD/YYYY format only
        //val exp = "^(0[0-9]|1[0-2])/([0-2][0-9]|3[0-1])/([0-9][0-9])?[0-9][0-9]$"
        val exp = "^(0[0-9]|1[0-2])/([0-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])?$"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun isUsernameValid(s: String): Boolean {                                                        // Accept letters, numbers, (.), (_) and (-) only
        val exp = "[a-zA-Z0-9._-]{6,}"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateInfo(field: String) {                                                         // Update desired information
        val input = etUserEditTextBox.text.toString()

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        databaseHandlerClass.updateUserInfo(field, encodingClass.encodeData(input), date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateUsername() {
        val input = etUserEditTextBox.text.toString()

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        databaseHandlerClass.updateUsername(encodingClass.encodeData(input), date)
    }

    private fun goBackToViewMode() {                                                                // Go back to view mode
        etUserEditTextBox.apply {
            setBackgroundResource(R.drawable.layout_edit_text_2)
            isFocusableInTouchMode = false
            clearFocus()
        }
        tvUserEditNote.text = null

        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_1,
                view as ViewGroup?,
                false
        )
        clUserAccountEditButton.apply {
            removeAllViews()
            addView(layoutButton)
        }

        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(                                                // Close keyboard
                        appCompatActivity.currentFocus?.windowToken,
                        0
                )
        }

        setEditOnClick()
    }

    private fun setUsernameInMenu() {                                                               // Set update username in menu
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var username = ""

        for (u in userAccList) {
            username = encodingClass.decodeData(u.username)
        }

        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val tvNavigationHeaderUsername: TextView =
                headerView.findViewById(R.id.tvNavigationHeaderUsername)

        tvNavigationHeaderUsername.text = username
    }
}