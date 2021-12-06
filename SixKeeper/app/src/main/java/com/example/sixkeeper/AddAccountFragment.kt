package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*

class AddAccountFragment : Fragment() {
    private val args: AddAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var encryptedSpecificPlatformId: String

    private lateinit var etAddAccountName: EditText
    private lateinit var sAddAccountCredential1: Spinner
    private lateinit var etAddAccountCredential1: EditText
    private lateinit var etAddAccountPassword: EditText
    private lateinit var ivAddAccountTogglePass: ImageView
    private lateinit var etAddAccountWebsite: EditText
    private lateinit var tvAddAccountAppSelection: TextView
    private lateinit var etAddAccountDescription: EditText
    private lateinit var cbAddAccountFavorites: CheckBox

    private lateinit var inflatedViewIcon: View
    private lateinit var ivAddAccountRemove: ImageView
    private lateinit var llAddAccountApplication: LinearLayout

    private lateinit var userId: String
    private var accountName: String = ""
    private lateinit var name: String
    private lateinit var credentialField: String
    private lateinit var credential: String
    private lateinit var password: String
    private var passwordVisibility: Int = 0
    private lateinit var websiteURL: String
    private lateinit var applicationName: String
    private var packageName = ""
    private lateinit var description: String
    private lateinit var deleted: String
    private var isFavorites: Int = 0
    private var addOrSave = 0
    private var nameTemp = ""
    private var passwordTemp = ""
    private var encryptedDateTemp = ""

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

    @SuppressLint("InflateParams")
    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()

        etAddAccountName = appCompatActivity.findViewById(R.id.etAddAccountName)
        sAddAccountCredential1 = appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        etAddAccountCredential1 = appCompatActivity.findViewById(R.id.etAddAccountCredential1)
        etAddAccountPassword = appCompatActivity.findViewById(R.id.etAddAccountPassword)
        ivAddAccountTogglePass = appCompatActivity.findViewById(R.id.ivAddAccountTogglePass)
        tvAddAccountAppSelection = appCompatActivity.findViewById(R.id.tvAddAccountAppSelection)
        etAddAccountWebsite = appCompatActivity.findViewById(R.id.etAddAccountWebsite)
        etAddAccountDescription = appCompatActivity.findViewById(R.id.etAddAccountDescription)
        cbAddAccountFavorites = appCompatActivity.findViewById(R.id.cbAddAccountFavorites)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        inflatedViewIcon = layoutInflater.inflate(
                R.layout.layout_add_account_remove, null, true
        )
        ivAddAccountRemove = inflatedViewIcon.findViewById(R.id.ivAddAccountRemove)
        llAddAccountApplication = appCompatActivity.findViewById(R.id.llAddAccountApplication)

