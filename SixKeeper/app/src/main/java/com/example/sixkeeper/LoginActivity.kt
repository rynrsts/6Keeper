package com.example.sixkeeper

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
                    validateUserCredential()
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
                acbLoginLogin.isClickable = false                                                   // Set button un-clickable for 1 second
                postDelayed(
                        {
                            acbLoginLogin.isClickable = true
                        }, 1000
                )
            }
        }

        tvLoginImportData.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(                                                 // Check if permission is granted
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (InternetConnectionClass().isConnected()) {
                    createFolder()

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("Make sure there is an exported file named " +
                            "'SixKeeperDatabase.skdb' in the 'SixKeeper' folder in the internal " +
                            "storage. Continue?")
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        if (InternetConnectionClass().isConnected()) {
                            @Suppress("DEPRECATION")
                            val sd = Environment.getExternalStorageDirectory()
                            val data = Environment.getDataDirectory()
                            val source: FileChannel?
                            val destination: FileChannel?
                            val packageName = this.packageName
                            val currentDBPath = "SixKeeper/SixKeeperDatabase.skdb"
                            val backupDBPath = "/data/$packageName/databases/SixKeeperDatabase"
                            val currentDB = File(sd, currentDBPath)
                            val backupDB = File(data, backupDBPath)

                            try {
                                source = FileInputStream(currentDB).channel
                                destination = FileOutputStream(backupDB).channel
                                destination.transferFrom(source, 0, source.size())                      // Save data to folder
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

                                setUserIdAfterImport()
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
            } else {
                ActivityCompat.requestPermissions(                                                  // Request permission
                        this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        52420
                )
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

                finish()
            } else {
                internetToast()
            }
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
            finishAffinity()
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