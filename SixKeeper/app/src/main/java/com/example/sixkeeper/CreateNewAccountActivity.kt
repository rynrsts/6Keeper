package com.example.sixkeeper

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

class CreateNewAccountActivity : CreateNewAccountManageFragmentsClass() {
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
}