        encryptedSpecificPlatformId = encryptionClass.encrypt(args.specificPlatformId, userId)
        deleted = encryptionClass.encrypt(0.toString(), userId)
    }

    private fun setSpinner() {
        val sAddAccountCredential1: Spinner =
                appCompatActivity.findViewById(R.id.sAddAccountCredential1)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                appCompatActivity, android.R.layout.simple_spinner_dropdown_item, items
        )

        sAddAccountCredential1.adapter = arrayAdapter
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    appCompatActivity.currentFocus?.windowToken, 0
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
        val ivAddAccountBack: ImageView = appCompatActivity.findViewById(R.id.ivAddAccountBack)

        ivAddAccountBack.apply {
            postDelayed({
                ivAddAccountBack.apply {
                    setBackgroundResource(R.color.blue)
                    setImageResource(R.drawable.ic_arrow_back_white)
                }
            }, 400)
        }

        ivAddAccountBack.setOnClickListener {
            appCompatActivity.onBackPressed()
        }

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
                encryptedSpecificPlatformId,
                deleted
        )

        for (u in userAccount) {
            if (args.specificAccountId == encryptionClass.decrypt(u.accountId, userId)) {
                accountName = encryptionClass.decrypt(u.accountName, userId)
                etAddAccountName.apply {
                    setText(accountName)
                    setSelection(etAddAccountName.text.length)
                }
                sAddAccountCredential1.setSelection(
                        items.indexOf(encryptionClass.decrypt(u.accountCredentialField, userId))
                )
                etAddAccountCredential1.apply {
                    setText(encryptionClass.decrypt(u.accountCredential, userId))
                    setSelection(etAddAccountCredential1.text.length)
                }
                nameTemp = encryptionClass.decrypt(u.accountName, userId)
                passwordTemp = encryptionClass.decrypt(u.accountPassword, userId)
                etAddAccountPassword.apply {
                    setText(passwordTemp)
                    setSelection(etAddAccountPassword.text.length)
                }
                etAddAccountWebsite.apply {
                    setText(encryptionClass.decrypt(u.accountWebsiteURL, userId))
                    setSelection(etAddAccountWebsite.text.length)
                }
                val appNameTemp = encryptionClass.decrypt(u.accountApplicationName, userId)
                tvAddAccountAppSelection.text = appNameTemp
                packageName = encryptionClass.decrypt(u.accountPackageName, userId)
                etAddAccountDescription.apply {
                    setText(encryptionClass.decrypt(u.accountDescription, userId))
                    setSelection(etAddAccountDescription.text.length)
                }
                encryptedDateTemp = u.creationDate

                if (appNameTemp.isNotEmpty()) {
                    llAddAccountApplication.addView(inflatedViewIcon)
                }

                if (encryptionClass.decrypt(u.accountIsFavorites, userId) == "1") {
                    cbAddAccountFavorites.isChecked = true
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val clAddAccountButton: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clAddAccountButton)

        tvAddAccountAppSelection.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_add_account_list_view, null)

            builder.apply {
                setView(dialogView)
                setCancelable(true)
            }

            val tvAddAccountInclude: TextView = dialogView.findViewById(R.id.tvAddAccountInclude)
            val lvAddAccountInstalledApps: ListView =
                    dialogView.findViewById(R.id.lvAddAccountInstalledApps)
            var include = false

            lvAddAccountInstalledApps.adapter = addAccountListAdapter(include)

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(appCompatActivity, R.drawable.layout_alert_dialog)
                )
                setTitle(R.string.add_account_select_app)
                show()
            }

            closeKeyboard()

            tvAddAccountInclude.setOnClickListener {
                if (include) {
                    tvAddAccountInclude.setText(R.string.add_account_include_system_apps)
                } else {
                    tvAddAccountInclude.setText(R.string.add_account_exclude_system_apps)
                }

                include = !include
                lvAddAccountInstalledApps.adapter = addAccountListAdapter(include)
            }

            lvAddAccountInstalledApps.onItemClickListener = (AdapterView.OnItemClickListener { _, _, i, _ ->
                val selectedItem = lvAddAccountInstalledApps.getItemAtPosition(i).toString()
                val selectedValue = selectedItem.split("ramjcammjar")
                val selectedAppName = selectedValue[0]
                val selectedPackageName = selectedValue[1]

                tvAddAccountAppSelection.text = selectedAppName
                packageName = selectedPackageName
                llAddAccountApplication.apply {
                    removeAllViews()
                    addView(inflatedViewIcon)
                }
                alert.cancel()
            })
        }

        ivAddAccountRemove.setOnClickListener {
            tvAddAccountAppSelection.text = ""
            llAddAccountApplication.removeAllViews()
            packageName = ""
        }

        clAddAccountButton.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (isNotEmpty()) {
                    credentialField = sAddAccountCredential1.selectedItem.toString()
                    applicationName = tvAddAccountAppSelection.text.toString()
                    websiteURL = etAddAccountWebsite.text.toString()
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
            } else {
                internetToast()
            }

            it.apply {
                clAddAccountButton.isClickable = false                                              // Set un-clickable for 1 second
                postDelayed({ clAddAccountButton.isClickable = true }, 1000)
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun addAccountListAdapter(include: Boolean): AddAccountAppListAdapter {
        val packageList = appCompatActivity.packageManager.getInstalledPackages(0)
        val packageNameTemp = ArrayList<String>(0)
        val packageListTemp = ArrayList<PackageInfo>(0)

        for (list in packageList.indices) {
            val packageInfo = packageList[list]

            if (include) {
                val appName = packageInfo.applicationInfo.loadLabel(
                        appCompatActivity.packageManager
                ).toString()
                val packageName = packageInfo.packageName

                packageListTemp.add(packageInfo)
                packageNameTemp.add(appName + "ramjcammjar" + packageName)
            } else {
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    val appName = packageInfo.applicationInfo.loadLabel(
                            appCompatActivity.packageManager
                    ).toString()
                    val packageName = packageInfo.packageName

                    packageListTemp.add(packageInfo)
                    packageNameTemp.add(appName + "ramjcammjar" + packageName)
                }
            }
        }

        for (i in 0 until packageNameTemp.size) {
            for (j in packageNameTemp.size - 1 downTo i + 1) {
                if (packageNameTemp[i] > packageNameTemp[j]) {
                    val nameTemp: String = packageNameTemp[i]
                    val listTemp = packageListTemp[i]

                    packageNameTemp[i] = packageNameTemp[j]
                    packageNameTemp[j] = nameTemp

                    packageListTemp[i] = packageListTemp[j]
                    packageListTemp[j] = listTemp
                }
            }
        }

        return AddAccountAppListAdapter(
                attActivity,
                packageNameTemp,
                packageListTemp
        )
    }

    private fun isNotEmpty(): Boolean {                                                             // Validate fields not empty
        name = etAddAccountName.text.toString()
        val credentialFieldPosition = sAddAccountCredential1.selectedItemPosition
        credential = etAddAccountCredential1.text.toString()
        password = etAddAccountPassword.text.toString()

        return name.isNotEmpty() && credentialFieldPosition != 0 && credential.isNotEmpty() &&
                password.isNotEmpty()
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
                "platformId", encryptedSpecificPlatformId, deleted
        )
        var accountId = 100001
        val lastId = databaseHandlerClass.getLastIdOfAccount()
        var existing = false
        var toast: Toast? = null

        if (lastId.isNotEmpty()) {
            accountId = Integer.parseInt(encryptionClass.decrypt(lastId, userId)) + 1
        }

        for (u in userAccount) {
            if (
                    name.equals(encryptionClass.decrypt(u.accountName, userId), ignoreCase = true) &&
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
                                encryptionClass.encrypt(accountId.toString(), userId),
                                encryptionClass.encrypt(name, userId),
                                encryptionClass.encrypt(credentialField, userId),
                                encryptionClass.encrypt(credential, userId),
                                encryptionClass.encrypt(password, userId),
                                encryptionClass.encrypt(websiteURL, userId),
                                encryptionClass.encrypt(applicationName, userId),
                                encryptionClass.encrypt(packageName, userId),
                                encryptionClass.encrypt(description, userId),
                                encryptionClass.encrypt(isFavorites.toString(), userId),
                                deleted,
                                "",
                                encryptionClass.encrypt(getCurrentDate(), userId),
                                encryptionClass.encrypt(checkPasswordStatus(password), userId),
                                encryptedSpecificPlatformId,
                                encryptionClass.encrypt(args.specificPlatformName, userId),
                                encryptionClass.encrypt(args.specificCategoryName, userId)
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Account '$name' added!",
                            Toast.LENGTH_SHORT
                    )
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encryptionClass.encrypt(
                                        getLastActionLogId().toString(), userId),
                                encryptionClass.encrypt("Account '$name' was added to " +
                                        "'${args.specificCategoryName} > " +
                                        "${args.specificPlatformName}'.", userId),
                                encryptionClass.encrypt(getCurrentDate(), userId)
                        )
                )

                addOrSave = 1
                appCompatActivity.onBackPressed()
            } else if (args.addOrEdit == "edit") {                                                  // Update Account
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                builder.setMessage(R.string.user_edit_save_alert_mes)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    val goToConfirmActivity = Intent(
                            appCompatActivity, ConfirmActionActivity::class.java
                    )

                    @Suppress("DEPRECATION")
                    startActivityForResult(goToConfirmActivity, 16914)
                    appCompatActivity.overridePendingTransition(
                            R.anim.anim_enter_bottom_to_top_2, R.anim.anim_0
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

    private fun checkPasswordStatus(password: String): String {                                     // Check if password is weak, medium, or strong
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val number = "0123456789"
        val specialChar = "!@#$%^&*()=+._-"
        val boolChar = booleanArrayOf(false, false, false, false)
        var status = ""

        for (i in password.indices) {
            when {
                lowercase.contains(password[i]) -> {
                    boolChar[0] = true
                }
                uppercase.contains(password[i]) -> {
                    boolChar[1] = true
                }
                number.contains(password[i]) -> {
                    boolChar[2] = true
                }
                specialChar.contains(password[i]) -> {
                    boolChar[3] = true
                }
            }

            if (boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]) {
                break
            }
        }

        if (
                (boolChar[0] && !boolChar[1] && !boolChar[2] && !boolChar[3]) ||                    // Lower
                (!boolChar[0] && boolChar[1] && !boolChar[2] && !boolChar[3]) ||                    // Upper
                (!boolChar[0] && !boolChar[1] && boolChar[2] && !boolChar[3]) ||                    // Number
                (!boolChar[0] && !boolChar[1] && !boolChar[2] && boolChar[3]) ||                    // Special
                (boolChar[0] && boolChar[1] && !boolChar[2] && !boolChar[3]
                        && password.length <= 9) ||                                                 // Lower, Upper, length <= 9
                (boolChar[0] && !boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length <= 10) ||                                                // Lower, Number, length <= 10
                (boolChar[0] && !boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length <= 10) ||                                                // Lower, Special, length <= 10
                (!boolChar[0] && boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length <= 10) ||                                                // Upper, Number, length <= 10
                (!boolChar[0] && boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length <= 10) ||                                                // Upper, Special, length <= 10
                (!boolChar[0] && !boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length <= 11) ||                                                // Number, Special, length <= 11
                (boolChar[0] && boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length <= 7) ||                                                 // Lower, Upper, Number, length <= 7
                (boolChar[0] && boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length <= 7) ||                                                 // Lower, Upper, Special, length <= 7
                (boolChar[0] && !boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length <= 8) ||                                                 // Lower, Number, Special, length <= 8
                (!boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length <= 8) ||                                                 // Upper, Number, Special, length <= 8
                (boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length <= 6)                                                    // Lower, Upper, Number, Special, length <= 6
        ) {
            status = "weak"
        } else if (
                (boolChar[0] && boolChar[1] && !boolChar[2] && !boolChar[3]
                        && password.length >= 10) ||                                                // Lower, Upper, length >= 10
                (boolChar[0] && !boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length >= 11) ||                                                // Lower, Number, length >= 11
                (boolChar[0] && !boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length >= 11) ||                                                // Lower, Special, length >= 11
                (!boolChar[0] && boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length >= 11) ||                                                // Upper, Number, length >= 11
                (!boolChar[0] && boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length >= 11) ||                                                // Upper, Special, length >= 11
                (!boolChar[0] && !boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length >= 12) ||                                                // Number, Special, length >= 12
                (boolChar[0] && boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length in 8..11) ||                                             // Lower, Upper, Number, 8 - 11
                (boolChar[0] && boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length in 8..11) ||                                             // Lower, Upper, Special, 8 - 11
                (boolChar[0] && !boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length in 9..11) ||                                             // Lower, Number, Special, 9 - 11
                (!boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length in 9..11) ||                                             // Upper, Number, Special, 9 - 11
                (boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length in 7..10)                                                // Lower, Upper, Number, Special, 7 - 10
        ) {
            status = "medium"
        } else if (
                (boolChar[0] && boolChar[1] && boolChar[2] && !boolChar[3]
                        && password.length >= 12) ||                                                // Lower, Upper, Number, length >= 12
                (boolChar[0] && boolChar[1] && !boolChar[2] && boolChar[3]
                        && password.length >= 12) ||                                                // Lower, Upper, Special, length >= 12
                (boolChar[0] && !boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length >= 12) ||                                                // Lower, Number, Special, length >= 12
                (!boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length >= 12) ||                                                // Upper, Number, Special, length >= 12
                (boolChar[0] && boolChar[1] && boolChar[2] && boolChar[3]
                        && password.length >= 11)                                                   // Lower, Upper, Number, Special, length >= 11
        ) {
            status = "strong"
        }

        return status
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, userId)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
            view?.apply {
                if (args.addOrEdit == "edit") {
                    val encryptedDatePlaceholder = if (password != passwordTemp) {
                        encryptionClass.encrypt(getCurrentDate(), userId)
                    } else {
                        encryptedDateTemp
                    }

                    val actionMessage = if (name != nameTemp) {
                        "Account '$nameTemp' was modified to '$name'."
                    } else {
                        "Account '$name' was modified."
                    }

                    val status = databaseHandlerClass.updateAccount(
                            UserAccountModelClass(
                                    encryptionClass.encrypt(args.specificAccountId, userId),
                                    encryptionClass.encrypt(name, userId),
                                    encryptionClass.encrypt(credentialField, userId),
                                    encryptionClass.encrypt(credential, userId),
                                    encryptionClass.encrypt(password, userId),
                                    encryptionClass.encrypt(websiteURL, userId),
                                    encryptionClass.encrypt(applicationName, userId),
                                    encryptionClass.encrypt(packageName, userId),
                                    encryptionClass.encrypt(description, userId),
                                    encryptionClass.encrypt(isFavorites.toString(), userId),
                                    "",
                                    "",
                                    encryptedDatePlaceholder,
                                    encryptionClass.encrypt(checkPasswordStatus(password), userId),
                                    "",
                                    "",
                                    ""
                            )
                    )

                    if (status > -1) {
                        val toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                "Account '$name' modified!",
                                Toast.LENGTH_SHORT
                        )
                        toast?.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encryptionClass.encrypt(getLastActionLogId().toString(), userId),
                                    encryptionClass.encrypt(actionMessage, userId),
                                    encryptionClass.encrypt(getCurrentDate(), userId)
                            )
                    )
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