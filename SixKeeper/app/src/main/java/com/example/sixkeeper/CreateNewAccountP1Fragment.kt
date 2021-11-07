package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast

class CreateNewAccountP1Fragment : CreateNewAccountP1ValidationClass() {

    private var lastLength = 0

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
        setBirthDateOnClick()
        setEditTextOnChange()
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

    private fun setBirthDateOnClick() {
        getEtCreateNewAccP1BirthDate().setOnClickListener {
            getEtCreateNewAccP1BirthDate().setSelection(
                    getEtCreateNewAccP1BirthDate().text.length
            )
        }
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        getEtCreateNewAccP1BirthDate().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setBirthDate(getEtCreateNewAccP1BirthDate().text.toString())

                if (getBirthDate().isNotEmpty()) {
                    if (
                            (lastLength == 0 && getBirthDate().length == 1) ||
                            (lastLength == 3 && getBirthDate().length == 4) ||
                            (lastLength == 6 && getBirthDate().length == 7) ||
                            (lastLength == 7 && getBirthDate().length == 8) ||
                            (lastLength == 8 && getBirthDate().length == 9) ||
                            (lastLength == 9 && getBirthDate().length == 10)
                    ) {
                        lastLength++
                    } else if (
                            (lastLength == 1 && getBirthDate().isEmpty()) ||
                            (lastLength == 4 && getBirthDate().length == 3) ||
                            (lastLength == 7 && getBirthDate().length == 6) ||
                            (lastLength == 8 && getBirthDate().length == 7) ||
                            (lastLength == 9 && getBirthDate().length == 8) ||
                            (lastLength == 10 && getBirthDate().length == 9)
                    ) {
                        lastLength--
                    } else if (
                            (lastLength == 1 && getBirthDate().length == 2) ||
                            (lastLength == 4 && getBirthDate().length == 5)
                    ) {
                        setEtCreateNewAccP1BirthDate(getBirthDate() + "/")
                        lastLength += 2
                    } else if (
                            (lastLength == 3 && getBirthDate().length == 2) ||
                            (lastLength == 6 && getBirthDate().length == 5)
                    ) {
                        setEtCreateNewAccP1BirthDate(
                                getBirthDate().substring(0, getBirthDate().length -1)
                        )
                        lastLength -= 2
                    }

                    getEtCreateNewAccP1BirthDate().setSelection(
                            getEtCreateNewAccP1BirthDate().text.length
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setButtonOnClick() {
        val acbCreateNewAccP1Next: Button =
                getAppCompatActivity().findViewById(R.id.acbCreateNewAccP1Next)

        acbCreateNewAccP1Next.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
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
                            immKeyboard.hideSoftInputFromWindow(                                    // Close keyboard
                                    getAppCompatActivity().currentFocus?.windowToken,
                                    0
                            )
                        }

                        createNewAccountActivity.apply {
                            manageCreateNewAccFragments(
                                    createNewAccountActivity.getCreateNewAccP3()
                            )
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
            } else {
                internetToast()
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                getAppCompatActivity().applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}