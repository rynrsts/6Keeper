package com.example.sixkeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
        setEditOnClick()
    }

    private fun setSpecifiedView() {
        if (isFirstNameToUsername()) {
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
        } else if (getViewId() == "password") {
            setView2()
        }
    }
}