package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class UserAccountFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeKeyboard()
        setOnClick()
    }

    private fun closeKeyboard() {
        appCompatActivity = activity as AppCompatActivity

        val immKeyboard: InputMethodManager =
            appCompatActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                appCompatActivity.currentFocus?.windowToken,
                0
            )
        }
    }

    private fun setOnClick() {
        val clUserAccountFirstName: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountFirstName)
        val clUserAccountLastName: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountLastName)
        val clUserAccountBirthDate: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountBirthDate)
        val clUserAccountEmail: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEmail)
        val clUserAccountMobileNum: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountMobileNum)
        val clUserAccountUsername: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountUsername)

        clUserAccountFirstName.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("first name")
            findNavController().navigate(action)
        }

        clUserAccountLastName.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("last name")
            findNavController().navigate(action)
        }

        clUserAccountBirthDate.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("birth date")
            findNavController().navigate(action)
        }

        clUserAccountEmail.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("email")
            findNavController().navigate(action)
        }

        clUserAccountMobileNum.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("mobile number")
            findNavController().navigate(action)
        }

        clUserAccountUsername.setOnClickListener {
            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment("username")
            findNavController().navigate(action)
        }
    }
}