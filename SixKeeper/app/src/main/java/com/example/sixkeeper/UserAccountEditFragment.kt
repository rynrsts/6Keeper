package com.example.sixkeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

class UserAccountEditFragment : UserAccountEditProcessClass() {

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
        setOnClick()
    }

    private fun setSpecifiedView() {
        if (
                getViewId() == "first name" ||
                getViewId() == "last name" ||
                getViewId() == "birth date" ||
                getViewId() == "email" ||
                getViewId() == "mobile number"
        ) {
            setView1()

            when (getViewId()) {
                "first name" ->
                    setFirstName()
                "last name" ->
                    setLastName()
                "birth date" ->
                    setBirthDate()
                "email" ->
                    setEmail()
                "mobile number" ->
                    setMobileNumber()
            }
        }
    }

    private fun setOnClick() {
        val clUserEditButton: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditButton)

        clUserEditButton.setOnClickListener {
            when (getViewId()) {
                "first name" ->
                    doFirstName()
            }
        }
    }
}