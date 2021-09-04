package com.example.sixkeeper

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class CreateNewAccountActivity : CreateNewAccountManageFragmentsClass() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private lateinit var email: String
    private var mobileNumber: Int = 0
    private lateinit var username: String
    private lateinit var password: String
    private var masterPin: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        manageCreateNewAccFragments(getCreateNewAccP1())
        buttonOnClick()
    }

    private fun buttonOnClick() {
        val ivActionBarBackArrow: ImageView = findViewById(R.id.ivActionBarBackArrow)

        ivActionBarBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {                                                                  // Override back button
        val immKeyboard: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        when {
            getFragmentNum() == 1 -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.many_alert_message)
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

            // TODO:
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

    // CreateNewAccountP1 Data
    internal fun setCreateNewAccountP1Data(f: String, l: String, b: String, e: String, m: Int) {
        firstName = f
        lastName = l
        birthDate = b
        email = e
        mobileNumber = m
    }

    // CreateNewAccountP3 Data
    internal fun setCreateNewAccountP3Data(u: String, p: String) {
        username = u
        password = p
    }

    // CreateNewAccountP4 Data
    internal fun setCreateNewAccountP4Data(mp: Int) {
        masterPin = mp
    }

    // Save data to database
    internal fun saveToDatabase() {
        val userId: Int = (1000..9999).random()
        val databaseHandler: DatabaseHandlerClass = DatabaseHandlerClass(this)

        val userInfoStatus = databaseHandler.addUserInfo(
                UserInfoModelClass(userId, firstName, lastName, birthDate, email, mobileNumber)
        )
//        val userAccStatus = databaseHandler.addUserAcc(
//                UserAccModelClass(userId, username, password, masterPin)
//        )

//        if (userInfoStatus > -1 && userAccStatus > -1) {
        if (userInfoStatus > -1) {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.create_new_acc_success, Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}