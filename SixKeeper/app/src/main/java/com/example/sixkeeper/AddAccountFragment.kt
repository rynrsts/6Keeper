package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class AddAccountFragment : Fragment() {
    private val args: AddAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etAddAccountName: EditText
    private lateinit var sAddAccountCredential1: Spinner
    private lateinit var etAddAccountCredential1: EditText
    private lateinit var etAddAccountPassword: EditText
    private lateinit var etAddAccountWebsite: EditText
    private lateinit var etAddAccountDescription: EditText
    private lateinit var cbAddAccountFavorites: CheckBox

    private lateinit var name: String
    private lateinit var credentialField: String
    private lateinit var credential: String
    private lateinit var password: String
    private lateinit var websiteURL: String
    private lateinit var description: String
    private var isFavorites: Int = 0

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

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etAddAccountName = appCompatActivity.findViewById(R.id.etAddAccountName)
        sAddAccountCredential1 = appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        etAddAccountCredential1 = appCompatActivity.findViewById(R.id.etAddAccountCredential1)
        etAddAccountPassword = appCompatActivity.findViewById(R.id.etAddAccountPassword)
        etAddAccountWebsite = appCompatActivity.findViewById(R.id.etAddAccountWebsite)
        etAddAccountDescription = appCompatActivity.findViewById(R.id.etAddAccountDescription)
        cbAddAccountFavorites = appCompatActivity.findViewById(R.id.cbAddAccountFavorites)
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
                credentialField = sAddAccountCredential1.selectedItem.toString()
                description = etAddAccountDescription.text.toString()

                if (cbAddAccountFavorites.isChecked) {
                    isFavorites = 1
                }

                if (addNewAccount()) {
                    appCompatActivity.onBackPressed()
                }
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
        name = etAddAccountName.text.toString()
        val credentialFieldPosition = sAddAccountCredential1.selectedItemPosition
        credential = etAddAccountCredential1.text.toString()
        password = etAddAccountPassword.text.toString()
        websiteURL = etAddAccountWebsite.text.toString()

        return name.isNotEmpty() &&
                credentialFieldPosition != 0 &&
                credential.isNotEmpty() &&
                password.isNotEmpty() &&
                websiteURL.isNotEmpty()
    }

    @SuppressLint("ShowToast")
    private fun addNewAccount(): Boolean {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val encodedArgs = encodingClass.encodeData(args.specificPlatformId)
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(encodedArgs)
        var accountId = 100001
        var existing = false
        var toast: Toast? = null
        var isAdded = false

        accountId += databaseHandlerClass.viewNumberOfAccounts("")

        for (u in userAccount) {
            if (
                    name.equals(
                            encodingClass.decodeData(u.accountName),
                            ignoreCase = true
                    )
            ) {
                existing = true
                break
            }
        }

        if (!existing) {
            val status = databaseHandlerClass.addAccount(
                    UserAccountModelClass(
                            encodingClass.encodeData(accountId.toString()),
                            encodingClass.encodeData(name),
                            encodingClass.encodeData(credentialField),
                            encodingClass.encodeData(credential),
                            encodingClass.encodeData(password),
                            encodingClass.encodeData(websiteURL),
                            encodingClass.encodeData(description),
                            encodingClass.encodeData(isFavorites.toString()),
                            encodedArgs
                    )
            )

            if (status > -1) {
                toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        "Account '$name' added!",
                        Toast.LENGTH_LONG
                )
            }

            isAdded = true
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.add_account_alert_existing,
                    Toast.LENGTH_LONG
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }

        return isAdded
    }
}