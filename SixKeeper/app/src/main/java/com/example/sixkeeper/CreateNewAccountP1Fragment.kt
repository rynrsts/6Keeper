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

class CreateNewAccountP1Fragment : CreateNewAccountP1ValidationClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_create_new_account_p1,
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
        getEtCreateNewAccP1FirstName().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setFirstName(getEtCreateNewAccP1FirstName().text.toString())

                    when {
                        !hasFocus && getFirstName().isNotEmpty() ->
                            validateFirstName()
                    }
                }

        getEtCreateNewAccP1LastName().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setLastName(getEtCreateNewAccP1LastName().text.toString())

                    when {
                        !hasFocus && getLastName().isNotEmpty() ->
                            validateLastName()
                    }
                }

        getEtCreateNewAccP1BirthDate().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setBirthDate(getEtCreateNewAccP1BirthDate().text.toString())

                    when {
                        !hasFocus && getBirthDate().isNotEmpty() ->
                            validateBirthDate()
                    }
                }

        getEtCreateNewAccP1Email().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setEmail(getEtCreateNewAccP1Email().text.toString())

                    when {
                        !hasFocus && getEmail().isNotEmpty() ->
                            validateEmail()
                    }
                }

        getEtCreateNewAccP1MobileNumber().onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    setMobileNumber(getEtCreateNewAccP1MobileNumber().text.toString())

                    when {
                        !hasFocus && getMobileNumber().isNotEmpty() ->
                            validateMobileNumber()
                    }
                }
    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP1Next: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP1Next)

        // TODO: Create New Account P1
        acbCreateNewAccP1Next.setOnClickListener {
//            if (isNotEmpty()) {
//                validateFirstName()
//                validateLastName()
//                validateBirthDate()
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