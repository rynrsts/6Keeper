package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast

class CreateNewAccountP2Fragment : CreateNewAccountP2ValidationClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_create_new_account_p2,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setEditTextFocusChange()
        setButtonOnClick()
    }

    private fun setEditTextFocusChange() {                                                          // Set action when focus left the EditText
        getEtCreateNewAccP2Email().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setEmail(getEtCreateNewAccP2Email().text.toString())

                    when {
                        !hasFocus && getEmail().isNotEmpty() ->
                            validateEmail()
                    }
                }

        getEtCreateNewAccP2MobileNumber().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setMobileNumber(getEtCreateNewAccP2MobileNumber().text.toString())

                    when {
                        !hasFocus && getMobileNumber().isNotEmpty() ->
                            validateMobileNumber()
                    }
                }
    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP2EmailSendCode: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP2EmailSendCode)
        val acbCreateNewAccP2VerifyEmail: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP2VerifyEmail)
        val acbCreateNewAccP2MobileNumSendCode: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP2MobileNumSendCode)
        val acbCreateNewAccP2VerifyMobileNum: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP2VerifyMobileNum)
        val acbCreateNewAccP2Next: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP2Next)

        acbCreateNewAccP2EmailSendCode.setOnClickListener {
            when {
                emailIsNotEmpty() -> {
                    validateEmail()
                }
                else -> {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.many_fill_email_field,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }

        acbCreateNewAccP2VerifyEmail.setOnClickListener {
            when {
                emailIsNotEmpty() && emailEnterCodeIsNotEmpty() -> {
                    //
                }
                else -> {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.many_enter_code_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }

        acbCreateNewAccP2MobileNumSendCode.setOnClickListener {
            when {
                mobileNumberIsNotEmpty() -> {
                    validateMobileNumber()
                }
                else -> {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.many_fill_mobile_num_field,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }

        acbCreateNewAccP2VerifyMobileNum.setOnClickListener {
            when {
                mobileNumberIsNotEmpty() && mobileNumEnterCodeIsNotEmpty() -> {
                    //
                }
                else -> {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.many_enter_code_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }

        // TODO: Create New Account P2
        acbCreateNewAccP2Next.setOnClickListener {
//            if (isNotEmpty()) {
//                validateEmail()
//                validateMobileNumber()
//
//                if (isValid()) {
            val immKeyboard: InputMethodManager =
                    getAppCompatActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager

            if (immKeyboard.isActive) {
                immKeyboard.hideSoftInputFromWindow(
                        getAppCompatActivity().currentFocus?.windowToken,
                        0
                )
            }

            val createNewAccountActivity: CreateNewAccountActivity =
                    activity as CreateNewAccountActivity
            createNewAccountActivity.manageCreateNewAccFragments(
                    createNewAccountActivity.getCreateNewAccP3()
            )
//                }
//            } else {
//                val toast: Toast = Toast.makeText(
//                    getAppCompatActivity().applicationContext,
//                    R.string.many_fill_missing_fields,
//                    Toast.LENGTH_SHORT
//                )
//                toast.apply {
//                    setGravity(Gravity.CENTER, 0, 0)
//                    show()
//                }
//            }
        }
    }
}