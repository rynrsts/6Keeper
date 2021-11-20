package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*

class SpecificAccountFragment : Fragment() {
    private val args: SpecificAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var tvSpecificAccName: TextView
    private lateinit var tvSpecificAccCredentialField: TextView
    private lateinit var tvSpecificAccCredential: TextView
    private lateinit var etSpecificAccPassword: EditText
    private lateinit var tvSpecificAccWebsiteURL: TextView

    private lateinit var key: ByteArray
    private var websiteUrl = ""
    private var applicationName = ""
    private var packageName = ""
    private var passwordVisibility: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setActionBarTitle()
        closeKeyboard()
        populateAccountData()
        setImageViewOnClick()
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()

        tvSpecificAccName = appCompatActivity.findViewById(R.id.tvSpecificAccName)
        tvSpecificAccCredentialField =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredentialField)
        tvSpecificAccCredential = appCompatActivity.findViewById(R.id.tvSpecificAccCredential)
        etSpecificAccPassword = appCompatActivity.findViewById(R.id.etSpecificAccPassword)
        tvSpecificAccWebsiteURL = appCompatActivity.findViewById(R.id.tvSpecificAccWebsiteURL)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
    }

    private fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = args.specificAccountName
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

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun populateAccountData() {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "platformId",
                encryptionClass.encrypt(args.specificPlatformId, key),
                encryptionClass.encrypt(0.toString(), key)
        )
        var platformName = ""
        var categoryName = ""

        val tvSpecificAccCategoryPlatform: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCategoryPlatform)
        val tvSpecificAccAppName: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccAppName)
        val tvSpecificAccDescription: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccDescription)
        val llSpecificAccFavorites: LinearLayout =
                appCompatActivity.findViewById(R.id.llSpecificAccFavorites)

        for (u in userAccount) {
            if (args.specificAccountId == encryptionClass.decrypt(u.accountId, key)) {
                tvSpecificAccName.text = encryptionClass.decrypt(u.accountName, key)
                tvSpecificAccCredentialField.text =
                        encryptionClass.decrypt(u.accountCredentialField, key)
                tvSpecificAccCredential.text = encryptionClass.decrypt(u.accountCredential, key)
                etSpecificAccPassword.setText(encryptionClass.decrypt(u.accountPassword, key))
                websiteUrl = encryptionClass.decrypt(u.accountWebsiteURL, key)

                applicationName = encryptionClass.decrypt(u.accountApplicationName, key)
                packageName = encryptionClass.decrypt(u.accountPackageName, key)
                val description = encryptionClass.decrypt(u.accountDescription, key)

                if (websiteUrl.isNotEmpty()) {
                    tvSpecificAccWebsiteURL.text = websiteUrl
                } else {
                    tvSpecificAccWebsiteURL.text = "-"
                }

                if (applicationName.isNotEmpty()) {
                    tvSpecificAccAppName.text = applicationName
                } else {
                    tvSpecificAccAppName.text = "-"
                }

                if (description.isNotEmpty()) {
                    tvSpecificAccDescription.text = description
                } else {
                    tvSpecificAccDescription.text = "-"
                }

                if (encryptionClass.decrypt(u.accountIsFavorites, key) == "1") {
                    val inflatedView = layoutInflater.inflate(
                            R.layout.layout_favorites_star, null, true
                    )
                    llSpecificAccFavorites.addView(inflatedView)
                }

                platformName = encryptionClass.decrypt(u.platformName, key)
                categoryName = encryptionClass.decrypt(u.categoryName, key)
            }
        }

        tvSpecificAccCategoryPlatform.text = "$categoryName > $platformName"
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        val ivSpecificAccTogglePass: ImageView =
                appCompatActivity.findViewById(R.id.ivSpecificAccTogglePass)

        ivSpecificAccTogglePass.setOnClickListener {
            when (passwordVisibility) {
                0 -> {                                                                              // Show password
                    ivSpecificAccTogglePass.setImageResource(R.drawable.ic_visibility_light_black)
                    etSpecificAccPassword.transformationMethod = null
                    passwordVisibility = 1
                }
                1 -> {                                                                              // Hide password
                    ivSpecificAccTogglePass.setImageResource(
                            R.drawable.ic_visibility_off_light_black
                    )
                    etSpecificAccPassword.transformationMethod = PasswordTransformationMethod()
                    passwordVisibility = 0
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setOnClick() {
        val tvSpecificAccCredentialCopy: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredentialCopy)
        val tvSpecificAccPasswordCopy: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccPasswordCopy)
        val tvSpecificAccWebsiteURLCopy: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccWebsiteURLCopy)
        val acbSpecificAccOpenPlatform: Button =
                appCompatActivity.findViewById(R.id.acbSpecificAccOpenPlatform)
        val clSpecificAccEdit: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSpecificAccEdit)
        val clSpecificAccDelete: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSpecificAccDelete)

        tvSpecificAccCredentialCopy.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val credentialField = tvSpecificAccCredentialField.text.toString()
                val credential = tvSpecificAccCredential.text.toString()
                val clipboard: ClipboardManager =
                        appCompatActivity.getSystemService(Context.CLIPBOARD_SERVICE)
                                as ClipboardManager
                val clip = ClipData.newPlainText("cred", credential)
                clipboard.setPrimaryClip(clip)

                showToast(credentialField)
                addActionLog(credentialField)
            } else {
                internetToast()
            }
        }

        tvSpecificAccPasswordCopy.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val password = etSpecificAccPassword.text.toString()
                val clipboard: ClipboardManager =
                        appCompatActivity.getSystemService(Context.CLIPBOARD_SERVICE)
                                as ClipboardManager
                val clip = ClipData.newPlainText("pw", password)
                clipboard.setPrimaryClip(clip)

                showToast("Password")
                addActionLog("Password")
            } else {
                internetToast()
            }
        }

        tvSpecificAccWebsiteURLCopy.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val websiteURL = tvSpecificAccWebsiteURL.text.toString()
                val clipboard: ClipboardManager =
                        appCompatActivity.getSystemService(Context.CLIPBOARD_SERVICE)
                                as ClipboardManager
                val clip = ClipData.newPlainText("url", websiteURL)
                clipboard.setPrimaryClip(clip)

                showToast("Website url")
                addActionLog("Website url")
            } else {
                internetToast()
            }
        }

        acbSpecificAccOpenPlatform.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                val inflater = this.layoutInflater
                val dialogView = inflater.inflate(
                        R.layout.layout_specific_account_open_platform, null
                )

                val llSpecificAccountBrowser: LinearLayout =
                        dialogView.findViewById(R.id.llSpecificAccountBrowser)
                val ivSpecificAccountBrowser: ImageView =
                        dialogView.findViewById(R.id.ivSpecificAccountBrowser)
                val tvSpecificAccountBrowser: TextView =
                        dialogView.findViewById(R.id.tvSpecificAccountBrowser)
                val llSpecificAccountWebView: LinearLayout =
                        dialogView.findViewById(R.id.llSpecificAccountWebView)
                val ivSpecificAccountWebView: ImageView =
                        dialogView.findViewById(R.id.ivSpecificAccountWebView)
                val tvSpecificAccountWebView: TextView =
                        dialogView.findViewById(R.id.tvSpecificAccountWebView)
                val llSpecificAccountApp: LinearLayout =
                        dialogView.findViewById(R.id.llSpecificAccountApp)
                val ivSpecificAccountApp: ImageView =
                        dialogView.findViewById(R.id.ivSpecificAccountApp)
                val tvSpecificAccountApp: TextView =
                        dialogView.findViewById(R.id.tvSpecificAccountApp)

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))        // Browser
                var browserPackage = ""

                if (websiteUrl.isNotEmpty()) {
                    try {                                                                           // Set default browser
                        val resolveInfo: ResolveInfo =
                                appCompatActivity.packageManager.resolveActivity(
                                        browserIntent, PackageManager.MATCH_DEFAULT_ONLY
                                )!!
                        val browserIcon = resolveInfo.activityInfo.loadIcon(
                                requireContext().packageManager
                        )
                        val browserName = resolveInfo.activityInfo.loadLabel(
                                requireContext().packageManager
                        ).toString()
                        browserPackage = resolveInfo.activityInfo.packageName
                        val browserNameText = "$browserName (default browser)"

                        ivSpecificAccountBrowser.setImageDrawable(browserIcon)
                        tvSpecificAccountBrowser.text = browserNameText
                    } catch (e: Exception) {                                                        // Cannot detect default browser
                        llSpecificAccountBrowser.isEnabled = false
                        ivSpecificAccountBrowser.setImageDrawable(null)
                        tvSpecificAccountBrowser.apply {
                            val noDefaultBrowser = "Cannot detect default browser"
                            text = noDefaultBrowser
                            setTextColor(ContextCompat.getColor(context, R.color.gray))
                        }
                    }
                } else {
                    llSpecificAccountBrowser.isEnabled = false
                    ivSpecificAccountBrowser.setImageDrawable(null)
                    tvSpecificAccountBrowser.apply {
                        val noApplication = "No Website URL"
                        text = noApplication
                        setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                }

                val packageManager: PackageManager = appCompatActivity.packageManager

                if (websiteUrl.isNotEmpty()) {
                    val sixKeeperPackageName = requireContext().packageName                         // 6Keeper app
                    val sixKeeperIcon = packageManager.getApplicationIcon(
                            packageManager.getApplicationInfo(
                                    sixKeeperPackageName, PackageManager.GET_META_DATA
                            )
                    )

                    ivSpecificAccountWebView.setImageDrawable(sixKeeperIcon)
                } else {
                    llSpecificAccountWebView.isEnabled = false
                    ivSpecificAccountWebView.setImageDrawable(null)
                    tvSpecificAccountWebView.apply {
                        val noApplication = "No Website URL"
                        text = noApplication
                        setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                }

                if (applicationName.isNotEmpty()) {                                                 // Selected app
                    val applicationNameText = "$applicationName (selected app)"
                    val applicationIcon = packageManager.getApplicationIcon(
                            packageManager.getApplicationInfo(
                                    packageName, PackageManager.GET_META_DATA
                            )
                    )

                    ivSpecificAccountApp.setImageDrawable(applicationIcon)
                    tvSpecificAccountApp.text = applicationNameText
                } else {
                    llSpecificAccountApp.isEnabled = false
                    ivSpecificAccountApp.setImageDrawable(null)
                    tvSpecificAccountApp.apply {
                        val noApplication = "No app is selected"
                        text = noApplication
                        setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                }

                builder.setView(dialogView)

                val alert: AlertDialog = builder.create()
                alert.apply {
                    window?.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                    context, R.drawable.layout_alert_dialog
                            )
                    )
                    setTitle(R.string.specific_account_open_platform)
                    show()
                }

                closeKeyboard()

                llSpecificAccountBrowser.setOnClickListener {
                    if (InternetConnectionClass().isConnected()) {
                        val browserURLIntent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(getWebsiteURL())
                        )
                        browserURLIntent.setPackage(browserPackage)
                        startActivity(browserURLIntent)

                        alert.cancel()
                    } else {
                        internetToast()
                    }
                }

                llSpecificAccountWebView.setOnClickListener {
                    if (InternetConnectionClass().isConnected()) {
                        val action = SpecificAccountFragmentDirections
                                .actionSpecificAccountFragmentToWebViewFragment(
                                        getWebsiteURL()
                                )
                        findNavController().navigate(action)

                        alert.cancel()
                    } else {
                        internetToast()
                    }
                }

                llSpecificAccountApp.setOnClickListener {
                    if (InternetConnectionClass().isConnected()) {
                        try {
                            val applicationIntent =
                                    appCompatActivity.packageManager.getLaunchIntentForPackage(
                                            packageName
                                    )
                            startActivity(applicationIntent)
                        } catch (e: Exception) {
                        } finally {
                            alert.cancel()
                        }
                    } else {
                        internetToast()
                    }
                }
            } else {
                internetToast()
            }
        }

        clSpecificAccEdit.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val action = SpecificAccountFragmentDirections
                        .actionSpecificAccountFragmentToAddAccountFragment(
                                args.specificPlatformId, "",
                                "", "edit", args.specificAccountId,
                        )
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clSpecificAccDelete.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                builder.setMessage(R.string.specific_account_delete)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    if (InternetConnectionClass().isConnected()) {
                        val goToConfirmActivity = Intent(
                                appCompatActivity, ConfirmActionActivity::class.java
                        )

                        @Suppress("DEPRECATION")
                        startActivityForResult(goToConfirmActivity, 16914)
                        appCompatActivity.overridePendingTransition(
                                R.anim.anim_enter_bottom_to_top_2, R.anim.anim_0
                        )
                    } else {
                        internetToast()
                    }
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title_confirm)
                alert.show()
            } else {
                internetToast()
            }

            it.apply {
                clSpecificAccDelete.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed({ clSpecificAccDelete.isClickable = true }, 1000)
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext, R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun showToast(field: String) {
        val toast = Toast.makeText(
                appCompatActivity.applicationContext, "$field was copied!", Toast.LENGTH_SHORT
        )
        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun addActionLog(field: String) {
        val accountName = tvSpecificAccName.text.toString()

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt(
                                "$field from account '$accountName' was copied.", key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    private fun getWebsiteURL(): String {
        var websiteURL = tvSpecificAccWebsiteURL.text.toString()

        if (
                !websiteURL.startsWith("http://")
                && !websiteURL.startsWith("https://")
        ) {
            websiteURL = "http://$websiteURL"
        }
        return websiteURL
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
            view?.apply {
                val status = databaseHandlerClass.updateDeleteAccount(
                        encryptionClass.encrypt(args.specificAccountId, key),
                        encryptionClass.encrypt(1.toString(), key),
                        encryptionClass.encrypt(getCurrentDate(), key),
                        "AccountsTable",
                        "account_id",
                        "account_deleted",
                        "account_delete_date"
                )

                if (status > -1) {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Account '${args.specificAccountName}' deleted!",
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                var actionLogId = 1000001
                val lastId = databaseHandlerClass.getLastIdOfActionLog()

                if (lastId.isNotEmpty()) {
                    actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encryptionClass.encrypt(actionLogId.toString(), key),
                                encryptionClass.encrypt("Account " +
                                        "'${args.specificAccountName}' was deleted.", key),
                                encryptionClass.encrypt(getCurrentDate(), key)
                        )
                )

                postDelayed({ appCompatActivity.onBackPressed() }, 250)
            }
        }
    }
}