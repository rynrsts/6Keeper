package com.example.sixkeeper

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

class LoginActivity : LoginValidationClass() {
    private var passwordVisibility: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeStatusBarColor()
        setVariables()
        requestPermission()
        createFolder()
        blockCapture()
        setButtonOnClick()
        setEditTextOnChange()
        setImageViewOnClick()
    }

    private fun setButtonOnClick() {
        val tvLoginForgotPass: TextView = findViewById(R.id.tvLoginForgotPass)
        val acbLoginLogin: Button = findViewById(R.id.acbLoginLogin)
        val tvLoginImportData: TextView = findViewById(R.id.tvLoginImportData)
        val acbLoginCreateNewAccount: Button = findViewById(R.id.acbLoginCreateNewAccount)

        tvLoginForgotPass.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val goToForgotCredentialsActivity =
                        Intent(this, ForgotCredentialsActivity::class.java)
                goToForgotCredentialsActivity.putExtra("credential", "password")

                startActivity(goToForgotCredentialsActivity)
                overridePendingTransition(
                        R.anim.anim_enter_right_to_left_2,
                        R.anim.anim_exit_right_to_left_2
                )

                it.apply {
                    tvLoginForgotPass.isClickable = false                                               // Set button un-clickable for 1 second
                    postDelayed(
                            {
                                tvLoginForgotPass.isClickable = true
                            }, 1000
                    )
                }
            } else {
                internetToast()
            }
        }

        acbLoginLogin.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (isNotEmpty()) {
                    val waitingTime = waitingTime()

                    if (validateUserCredential()) {
                        if (waitingTime == 0.toLong()) {
                            restartAttemptAndTime()
                            updateUserStatus()

                            closeKeyboard()

                            val goToMasterPINActivity = Intent(
                                    this,
                                    MasterPINActivity::class.java
                            )
                            startActivity(goToMasterPINActivity)
                            overridePendingTransition(
                                    R.anim.anim_enter_bottom_to_top_2,
                                    R.anim.anim_0
                            )

                            this.finish()
                        } else {
                            lockToast(waitingTime)
                        }
                    } else if (validateUsername()) {
                        if (waitingTime == 0.toLong()) {
                            updateAccountStatus()
                        } else {
                            lockToast(waitingTime)
                        }

                        setEtLoginPassword("")
                        getEtLoginPassword().requestFocus()
                    } else {
                        val toast: Toast = Toast.makeText(
                                applicationContext,
                                R.string.login_invalid_credentials,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }

                        setEtLoginPassword("")
                        getEtLoginPassword().requestFocus()
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            applicationContext,
                            R.string.login_enter_credentials,
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

            it.apply {
                getEtLoginPassword().isClickable = false                                            // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getEtLoginPassword().isClickable = true
                        }, 1000
                )
            }
        }

        tvLoginImportData.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                createFolder()

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Make sure there is an exported file named 'SixKeeperDatabase' " +
                        "in the 'SixKeeper' folder in the internal storage. Continue?")
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    if (InternetConnectionClass().isConnected()) {
                        @Suppress("DEPRECATION")
                        val sd = Environment.getExternalStorageDirectory()
                        val data = Environment.getDataDirectory()
                        val source: FileChannel?
                        val destination: FileChannel?
                        val packageName = this.packageName
                        val currentDBPath = "SixKeeper/SixKeeperDatabase"
                        val backupDBPath = "/data/$packageName/databases/SixKeeperDatabase"
                        val currentDB = File(sd, currentDBPath)
                        val backupDB = File(data, backupDBPath)

                        try {
                            source = FileInputStream(currentDB).channel
                            destination = FileOutputStream(backupDB).channel
                            destination.transferFrom(source, 0, source.size())                              // Save data to folder
                            source.close()
                            destination.close()

                            val toast: Toast = Toast.makeText(
                                    this,
                                    "Data was imported! Please enter your credentials",
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        } catch (e: IOException) {
                            val toast: Toast = Toast.makeText(
                                    this,
                                    "Cannot find folder and/or file",
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
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.login_import_data_title)
                alert.show()
            } else {
                internetToast()
            }

            it.apply {
                tvLoginImportData.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvLoginImportData.isClickable = true
                        }, 1000
                )
            }
        }

        acbLoginCreateNewAccount.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val goToCreateNewAccountActivity =
                        Intent(this, CreateNewAccountActivity::class.java)

                startActivity(goToCreateNewAccountActivity)
                overridePendingTransition(
                        R.anim.anim_enter_right_to_left_2,
                        R.anim.anim_exit_right_to_left_2
                )

                it.apply {
                    acbLoginCreateNewAccount.isClickable = false                                        // Set button un-clickable for 1 second
                    postDelayed(
                            {
                                acbLoginCreateNewAccount.isClickable = true
                            }, 1000
                    )
                }
            } else {
                internetToast()
            }
        }
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)                   // Close keyboard
        }
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        getEtLoginPassword().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setPassword(getEtLoginPassword().text.toString())

                if (getPassword().isNotEmpty()) {
                    if (passwordVisibility == 0) {
                        getIvLoginTogglePass().setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        passwordVisibility = 1

                        getEtLoginPassword().apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(getEtLoginPassword().text.length)
                        }
                    }
                } else {
                    getIvLoginTogglePass().setImageResource(0)
                    passwordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        getIvLoginTogglePass().setOnClickListener {
            when (passwordVisibility) {
                1 -> {                                                                              // Show password
                    getIvLoginTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    getEtLoginPassword().apply {
                        transformationMethod = null
                        setSelection(getEtLoginPassword().text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    getIvLoginTogglePass().apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    getEtLoginPassword().apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(getEtLoginPassword().text.length)
                    }
                    passwordVisibility = 1
                }
            }
        }
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.many_exit_mes)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}