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
//    private var typing: Boolean = false

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
//        setEditTextOnChange()
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

//    TODO: Add '/' automatically in the birth date
//    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
//        getEtCreateNewAccP1BirthDate().addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                setBirthDate(getEtCreateNewAccP1BirthDate().text.toString())
//
//                if (getBirthDate().isNotEmpty()) {
//                    if (getBirthDate().length == 1) {
//                        typing = true
//                    }
//
//                    if (getBirthDate().length == 2 && typing) {
//                        setEtCreateNewAccP1BirthDate(getBirthDate() + "/")
//                        getEtCreateNewAccP1BirthDate().setSelection(
//                                getEtCreateNewAccP1BirthDate().text.length
//                        )
//                        typing = false
//                    }
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//        })
//    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP1Next: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP1Next)

        acbCreateNewAccP1Next.setOnClickListener {
            if (isNotEmpty()) {
                validateFirstName()
                validateLastName()
                validateBirthDate()
                validateEmail()
                validateMobileNumber()

                if (isValid()) {
                    val immKeyboard: InputMethodManager =
                            getAppCompatActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager
                    val createNewAccountActivity: CreateNewAccountActivity =
                            activity as CreateNewAccountActivity

                    if (immKeyboard.isActive) {
                        immKeyboard.hideSoftInputFromWindow(                                        // Close keyboard
                                getAppCompatActivity().currentFocus?.windowToken,
                                0
                        )
                    }

                    createNewAccountActivity.apply {
                        manageCreateNewAccFragments(createNewAccountActivity.getCreateNewAccP3())
                        setCreateNewAccountP1Data(
                                getFirstName(),
                                getLastName(),
                                getBirthDate(),
                                getEmail(),
                                getMobileNumber().toLong()
                        )
                    }
                }
            } else {
                val toast: Toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        R.string.many_fill_missing_fields,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }
}