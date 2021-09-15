package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class CreateNewAccountActivity : CreateNewAccountManageFragmentsClass() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private lateinit var email: String
    private var mobileNumber: Long = 0
    private lateinit var username: String
    private lateinit var password: String
    private var masterPin: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        manageCreateNewAccFragments(getCreateNewAccP1())
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        val ivActionBarBackArrow: ImageView = findViewById(R.id.ivActionBarBackArrow)

        ivActionBarBackArrow.setOnClickListener {
            it.apply {
                ivActionBarBackArrow.isClickable = false                                            // Set button un-clickable for 1 second
                postDelayed(
                        {
                            ivActionBarBackArrow.isClickable = true
                        }, 1000
                )
            }

            onBackPressed()
        }
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val immKeyboard: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)                   // Close keyboard
        }

        when {
            getFragmentNum() == 1 -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.create_new_acc_alert_message)
                builder.setCancelable(false)

                builder.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                    super.onBackPressed()
                    overridePendingTransition(
                            R.anim.anim_enter_left_to_right_2,
                            R.anim.anim_exit_left_to_right_2
                    )
                }
                builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title)
                alert.show()
            }

//            TODO: Second Fragment
//            getFragmentNum() == 2 -> {
//                manageCreateNewAccFragments(getCreateNewAccP1())
//            }
            getFragmentNum() == 3 -> {
                manageCreateNewAccFragments(getCreateNewAccP1())
            }
            getFragmentNum() == 4 -> {
                manageCreateNewAccFragments(getCreateNewAccP3())
            }
        }
    }

    internal fun setCreateNewAccountP1Data(f: String, l: String, b: String, e: String, m: Long) {   // CreateNewAccountP1 data
        firstName = f
        lastName = l
        birthDate = b
        email = e
        mobileNumber = m
    }

    internal fun setCreateNewAccountP3Data(u: String, p: String) {                                  // CreateNewAccountP3 data
        username = u
        password = p
    }

    internal fun setCreateNewAccountP4Data(mp: Int) {                                               // CreateNewAccountP4 data
        masterPin = mp
    }

    @SuppressLint("SimpleDateFormat")
    internal fun saveAccount() {                                                                    // Save account data to database
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userId: Int = (1000000..9999999).random()

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        val tableStatus = databaseHandlerClass.truncateAllTables()
        val userInfoStatus = databaseHandlerClass.addUserInfo(
                UserInfoModelClass(
                        encodeData(userId.toString()),
                        encodeData(firstName),
                        encodeData(lastName),
                        encodeData(birthDate),
                        encodeData(email),
                        encodeData(mobileNumber.toString()),
                        ""
                )
        )
        val userAccStatus = databaseHandlerClass.addUserAcc(
                UserAccModelClass(
                        encodeData(userId.toString()),
                        encodeData(username),
                        encodeData(password),
                        encodeData(masterPin.toString()),
                        encodeData(0.toString()),
                        encodeData(date),
                        ""
                )
        )

        if (tableStatus > -1 && userInfoStatus > -1 && userAccStatus > -1) {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.create_new_acc_success, Toast.LENGTH_LONG
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    private fun encodeData(data: String): String {                                                  // Encode data using Base64
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)
        return String(encrypt)
    }
}