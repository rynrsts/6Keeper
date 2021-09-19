package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

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

    fun setInfoContent() {                                                                          // Set user information data
        etUserEditTextBox.setText(viewUserInformation())
    }

    private fun viewUserInformation(): String {                                                     // View user information
        val userInfoList: List<UserInfoModelClass> = databaseHandlerClass.viewUserInfo()
        var returnValue = ""

        var firstName = ""
        var lastName = ""
        var birthDate = ""
        var email = ""
        var mobileNumber = ""

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
        }

        return returnValue
    }

    fun enterEditModeName() {
        tvUserEditNote.setText(R.string.many_letters_only_message)
    }

    fun enterEditModeBirthDate() {
        tvUserEditNote.setText(R.string.many_birth_date_format_message)
    }

    fun enterEditModeEmail() {
        tvUserEditNote.setText(R.string.many_validate_email)
    }

    fun enterEditModeMobileNum() {
        tvUserEditNote.setText(R.string.many_validate_mobile_num)
    }

    fun enterEditMode() {
        etUserEditTextBox.apply {
            isEnabled = true
            requestFocus()
            setSelection(etUserEditTextBox.text.toString().length)
        }
        showKeyboard()

        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_2,
                view as ViewGroup?,
                false
        )
        clUserAccountEditButton.apply {
            removeAllViews()
            addView(layoutButton)
        }
    }

    private fun showKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
        immKeyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)                          // Open keyboard
    }
}