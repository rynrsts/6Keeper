package com.example.sixkeeper

import android.content.Context
import android.content.Intent
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

    private var field = ""

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
            field = "first name"
            openConfirmActionActivity()
        }

        clUserAccountLastName.setOnClickListener {
            field = "last name"
            openConfirmActionActivity()
        }

        clUserAccountBirthDate.setOnClickListener {
            field = "birth date"
            openConfirmActionActivity()
        }

        clUserAccountEmail.setOnClickListener {
            field = "email"
            openConfirmActionActivity()
        }

        clUserAccountMobileNum.setOnClickListener {
            field = "mobile number"
            openConfirmActionActivity()
        }

        clUserAccountUsername.setOnClickListener {
            field = "username"
            openConfirmActionActivity()
        }
    }

    private fun openConfirmActionActivity() {
        val goToConfirmActivity = Intent(
                appCompatActivity,
                ConfirmActionActivity::class.java
        )

        @Suppress("DEPRECATION")
        startActivityForResult(goToConfirmActivity, 16914)
        appCompatActivity.overridePendingTransition(
                R.anim.anim_enter_bottom_to_top_2,
                R.anim.anim_0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                view?.apply {
                    postDelayed(
                        {
                            val action = UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountEditFragment(field)
                            findNavController().navigate(action)
                        }, 250
                    )
                }
            }
        }
    }
}