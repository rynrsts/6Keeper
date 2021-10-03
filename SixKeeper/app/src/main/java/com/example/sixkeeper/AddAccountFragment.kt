package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class AddAccountFragment : Fragment() {
    private val args: AddAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etAddAccountName: EditText
    private lateinit var sAddAccountCredential1: Spinner
    private lateinit var etAddAccountCredential1: EditText
    private lateinit var etAddAccountPassword: EditText
    private lateinit var etAddAccountWebsite: EditText
    private lateinit var etAddAccountDescription: EditText
    private lateinit var cbAddAccountFavorites: CheckBox

    private var accountName: String = ""
    private lateinit var name: String
    private lateinit var credentialField: String
    private lateinit var credential: String
    private lateinit var password: String
    private lateinit var websiteURL: String
    private lateinit var description: String
    private var isFavorites: Int = 0

    private val items = arrayOf("-- Select Item --", "Email", "Mobile Number", "Username", "Other")

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
        setButton()
        setOnClick()
        setOnTouchListener()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

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

    private fun setButton() {
        val ivAddAccountButton: ImageView = appCompatActivity.findViewById(R.id.ivAddAccountButton)
        val tvAddAccountButton: TextView = appCompatActivity.findViewById(R.id.tvAddAccountButton)

        if (args.addOrEdit == "add") {
            ivAddAccountButton.setImageResource(R.drawable.ic_add_white)
            tvAddAccountButton.setText(R.string.add_account_add)
        } else if (args.addOrEdit == "edit") {
            val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
            tAppBarToolbar.title = "Edit Account"

            populateAccountData()

            ivAddAccountButton.setImageResource(R.drawable.ic_save_white)
            tvAddAccountButton.setText(R.string.many_save)
        }
    }

    private fun setOnClick() {
        val clAddAccountButton: ConstraintLayout = appCompatActivity.findViewById(R.id.clAddAccountButton)

        clAddAccountButton.setOnClickListener {
            if (isNotEmpty()) {
                credentialField = sAddAccountCredential1.selectedItem.toString()
                description = etAddAccountDescription.text.toString()

                if (cbAddAccountFavorites.isChecked) {
                    isFavorites = 1
                }

                addOrEditAccount()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener() {
        etAddAccountDescription.movementMethod = ScrollingMovementMethod.getInstance()

        etAddAccountDescription.setOnTouchListener { view, motionEvent ->                           // Make Description field scrollable
            view.parent.requestDisallowInterceptTouchEvent(true)

            if (
                    motionEvent.action and MotionEvent.ACTION_UP != 0 &&
                    motionEvent.actionMasked and MotionEvent.ACTION_UP != 0
            ) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    @SuppressLint("ShowToast")
    private fun addOrEditAccount() {
        val encodedArgs = encodingClass.encodeData(args.specificPlatformId)
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(encodedArgs)
        var accountId = 100001
        var existing = false
        var toast: Toast? = null

        accountId += databaseHandlerClass.viewNumberOfAccounts("")

        for (u in userAccount) {
            if (
                    name.equals(encodingClass.decodeData(u.accountName), ignoreCase = true) &&
                    !name.equals(accountName, ignoreCase = true)
            ) {
                existing = true
                break
            }
        }

        if (!existing) {
            if (args.addOrEdit == "add") {
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

                appCompatActivity.onBackPressed()
            } else if (args.addOrEdit == "edit") {
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                builder.setMessage(R.string.user_edit_save_alert_mes)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
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
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title_confirm)
                alert.show()
            }
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 16914 && resultCode == 16914) {                                        // If Master PIN is correct
            view?.apply {
                if (args.addOrEdit == "edit") {
                    val status = databaseHandlerClass.updateAccount(
                            UserAccountModelClass(
                                    encodingClass.encodeData(args.specificAccountId),
                                    encodingClass.encodeData(name),
                                    encodingClass.encodeData(credentialField),
                                    encodingClass.encodeData(credential),
                                    encodingClass.encodeData(password),
                                    encodingClass.encodeData(websiteURL),
                                    encodingClass.encodeData(description),
                                    encodingClass.encodeData(isFavorites.toString()),
                                    ""
                            )
                    )

                    if (status > -1) {
                        val toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                "Account '$name' updated!",
                                Toast.LENGTH_LONG
                        )
                        toast?.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                }

                postDelayed(
                        {
                            appCompatActivity.onBackPressed()
                        }, 250
                )
            }
        }
    }

    private fun populateAccountData() {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val userAccount: List<UserAccountModelClass> =
                databaseHandlerClass.viewAccount(encodingClass.encodeData(args.specificPlatformId))

        for (u in userAccount) {
            if (args.specificAccountId == encodingClass.decodeData(u.accountId)) {
                accountName = encodingClass.decodeData(u.accountName)
                etAddAccountName.setText(accountName)
                etAddAccountCredential1.setText(encodingClass.decodeData(u.accountCredential))
                etAddAccountPassword.setText(encodingClass.decodeData(u.accountPassword))
                etAddAccountWebsite.setText(encodingClass.decodeData(u.accountWebsiteURL))
                etAddAccountDescription.setText(encodingClass.decodeData(u.accountDescription))

                sAddAccountCredential1.setSelection(
                        items.indexOf(encodingClass.decodeData(u.accountCredentialField))
                )

                if (encodingClass.decodeData(u.accountIsFavorites) == "1") {
                    cbAddAccountFavorites.isChecked = true
                }
            }
        }
    }
}