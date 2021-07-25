package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button

class CreateNewAccountP3Fragment : CreateNewAccountP3ValidationClass() {
    private var passwordVisibility: Int = 0
    private var confirmPasswordVisibility: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_create_new_account_p3,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setEditTextFocusChange()
        setEditTextOnChange()
        setButtonOnClick()
        setImageViewOnClick()
    }

    private fun setEditTextFocusChange() {                                                          // Set action when focus left the EditText
        getEtCreateNewAccP3Username().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setUsername(getEtCreateNewAccP3Username().text.toString())

                    when {
                        !hasFocus && getUsername().isNotEmpty() ->
                            validateUsername()
                    }
                }

        getEtCreateNewAccP3Password().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setPassword(getEtCreateNewAccP3Password().text.toString())

                    when {
                        !hasFocus && getPassword().isNotEmpty() ->
                            validatePassword()
                    }
                }

        getEtCreateNewAccP3ConfirmPassword().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setConfirmPassword(getEtCreateNewAccP3ConfirmPassword().text.toString())

                    when {
                        !hasFocus && getConfirmPassword().isNotEmpty() ->
                            validateConfirmPassword()
                    }
                }
    }

    private fun setEditTextOnChange() {
        getEtCreateNewAccP3Password().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setPassword(getEtCreateNewAccP3Password().text.toString())

                if (getPassword().isNotEmpty()) {
                    if (passwordVisibility == 0) {
                        getIvCreateNewAccP3TogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        passwordVisibility = 1

                        getEtCreateNewAccP3Password().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtCreateNewAccP3Password().text.length)
                        }
                    }
                } else {
                    getIvCreateNewAccP3TogglePass().setImageResource(0)
                    passwordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        getEtCreateNewAccP3ConfirmPassword().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setConfirmPassword(getEtCreateNewAccP3ConfirmPassword().text.toString())

                if (getConfirmPassword().isNotEmpty()) {
                    if (confirmPasswordVisibility == 0) {
                        getIvCreateNewAccP3ToggleConfirmPass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        confirmPasswordVisibility = 1

                        getEtCreateNewAccP3ConfirmPassword().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtCreateNewAccP3ConfirmPassword().text.length)
                        }
                    }
                } else {
                    getIvCreateNewAccP3ToggleConfirmPass().setImageResource(0)
                    confirmPasswordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP3Next: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP3Next)

        // TODO: Create New Account P3
        acbCreateNewAccP3Next.setOnClickListener {
//            if (isNotEmpty()) {
//                validateUsername()
//                validatePassword()
//                validateConfirmPassword()
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
                    createNewAccountActivity.getCreateNewAccP4()
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

    private fun setImageViewOnClick() {
        getIvCreateNewAccP3TogglePass().setOnClickListener {
            when (passwordVisibility) {
                1 -> {
                    getIvCreateNewAccP3TogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtCreateNewAccP3Password().apply {
                        transformationMethod = null
                        setSelection(getEtCreateNewAccP3Password().text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {
                    getIvCreateNewAccP3TogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtCreateNewAccP3Password().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtCreateNewAccP3Password().text.length)
                    }
                    passwordVisibility = 1
                }
            }
        }

        getIvCreateNewAccP3ToggleConfirmPass().setOnClickListener {
            when (confirmPasswordVisibility) {
                1 -> {
                    getIvCreateNewAccP3ToggleConfirmPass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtCreateNewAccP3ConfirmPassword().apply {
                        transformationMethod = null
                        setSelection(getEtCreateNewAccP3ConfirmPassword().text.length)
                    }
                    confirmPasswordVisibility = 2
                }
                2 -> {
                    getIvCreateNewAccP3ToggleConfirmPass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtCreateNewAccP3ConfirmPassword().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtCreateNewAccP3ConfirmPassword().text.length)
                    }
                    confirmPasswordVisibility = 1
                }
            }
        }
    }
}