package com.example.sixkeeper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecurityQuestionFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etSecurityQuestionFirstName: EditText
    private lateinit var etSecurityQuestionLastName: EditText
    private lateinit var etSecurityQuestionBirthDate: EditText
    private lateinit var etSecurityQuestionEmail: EditText
    private lateinit var etSecurityQuestionMobileNumber: EditText

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private lateinit var email: String
    private lateinit var mobileNumber: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_security_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setButtonOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        etSecurityQuestionFirstName =
                appCompatActivity.findViewById(R.id.etSecurityQuestionFirstName)
        etSecurityQuestionLastName =
                appCompatActivity.findViewById(R.id.etSecurityQuestionLastName)
        etSecurityQuestionBirthDate =
                appCompatActivity.findViewById(R.id.etSecurityQuestionBirthDate)
        etSecurityQuestionEmail = appCompatActivity.findViewById(R.id.etSecurityQuestionEmail)
        etSecurityQuestionMobileNumber =
                appCompatActivity.findViewById(R.id.etSecurityQuestionMobileNumber)
    }

    private fun setButtonOnClick() {
        val acbSecurityQuestionConfirm: Button =
                appCompatActivity.findViewById(R.id.acbSecurityQuestionConfirm)

        acbSecurityQuestionConfirm.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (isNotEmpty()) {
                    val encodedFirstName = encodingClass.encodeData(firstName)
                    val encodedLastName = encodingClass.encodeData(lastName)
                    val encodedBirthDate = encodingClass.encodeData(birthDate)
                    val encodedEmail = encodingClass.encodeData(email)
                    val encodedMobileNumber = encodingClass.encodeData(mobileNumber)

                    val immKeyboard: InputMethodManager =
                            appCompatActivity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager

                    if (
                            databaseHandlerClass.verifyInformation(
                                    encodedFirstName,
                                    encodedLastName,
                                    encodedBirthDate,
                                    encodedEmail,
                                    encodedMobileNumber
                            )
                    ) {
                        val forgotCredentialsActivity: ForgotCredentialsActivity =
                                activity as ForgotCredentialsActivity

                        if (immKeyboard.isActive) {
                            immKeyboard.hideSoftInputFromWindow(                                    // Close keyboard
                                    appCompatActivity.currentFocus?.windowToken,
                                    0
                            )
                        }

                        forgotCredentialsActivity.manageForgotFragments(
                                forgotCredentialsActivity.getCredential()
                        )
                    } else {
                        if (immKeyboard.isActive) {
                            immKeyboard.hideSoftInputFromWindow(                                    // Close keyboard
                                    appCompatActivity.currentFocus?.windowToken,
                                    0
                            )
                        }

                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.security_question_invalid,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.many_fill_missing_fields,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            } else {
                internetToast()
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun isNotEmpty(): Boolean {                                                             // Validate EditTexts are not empty
        firstName = etSecurityQuestionFirstName.text.toString()
        lastName = etSecurityQuestionLastName.text.toString()
        birthDate = etSecurityQuestionBirthDate.text.toString()
        email = etSecurityQuestionEmail.text.toString()
        mobileNumber = etSecurityQuestionMobileNumber.text.toString()

        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                birthDate.isNotEmpty() &&
                email.isNotEmpty() &&
                mobileNumber.isNotEmpty()
    }
}