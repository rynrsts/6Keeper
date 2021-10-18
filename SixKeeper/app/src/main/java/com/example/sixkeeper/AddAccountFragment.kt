package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class AddAccountFragment : Fragment() {
    private val args: AddAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var encodedSpecificPlatformId: String

    private lateinit var etAddAccountName: EditText
    private lateinit var sAddAccountCredential1: Spinner
    private lateinit var etAddAccountCredential1: EditText
    private lateinit var etAddAccountPassword: EditText
    private lateinit var ivAddAccountTogglePass: ImageView
    private lateinit var etAddAccountWebsite: EditText
    private lateinit var etAddAccountDescription: EditText
    private lateinit var cbAddAccountFavorites: CheckBox

    private var accountName: String = ""
    private lateinit var name: String
    private lateinit var credentialField: String
    private lateinit var credential: String
    private lateinit var password: String
    private var passwordVisibility: Int = 0
    private lateinit var websiteURL: String
    private lateinit var description: String
    private lateinit var deleted: String
    private var isFavorites: Int = 0
    private var addOrSave = 0

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
        setEditTextOnChange()
        setImageViewOnClick()
        setOnClick()
        setOnTouchListener()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    //    TODO: Apply this on Action Bar back
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {                                                    // Override back press
                if (addOrSave == 1) {
                    val controller = Navigation.findNavController(view!!)
                    controller.popBackStack(R.id.addAccountFragment, true)
                } else {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    builder.setCancelable(false)

                    if (args.addOrEdit == "add") {
                        builder.setMessage(R.string.create_new_acc_alert_message)
                    } else if (args.addOrEdit == "edit") {
                        builder.setMessage(R.string.many_alert_discard_change)
                    }

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        val controller = Navigation.findNavController(view!!)
                        controller.popBackStack(R.id.addAccountFragment, true)
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()
                    }

                    val alert: AlertDialog = builder.create()
                    alert.setTitle(R.string.many_alert_title)
                    alert.show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()
        encodedSpecificPlatformId = encodingClass.encodeData(args.specificPlatformId)

        etAddAccountName = appCompatActivity.findViewById(R.id.etAddAccountName)
        sAddAccountCredential1 = appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        etAddAccountCredential1 = appCompatActivity.findViewById(R.id.etAddAccountCredential1)
        etAddAccountPassword = appCompatActivity.findViewById(R.id.etAddAccountPassword)
        ivAddAccountTogglePass = appCompatActivity.findViewById(R.id.ivAddAccountTogglePass)
        etAddAccountWebsite = appCompatActivity.findViewById(R.id.etAddAccountWebsite)
        etAddAccountDescription = appCompatActivity.findViewById(R.id.etAddAccountDescription)
        cbAddAccountFavorites = appCompatActivity.findViewById(R.id.cbAddAccountFavorites)

        deleted = encodingClass.encodeData(0.toString())
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

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        etAddAccountPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (etAddAccountPassword.text.toString().isNotEmpty()) {
                    if (passwordVisibility == 0) {
                        ivAddAccountTogglePass.setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        passwordVisibility = 1

                        etAddAccountPassword.apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(etAddAccountPassword.text.length)
                        }
                    }
                } else {
                    ivAddAccountTogglePass.setImageResource(0)
                    passwordVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        ivAddAccountTogglePass.setOnClickListener {
            when (passwordVisibility) {
                1 -> {                                                                              // Show password
                    ivAddAccountTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    etAddAccountPassword.apply {
                        transformationMethod = null
                        setSelection(etAddAccountPassword.text.length)
                    }
                    passwordVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    ivAddAccountTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    etAddAccountPassword.apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(etAddAccountPassword.text.length)
                    }
                    passwordVisibility = 1
                }
            }
        }
    }

    private fun populateAccountData() {                                                             // Set Account data to fields
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "platformId",
                encodedSpecificPlatformId,
                deleted
        )

        for (u in userAccount) {
            if (args.specificAccountId == encodingClass.decodeData(u.accountId)) {
                accountName = encodingClass.decodeData(u.accountName)
                etAddAccountName.apply {
                    setText(accountName)
                    setSelection(etAddAccountName.text.length)
                }
                sAddAccountCredential1.setSelection(
                        items.indexOf(encodingClass.decodeData(u.accountCredentialField))
                )
                etAddAccountCredential1.apply {
                    setText(encodingClass.decodeData(u.accountCredential))
                    setSelection(etAddAccountCredential1.text.length)
                }
                etAddAccountPassword.apply {
                    setText(encodingClass.decodeData(u.accountPassword))
                    setSelection(etAddAccountPassword.text.length)
                }
                etAddAccountWebsite.apply {
                    setText(encodingClass.decodeData(u.accountWebsiteURL))
                    setSelection(etAddAccountWebsite.text.length)
                }
                etAddAccountDescription.apply {
                    setText(encodingClass.decodeData(u.accountDescription))
                    setSelection(etAddAccountDescription.text.length)
                }

                if (encodingClass.decodeData(u.accountIsFavorites) == "1") {
                    cbAddAccountFavorites.isChecked = true
                }
            }
        }
    }

    private fun setOnClick() {
        val clAddAccountButton: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clAddAccountButton)

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

            it.apply {
                clAddAccountButton.isClickable = false                                              // Set un-clickable for 1 second
                postDelayed(
                        {
                            clAddAccountButton.isClickable = true
                        }, 1000
                )
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
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "platformId",
                encodedSpecificPlatformId,
                deleted

        )
        var accountId = 100001
        val lastId = databaseHandlerClass.getLastIdOfAccount()
        var existing = false
        var toast: Toast? = null

        if (lastId.isNotEmpty()) {
            accountId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
        }

        for (u in userAccount) {
            if (
                    name.equals(encodingClass.decodeData(u.accountName),ignoreCase = true) &&
                    !name.equals(accountName, ignoreCase = true)
            ) {
                existing = true
                break
            }
        }

        if (!existing) {
            if (args.addOrEdit == "add") {                                                          // Add Account
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
                                deleted,
                                "",
                                encodedSpecificPlatformId,
                                encodingClass.encodeData(args.specificPlatformName),
                                encodingClass.encodeData(args.specificCategoryName)
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Account '$name' added!",
                            Toast.LENGTH_SHORT
                    )
                }

                addOrSave = 1
                appCompatActivity.onBackPressed()
            } else if (args.addOrEdit == "edit") {                                                  // Update Account
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
                    R.string.add_account_new_account_alert_existing,
                    Toast.LENGTH_SHORT
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

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
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
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""
                            )
                    )

                    if (status > -1) {
                        val toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                "Account '$name' updated!",
                                Toast.LENGTH_SHORT
                        )
                        toast?.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                }

                postDelayed(
                        {
                            addOrSave = 1
                            appCompatActivity.onBackPressed()
                        }, 250
                )
            }
        }
    }
}