package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment


class AddAccountFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etAddAccountName: EditText
    private lateinit var sAddAccountCredential1: Spinner
    private lateinit var etAddAccountCredential1: EditText
    private lateinit var etAddAccountPassword: EditText
    private lateinit var etAddAccountWebsite: EditText
    private lateinit var etAddAccountDescription: EditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setSpinner()
        closeKeyboard()
        setOnClick()
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etAddAccountName = appCompatActivity.findViewById(R.id.etAddAccountName)
        sAddAccountCredential1 = appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        etAddAccountCredential1 = appCompatActivity.findViewById(R.id.etAddAccountCredential1)
        etAddAccountPassword = appCompatActivity.findViewById(R.id.etAddAccountPassword)
        etAddAccountWebsite = appCompatActivity.findViewById(R.id.etAddAccountWebsite)
        etAddAccountDescription = appCompatActivity.findViewById(R.id.etAddAccountDescription)
    }

    private fun setSpinner() {
        val sAddAccountCredential1: Spinner =
                appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        val items = arrayOf("-- Select Item --", "Email", "Mobile Number", "Username", "Other")
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                appCompatActivity,
                android.R.layout.simple_spinner_dropdown_item,
                items
        )

        sAddAccountCredential1.adapter = arrayAdapter
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

    private fun setOnClick() {
        val clAddAccountAdd: ConstraintLayout = appCompatActivity.findViewById(R.id.clAddAccountAdd)

        clAddAccountAdd.setOnClickListener {
            if (isNotEmpty()) {

            } else {
                val toast: Toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.many_fill_missing_fields,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }

    private fun isNotEmpty(): Boolean {                                                             // Validate fields not empty
        val name = etAddAccountName.text.toString()
        val credentialField = sAddAccountCredential1.selectedItemPosition
        val credential = etAddAccountCredential1.text.toString()
        val password = etAddAccountPassword.text.toString()
        val websiteURL = etAddAccountWebsite.text.toString()
        val description = etAddAccountDescription.text.toString()

        return name.isNotEmpty() &&
                credentialField != 0 &&
                credential.isNotEmpty() &&
                password.isNotEmpty() &&
                websiteURL.isNotEmpty() &&
                description.isNotEmpty()
    }
}