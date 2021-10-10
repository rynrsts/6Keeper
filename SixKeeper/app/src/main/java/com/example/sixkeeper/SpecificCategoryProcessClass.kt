package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class SpecificCategoryProcessClass : Fragment() {
    private val args: SpecificCategoryFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etSpecificCatSearchBox: EditText
    private lateinit var lvSpecificCatContainer: ListView
    private lateinit var llSpecificCatNoItem: LinearLayout

    private lateinit var platformIdTemp: String
    private lateinit var platformNameTemp: String

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun getArgsSpecificCategoryName(): String {
        return args.specificCategoryName
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtSpecificCatSearchBox(): EditText {
        return etSpecificCatSearchBox
    }

    fun getLvSpecificCatContainer(): ListView {
        return lvSpecificCatContainer
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        etSpecificCatSearchBox = appCompatActivity.findViewById(R.id.etSpecificCatSearchBox)
        lvSpecificCatContainer = appCompatActivity.findViewById(R.id.lvSpecificCatContainer)
        llSpecificCatNoItem = appCompatActivity.findViewById(R.id.llSpecificCatNoItem)
    }

    fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = getString(R.string.specific_category_platforms)
    }

    fun setHierarchy() {
        val tvSpecificCatHierarchy: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificCatHierarchy)
        tvSpecificCatHierarchy.text = args.specificCategoryName
    }

    fun closeKeyboard() {
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

    @SuppressLint("DefaultLocale", "InflateParams")
    fun populatePlatforms(platformName: String) {                                                   // Populate list view with platforms
        val userPlatform: List<UserPlatformModelClass> = databaseHandlerClass.viewPlatform(
                "category",
                encodingClass.encodeData(args.specificCategoryId)
        )
        val userPlatformId = ArrayList<String>(0)
        val userPlatformName = ArrayList<String>(0)
        val userNumberOfAccounts = ArrayList<String>(0)

        if (userPlatform.isNullOrEmpty()) {
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvAccountsNoItem.setText(R.string.many_no_platform)
            lvSpecificCatContainer.adapter = null
            llSpecificCatNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userPlatform) {
                val uPlatformName = encodingClass.decodeData(u.platformName)
                val uPlatformNum = databaseHandlerClass.viewNumberOfAccounts(
                        encodingClass.encodeData(0.toString()),
                        u.platformId
                ).toString()

                if (uPlatformName.toLowerCase().startsWith(platformName.toLowerCase())) {
                    userPlatformId.add(
                            encodingClass.decodeData(u.platformId) + "ramjcammjar" +
                                    uPlatformName + "ramjcammjar" + uPlatformNum

                    )
                    userPlatformName.add(uPlatformName)
                    userNumberOfAccounts.add(uPlatformNum)
                }
            }

            val categoriesPlatformsListAdapter = CategoriesPlatformsListAdapter(
                    attActivity,
                    "Accounts:",
                    userPlatformId,
                    userPlatformName,
                    userNumberOfAccounts
            )

            lvSpecificCatContainer.adapter = categoriesPlatformsListAdapter
        }
    }

    @SuppressLint("InflateParams")
    fun showAddUpdatePlatform(
            addOrUpdate: String,
            selectedPlatformName: String,
            selectedPlatformId: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_accounts_add_new, null)

        builder.apply {
            setView(dialogView)
            setCancelable(false)
        }

        val tvAccountsAddNew: TextView = dialogView.findViewById(R.id.tvAccountsAddNew)
        val etAccountsAddNew: EditText = dialogView.findViewById(R.id.etAccountsAddNew)
        val ivAccountsAddNew: ImageView = dialogView.findViewById(R.id.ivAccountsAddNew)

        tvAccountsAddNew.setText(R.string.specific_category_platform_name)
        ivAccountsAddNew.setImageResource(R.drawable.ic_globe_light_black)

        var buttonName = ""
        var dialogTitle = ""
        var toastMes = ""

        if (addOrUpdate == "add") {
            buttonName = "Add"
            dialogTitle = resources.getString(R.string.specific_category_new_platform)
            toastMes = resources.getString(R.string.many_nothing_to_add)
        } else if (addOrUpdate == "update") {
            buttonName = "Update"
            dialogTitle = resources.getString(R.string.specific_category_edit_platform) + ": " + selectedPlatformName
            toastMes = resources.getString(R.string.many_nothing_to_update)

            etAccountsAddNew.apply {
                setText(selectedPlatformName)
                setSelection(etAccountsAddNew.text.length)
            }
        }

        builder.setPositiveButton(buttonName) { _: DialogInterface, _: Int ->
            val platformName = etAccountsAddNew.text.toString()

            if (platformName.isNotEmpty()) {
                addOrUpdatePlatform(
                        addOrUpdate,
                        platformName,
                        selectedPlatformId,
                        selectedPlatformName
                )
            } else {
                val toast: Toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        toastMes,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }

            view?.apply {
                postDelayed(
                        {
                            closeKeyboard()
                        }, 50
                )
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            dialog.cancel()

            view?.apply {
                postDelayed(
                        {
                            closeKeyboard()
                        }, 50
                )
            }
        }

        val alert: AlertDialog = builder.create()
        alert.apply {
            window?.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                            getAppCompatActivity(),
                            R.drawable.layout_alert_dialog
                    )
            )
            setTitle(dialogTitle)
            show()
        }
    }

    @SuppressLint("ShowToast")
    fun addOrUpdatePlatform(
            addOrUpdate: String,
            platformName: String,
            selectedPlatformId: String,
            selectedPlatformName: String
    ) {
        val encodedArgs = encodingClass.encodeData(args.specificCategoryId)
        val userPlatform: List<UserPlatformModelClass> =
                databaseHandlerClass.viewPlatform("category", encodedArgs)
        var platformId = 10001
        val lastId = databaseHandlerClass.getLastIdOfPlatform()
        var existing = false
        var toast: Toast? = null

        if (lastId.isNotEmpty()) {
            platformId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
        }

        for (u in userPlatform) {
            if (
                    platformName.equals(
                            encodingClass.decodeData(u.platformName),
                            ignoreCase = true
                    ) &&
                    !platformName.equals(selectedPlatformName, ignoreCase = true)
            ) {
                existing = true
                break
            }
        }

        if (!existing) {
            if (addOrUpdate == "add") {
                val status = databaseHandlerClass.addPlatform(                                      // Add Platform
                        UserPlatformModelClass(
                                encodingClass.encodeData(platformId.toString()),
                                encodingClass.encodeData(platformName),
                                encodedArgs
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Platform '$platformName' added!",
                            Toast.LENGTH_SHORT
                    )
                }
            } else if (addOrUpdate == "update") {
                val status = databaseHandlerClass.updatePlatform(                                   // Update Platform
                        UserPlatformModelClass(
                                encodingClass.encodeData(selectedPlatformId),
                                encodingClass.encodeData(platformName),
                                ""
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Platform updated to '$platformName'",
                            Toast.LENGTH_SHORT
                    )
                }
            }

            populatePlatforms("")
            llSpecificCatNoItem.removeAllViews()
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.specific_category_new_platform_alert_existing,
                    Toast.LENGTH_SHORT
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        closeKeyboard()
    }

    fun showDeletePlatform(
            selectedPlatformId: String,
            selectedPlatformName: String,
            selectedPlatformNum: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)

        val toastMessage = if (selectedPlatformNum == "0") {
            "Delete platform?"
        } else {
            "Selected platform is not empty. Deleting it will also delete its contents. Delete anyway?"
        }

        builder.apply {
            setMessage(toastMessage)
            setCancelable(false)
        }

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            platformIdTemp = selectedPlatformId
            platformNameTemp = selectedPlatformName

            if (selectedPlatformNum == "0") {
                deletePlatform()
            } else {
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
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }

    private fun deletePlatform() {                                                                  // Delete Platform
        val status = databaseHandlerClass.removeCatPlatAcc(
                "PlatformsTable",
                "platform_id",
                encodingClass.encodeData(platformIdTemp)
        )

        if (status > -1) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    "Platform '$platformNameTemp' deleted!",
                    Toast.LENGTH_SHORT
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        populatePlatforms("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                deletePlatform()
                deleteAccount()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun deleteAccount() {                                                                   // Delete Platforms' contents
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        databaseHandlerClass.updateDeleteAccount(
                encodingClass.encodeData(platformIdTemp),
                encodingClass.encodeData(1.toString()),
                encodingClass.encodeData(date),
                "AccountsTable",
                "platform_id",
                "account_deleted",
                "account_delete_date"
        )
    }
}