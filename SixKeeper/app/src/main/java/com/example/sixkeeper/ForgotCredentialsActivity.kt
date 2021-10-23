package com.example.sixkeeper

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class ForgotCredentialsActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager

    private val securityQuestionFragment: Fragment = SecurityQuestionFragment()
    private val resetPasswordFragment: Fragment = ResetPasswordFragment()
    private val resetMasterPINFragment: Fragment = ResetMasterPINFragment()

    private var credential = ""
    private val securityQuestion = "securityQuestion"
    private val resetPassword = "resetPassword"
    private val resetMasterPIN = "resetMasterPIN"
    private var fragmentNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_credentials)

        getExtra()
        changeActionBarTitle()
        manageForgotFragments(securityQuestion)
        setOnClick()
    }

    private fun getExtra() {
        val extras = intent.extras

        if (extras != null) {
            credential = extras.getString("credential").toString()
        }
    }

    fun getCredential(): String {
        var reset = ""

        if (credential == "password") {
            reset = resetPassword
        } else if (credential == "master pin") {
            reset = resetMasterPIN
        }

        return reset
    }

    private fun changeActionBarTitle() {
        val tvActionBarTitle: TextView = findViewById(R.id.tvActionBarTitle)

        if (credential == "password") {
            tvActionBarTitle.setText(R.string.reset_password_title)
        } else if (credential == "master pin") {
            tvActionBarTitle.setText(R.string.reset_master_pin_title)
        }
    }

    fun manageForgotFragments(selectedFragment: String) {
        var forgot = ""

        if (credential == "password") {
            forgot = resetPassword
        } else if (credential == "master pin") {
            forgot = resetMasterPIN
        }

        when (selectedFragment) {
            securityQuestion -> {                                                                   // Security Question fragment
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 2) {
                        setCustomAnimations(
                                R.anim.anim_enter_left_to_right_1,
                                R.anim.anim_exit_left_to_right_1
                        )
                    }

                    replace(R.id.clForgotCredentialsContainer, securityQuestionFragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 1
            }
            forgot -> {                                                                             // Forgot fragment (Password / Master PIN)
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 1) {
                        setCustomAnimations(
                                R.anim.anim_enter_right_to_left_1,
                                R.anim.anim_exit_right_to_left_1
                        )
                    }

                    if (forgot == resetPassword) {
                        replace(R.id.clForgotCredentialsContainer, resetPasswordFragment)
                    } else if (forgot == resetMasterPIN) {
                        replace(R.id.clForgotCredentialsContainer, resetMasterPINFragment)
                    }

                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 2
            }
        }
    }

    private fun setOnClick() {
        val ivActionBarBackArrow: ImageView = findViewById(R.id.ivActionBarBackArrow)

        ivActionBarBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val immKeyboard: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)             // Close keyboard
        }

        when (fragmentNum) {
            1 -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.forgot_credentials_mes)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    super.onBackPressed()
                    overridePendingTransition(
                            R.anim.anim_enter_left_to_right_2,
                            R.anim.anim_exit_left_to_right_2
                    )
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title)
                alert.show()
            }
            2 -> {
                manageForgotFragments(securityQuestion)
            }
        }
    }
}