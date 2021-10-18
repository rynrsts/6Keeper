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
        val clUserAccountPassword: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountPassword)
        val clUserAccountMasterPIN: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountMasterPIN)

        clUserAccountFirstName.setOnClickListener {
            field = "first name"
            openConfirmActionActivity()

            it.apply {
                clUserAccountFirstName.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountFirstName.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountLastName.setOnClickListener {
            field = "last name"
            openConfirmActionActivity()

            it.apply {
                clUserAccountLastName.isClickable = false                                           // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountLastName.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountBirthDate.setOnClickListener {
            field = "birth date"
            openConfirmActionActivity()

            it.apply {
                clUserAccountBirthDate.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountBirthDate.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountEmail.setOnClickListener {
            field = "email"
            openConfirmActionActivity()

            it.apply {
                clUserAccountEmail.isClickable = false                                              // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountEmail.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountMobileNum.setOnClickListener {
            field = "mobile number"
            openConfirmActionActivity()

            it.apply {
                clUserAccountMobileNum.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountMobileNum.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountUsername.setOnClickListener {
            field = "username"
            openConfirmActionActivity()

            it.apply {
                clUserAccountUsername.isClickable = false                                           // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountUsername.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountPassword.setOnClickListener {
            field = "password"
            val action = UserAccountFragmentDirections
                    .actionUserAccountFragmentToUserAccountEditFragment(field)
            findNavController().navigate(action)
        }

        clUserAccountMasterPIN.setOnClickListener {
            field = "master pin"
            val action = UserAccountFragmentDirections
                    .actionUserAccountFragmentToUserAccountEditFragment(field)
            findNavController().navigate(action)
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
                            val action = UserAccountFragmentDirections
                                    .actionUserAccountFragmentToUserAccountEditFragment(field)
                            findNavController().navigate(action)
                        }, 250
                    )
                }
            }
        }
    }
}