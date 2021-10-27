package com.example.sixkeeper

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class UserAccountEditFragment : UserAccountEditProcessClass() {
    private var currentPassVisibility: Int = 0
    private var newPassVisibility: Int = 0
    private var confirmPassVisibility: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setSpecifiedView()
        setEditOnClick()
        setCustomBackArrow()
    }

    private fun setSpecifiedView() {
        when {
            isFirstNameToUsername() -> {
                setView1()

                when (getViewId()) {
                    "first name" -> {
                        setFirstName()
                    }
                    "last name" -> {
                        setLastName()
                    }
                    "birth date" -> {
                        setBirthDate()
                    }
                    "email" -> {
                        setViewButton()
                        setEmail()
                    }
                    "mobile number" -> {
                        setViewButton()
                        setMobileNumber()
                    }
                    "username" -> {
                        setViewButton()
                        setUsername()
                    }
                }

                setInfoContent()
            }
            getViewId() == "password" -> {
                setView2()
                setEditTextOnChange()
                setImageViewOnClick()
            }
            getViewId() == "master pin" -> {
                setView3()
            }
        }
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        getEtUserEditCurrentPass().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val currentPass = getEtUserEditCurrentPass().text.toString()

                if (currentPass.isNotEmpty()) {
                    if (currentPassVisibility == 0) {
                        getIvUserEditCurrentTogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        currentPassVisibility = 1

                        getEtUserEditCurrentPass().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtUserEditCurrentPass().text.length)
                        }
                    }
                } else {
                    getIvUserEditCurrentTogglePass().setImageResource(0)
                    currentPassVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        getEtUserEditNewPass().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val newPass = getEtUserEditNewPass().text.toString()

                if (newPass.isNotEmpty()) {
                    if (newPassVisibility == 0) {
                        getIvUserEditNewTogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        newPassVisibility = 1

                        getEtUserEditNewPass().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtUserEditNewPass().text.length)
                        }
                    }
                } else {
                    getIvUserEditNewTogglePass().setImageResource(0)
                    newPassVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        getEtUserEditConfirmPass().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val confirmPass = getEtUserEditConfirmPass().text.toString()

                if (confirmPass.isNotEmpty()) {
                    if (confirmPassVisibility == 0) {
                        getIvUserEditConfirmTogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        confirmPassVisibility = 1

                        getEtUserEditConfirmPass().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtUserEditConfirmPass().text.length)
                        }
                    }
                } else {
                    getIvUserEditConfirmTogglePass().setImageResource(0)
                    confirmPassVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        getIvUserEditCurrentTogglePass().setOnClickListener {
            when (currentPassVisibility) {
                1 -> {                                                                              // Show password
                    getIvUserEditCurrentTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtUserEditCurrentPass().apply {
                        transformationMethod = null
                        setSelection(getEtUserEditCurrentPass().text.length)
                    }
                    currentPassVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    getIvUserEditCurrentTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtUserEditCurrentPass().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtUserEditCurrentPass().text.length)
                    }
                    currentPassVisibility = 1
                }
            }
        }

        getIvUserEditNewTogglePass().setOnClickListener {
            when (newPassVisibility) {
                1 -> {                                                                              // Show password
                    getIvUserEditNewTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtUserEditNewPass().apply {
                        transformationMethod = null
                        setSelection(getEtUserEditNewPass().text.length)
                    }
                    newPassVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    getIvUserEditNewTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtUserEditNewPass().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtUserEditNewPass().text.length)
                    }
                    newPassVisibility = 1
                }
            }
        }

        getIvUserEditConfirmTogglePass().setOnClickListener {
            when (confirmPassVisibility) {
                1 -> {                                                                              // Show confirm password
                    getIvUserEditConfirmTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtUserEditConfirmPass().apply {
                        transformationMethod = null
                        setSelection(getEtUserEditConfirmPass().text.length)
                    }
                    confirmPassVisibility = 2
                }
                2 -> {                                                                              // Hide confirm password
                    getIvUserEditConfirmTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtUserEditConfirmPass().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtUserEditConfirmPass().text.length)
                    }
                    confirmPassVisibility = 1
                }
            }
        }
    }

    private fun setCustomBackArrow() {
        val ivUserAccountEditBack: ImageView =
                getAppCompatActivity().findViewById(R.id.ivUserAccountEditBack)

        ivUserAccountEditBack.apply {
            postDelayed(
                    {
                        ivUserAccountEditBack.apply {
                            setBackgroundResource(R.color.blue)
                            setImageResource(R.drawable.ic_arrow_back_white)
                        }
                    }, 400
            )
        }

        ivUserAccountEditBack.setOnClickListener {
            getAppCompatActivity().onBackPressed()
        }
    }
}