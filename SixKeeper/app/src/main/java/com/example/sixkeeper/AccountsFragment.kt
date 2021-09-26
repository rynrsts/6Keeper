package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class AccountsFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
        closeKeyboard()
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun closeKeyboard() {
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

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val ivAccountsAddCategories: ImageView =
                appCompatActivity.findViewById(R.id.ivAccountsAddCategories)

        ivAccountsAddCategories.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_add_new, null)

            builder.apply {
                setView(dialogView)
                setCancelable(false)
            }

            val tvAccountsAddNew: TextView = dialogView.findViewById(R.id.tvAccountsAddNew)
            val etAccountsAddNew: EditText = dialogView.findViewById(R.id.etAccountsAddNew)
            val ivAccountsAddNew: ImageView = dialogView.findViewById(R.id.ivAccountsAddNew)

            tvAccountsAddNew.setText(R.string.accounts_category_name)
            ivAccountsAddNew.setImageResource(R.drawable.ic_format_list_bulleted_light_black)

            builder.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                val addNewCategory = etAccountsAddNew.text.toString()

                if (addNewCategory.isNotEmpty()) {
                    addNewCategory(addNewCategory)
                    closeKeyboard()
                } else {
                    val toast: Toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.accounts_new_category_alert_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(ContextCompat.getDrawable(
                        appCompatActivity, R.drawable.layout_alert_dialog)
                )
                setTitle(R.string.accounts_new_category)
                show()
            }
        }
    }

    private fun addNewCategory(categoryName: String) {                                                  // Add new category
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val userCategory: List<UserCategoryModelClass> = databaseHandlerClass.viewCategory()

        val categoryId: Int = if (userCategory.isNullOrEmpty()) {
            10000
        } else {
            Integer.parseInt(userCategory.last().toString()) + 1
        }

        val status = databaseHandlerClass.addCategory(
                UserCategoryModelClass(
                        encodingClass.encodeData(categoryId.toString()),
                        encodingClass.encodeData(categoryName)
                )
        )

        if (status > -1) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    "Category '$categoryName' added!",
                    Toast.LENGTH_LONG
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }
}