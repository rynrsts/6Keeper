package com.example.sixkeeper

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

open class UserAccountEditProcessClass : Fragment() {
    private val args: UserAccountEditFragmentArgs by navArgs()
    private lateinit var viewId: String
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var tvUserEditLabel: TextView
    private lateinit var etUserEditTextBox: TextView
    private lateinit var ivUserEditIcon: ImageView

    private lateinit var ivUserEditButtonIcon: ImageView
    private lateinit var tvUserEditButtonText: TextView

    fun setVariables() {
        viewId = args.userAccountEditId
        appCompatActivity = activity as AppCompatActivity
    }

    fun getViewId(): String {
        return viewId
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun setView1() {
        val vsUserAccountEditContainer: ViewStub =
                appCompatActivity.findViewById(R.id.vsUserAccountEditContainer)
        val vsUserAccountEditButton: ViewStub =
                appCompatActivity.findViewById(R.id.vsUserAccountEditButton)

        vsUserAccountEditContainer.apply {
            layoutResource = R.layout.layout_user_edit_1
            inflate()
        }
        vsUserAccountEditButton.apply {
            layoutResource = R.layout.layout_user_edit_button
            inflate()

        }

        tvUserEditLabel = appCompatActivity.findViewById(R.id.tvUserEditLabel)
        etUserEditTextBox = appCompatActivity.findViewById(R.id.etUserEditTextBox)
        ivUserEditIcon = appCompatActivity.findViewById(R.id.ivUserEditIcon)
        ivUserEditButtonIcon = appCompatActivity.findViewById(R.id.ivUserEditButtonIcon)
        tvUserEditButtonText = appCompatActivity.findViewById(R.id.tvUserEditButtonText)

        ivUserEditButtonIcon.setImageResource(R.drawable.ic_edit_white)
        tvUserEditButtonText.setText(R.string.many_edit)
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

    fun doFirstName() {                                                                             // Action for First Name
        etUserEditTextBox.apply {
            isEnabled = true
            requestFocus()
        }
        showKeyboard()
        ivUserEditButtonIcon.setImageResource(R.drawable.ic_save_white)
        tvUserEditButtonText.setText(R.string.many_save)
    }

    private fun showKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
        immKeyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)                          // Open keyboard
    }
}