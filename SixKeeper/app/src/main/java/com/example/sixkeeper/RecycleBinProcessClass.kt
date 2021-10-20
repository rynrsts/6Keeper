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
import androidx.fragment.app.Fragment
import kotlin.collections.ArrayList

open class RecycleBinProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var cbRecycleBinSelectAll: CheckBox
    private lateinit var llRecycleBinNoItem: LinearLayout
    private lateinit var lvRecycleBinContainer: ListView

    private val modelArrayList = ArrayList<RecycleBinModelClass>(0)
    private lateinit var recycleBinModelClass: RecycleBinModelClass

    private lateinit var selectedTab: String

    private var totalRestoreNum = 0
    private var restoreCount = 0

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getModelArrayList(): ArrayList<RecycleBinModelClass> {
        return modelArrayList
    }

    fun setSelectedTab(s: String) {
        selectedTab = s
    }

    fun getSelectedTab(): String {
        return selectedTab
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        cbRecycleBinSelectAll = appCompatActivity.findViewById(R.id.cbRecycleBinSelectAll)
        llRecycleBinNoItem = appCompatActivity.findViewById(R.id.llRecycleBinNoItem)
        lvRecycleBinContainer = appCompatActivity.findViewById(R.id.lvRecycleBinContainer)

        selectedTab = "accounts"
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

    fun populateDeletedAccounts() {                                                                 // Populate with deleted Accounts
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "deleted",
                "",
                encodingClass.encodeData(1.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)

        clearAll()

        if (userAccount.isNullOrEmpty()) {
            inflateNoItem()
        } else {
            for (u in userAccount) {
                val uId = encodingClass.decodeData(u.accountId)

                userAccountId.add(uId)
                userAccountName.add(encodingClass.decodeData(u.accountName))

                recycleBinModelClass = RecycleBinModelClass()
                recycleBinModelClass.setSelected(false)
                recycleBinModelClass.setId(Integer.parseInt(uId))
                recycleBinModelClass.setAccountName(encodingClass.decodeData(u.accountName))
                recycleBinModelClass.setPlatformName(encodingClass.decodeData(u.platformName))
                recycleBinModelClass.setCategoryName(encodingClass.decodeData(u.categoryName))
                modelArrayList.add(recycleBinModelClass)
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName,
                    "accounts",
                    modelArrayList
            )

            llRecycleBinNoItem.removeAllViews()
            lvRecycleBinContainer.adapter = accountsListAdapter
        }
    }

    fun populateDeletedPasswords() {                                                                // Populate with deleted Saved Passwords
        val userSavedPass: List<UserSavedPassModelClass> =
                databaseHandlerClass.viewSavedPass(encodingClass.encodeData(1.toString()))
        val userPasswordId = ArrayList<String>(0)
        val userPasswordPass = ArrayList<String>(0)

        clearAll()

        if (userSavedPass.isNullOrEmpty()) {
            inflateNoItem()
        } else {
            for (u in userSavedPass) {
                val uId = encodingClass.decodeData(u.passId)

                userPasswordId.add(uId)
                userPasswordPass.add(encodingClass.decodeData(u.generatedPassword))

                recycleBinModelClass = RecycleBinModelClass()
                recycleBinModelClass.setSelected(false)
                recycleBinModelClass.setId(Integer.parseInt(uId))
                modelArrayList.add(recycleBinModelClass)
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userPasswordId,
                    userPasswordPass,
                    "passwords",
                    modelArrayList
            )

            llRecycleBinNoItem.removeAllViews()
            lvRecycleBinContainer.adapter = accountsListAdapter
        }
    }

    private fun clearAll() {
        if (cbRecycleBinSelectAll.isChecked) {
            cbRecycleBinSelectAll.performClick()
        }

        modelArrayList.clear()
    }

    @SuppressLint("InflateParams")
    private fun inflateNoItem() {
        val inflatedView = layoutInflater.inflate(
                R.layout.layout_accounts_no_item,
                null,
                true
        )
        val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

        tvAccountsNoItem.setText(R.string.recycle_bin_no_item)
        lvRecycleBinContainer.adapter = null
        llRecycleBinNoItem.apply {
            removeAllViews()
            addView(inflatedView)
        }
    }

    fun confirmMasterPIN() {                                                                        // Show Confirm Master PIN
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                val container = ArrayList<String>(0)

                for (i in 0 until modelArrayList.size) {
                    if (modelArrayList[i].getSelected()) {
                        container.add(
                                encodingClass.encodeData(
                                        modelArrayList[i].getId().toString()
                                )
                        )
                    }
                }

                var selectedId = arrayOfNulls<String>(container.size)
                selectedId = container.toArray(selectedId)
                var tableName = ""
                var field = ""

                if (selectedTab == "accounts") {
                    tableName = "AccountsTable"
                    field = "account_id"
                } else if (selectedTab == "password generator") {
                    tableName = "SavedPasswordTable"
                    field = "pass_id"
                }

                val status = databaseHandlerClass.removeRecycleBin(selectedId, tableName, field)    // Delete selected items

                if (status > -1) {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.recycle_bin_selected_items_delete_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                if (selectedTab == "accounts") {
                    populateDeletedAccounts()
                } else if (selectedTab == "password generator") {
                    populateDeletedPasswords()
                }
            }
        }
    }

    fun restoreSelectedItems() {                                                                    // Restore selected items
        val containerId = ArrayList<String>(0)
        val containerAccountName = ArrayList<String>(0)
        val containerPlatformName = ArrayList<String>(0)
        val containerCategoryName = ArrayList<String>(0)

        for (i in 0 until modelArrayList.size) {
            if (modelArrayList[i].getSelected()) {
                if (selectedTab == "accounts") {
                    containerId.add(modelArrayList[i].getId().toString())
                    containerAccountName.add(modelArrayList[i].getAccountName())
                    containerPlatformName.add(modelArrayList[i].getPlatformName())
                    containerCategoryName.add(modelArrayList[i].getCategoryName())
                } else if (selectedTab == "password generator") {
                    containerId.add(encodingClass.encodeData(modelArrayList[i].getId().toString()))
                }
            }
        }

        var selectedId = arrayOfNulls<String>(containerId.size)
        selectedId = containerId.toArray(selectedId)

        var selectedAccountName = arrayOfNulls<String>(containerAccountName.size)
        selectedAccountName = containerAccountName.toArray(selectedAccountName)

        var selectedPlatformName = arrayOfNulls<String>(containerPlatformName.size)
        selectedPlatformName = containerPlatformName.toArray(selectedPlatformName)

        var selectedCategoryName = arrayOfNulls<String>(containerCategoryName.size)
        selectedCategoryName = containerCategoryName.toArray(selectedCategoryName)

        if (selectedTab == "accounts") {
            restoreAccounts(
                    selectedId, selectedAccountName, selectedPlatformName, selectedCategoryName
            )
        } else if (selectedTab == "password generator") {
            restoreSavedPasswords(selectedId)
        }
    }

    private fun restoreSavedPasswords(selectedId: Array<String?>) {                                 // Restore Saved Passwords
        val status = databaseHandlerClass.updateDeleteMultipleAccount(
                selectedId,
                encodingClass.encodeData(0.toString()),
                "",
                "SavedPasswordTable",
                "pass_id",
                "pass_deleted",
                "pass_delete_date"
        )

        if (status > -1) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.recycle_bin_selected_items_restore_mes,
                    Toast.LENGTH_SHORT
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        populateDeletedPasswords()
    }

    private fun restoreAccounts(                                                                    // Restore Accounts
            selectedId: Array<String?>,
            selectedAccountName: Array<String?>,
            selectedPlatformName: Array<String?>,
            selectedCategoryName: Array<String?>
    ) {
        totalRestoreNum = selectedId.size

        for (i in selectedId.indices) {
            val encodedId = encodingClass.encodeData(selectedId[i]!!)
            val encodedPlatformName = encodingClass.encodeData(selectedPlatformName[i]!!)
            val encodedCategoryName = encodingClass.encodeData(selectedCategoryName[i]!!)

            val userCategory: List<UserCategoryModelClass> =
                    databaseHandlerClass.viewCategory("", "")
            var categoryId = 10001
            var isExisting = false
            var exEncodedCategoryId = ""
            var exEncodedCategoryName = ""

            for (u in userCategory) {
                if (selectedCategoryName[i].equals(
                                encodingClass.decodeData(u.categoryName), ignoreCase = true)
                ) {
                    isExisting = true
                    exEncodedCategoryId = u.categoryId
                    exEncodedCategoryName = u.categoryName
                    break
                }

                categoryId = Integer.parseInt(encodingClass.decodeData(u.categoryId)) + 1
            }

            var platformId = 10001
            val lastId = databaseHandlerClass.getLastIdOfPlatform()

            if (lastId.isNotEmpty()) {
                platformId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
            }
            val encodedPlatformId = encodingClass.encodeData(platformId.toString())

            if (!isExisting) {                                                                      // If Category is not existing...
                val encodedCategoryId = encodingClass.encodeData(categoryId.toString())

                databaseHandlerClass.addCategory(                                                   // Add Category
                        UserCategoryModelClass(encodedCategoryId, encodedCategoryName)
                )
                databaseHandlerClass.addPlatform(                                                   // Add Platform
                        UserPlatformModelClass(
                                encodedPlatformId,
                                encodedPlatformName,
                                encodedCategoryId,
                                encodedCategoryName
                        )
                )
                databaseHandlerClass.updateAccountPath(
                        encodedId, encodingClass.encodeData(0.toString()), "",
                        encodedPlatformId, encodedPlatformName, encodedCategoryName
                )
                increaseRestoreCount()
            } else {                                                                                // If Category is existing...
                val userPlatform: List<UserPlatformModelClass> = databaseHandlerClass.viewPlatform(
                        "category",
                        exEncodedCategoryId
                )
                var isExistingP = false
                var exEncodedPlatformId = ""
                var exEncodedPlatformName = ""

                for (up in userPlatform) {
                    if (selectedPlatformName[i].equals(
                                    encodingClass.decodeData(up.platformName), ignoreCase = true)
                    ) {
                        isExistingP = true
                        exEncodedPlatformId = up.platformId
                        exEncodedPlatformName = up.platformName
                        break
                    }
                }

                if (!isExistingP) {                                                                 // If Platform is not existing...
                    databaseHandlerClass.addPlatform(                                               // Add Platform
                            UserPlatformModelClass(
                                    encodedPlatformId,
                                    encodedPlatformName,
                                    exEncodedCategoryId,
                                    exEncodedCategoryName
                            )
                    )
                    databaseHandlerClass.updateAccountPath(
                            encodedId, encodingClass.encodeData(0.toString()), "",
                            encodedPlatformId, encodedPlatformName, exEncodedCategoryName
                    )
                    increaseRestoreCount()
                } else {                                                                            // If Platform is existing...
                    val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                            "platformId",
                            exEncodedPlatformId,
                            encodingClass.encodeData(0.toString())
                    )
                    var isExistingA = false
                    var encodedExistingId = ""

                    for (ua in userAccount) {
                        if (selectedAccountName[i].equals(
                                        encodingClass.decodeData(ua.accountName), ignoreCase = true)
                        ) {
                            isExistingA = true
                            encodedExistingId = ua.accountId
                            break
                        }
                    }

                    if (!isExistingA) {                                                             // If account is not existing...
                        databaseHandlerClass.updateAccountPath(
                                encodedId, encodingClass.encodeData(0.toString()), "",
                                exEncodedPlatformId, exEncodedPlatformName, exEncodedCategoryName
                        )
                        increaseRestoreCount()
                    } else {
                        val builder: AlertDialog.Builder =
                                AlertDialog.Builder(getAppCompatActivity())
                        val name = selectedAccountName[i]!!

                        builder.setMessage("Account $name is already existing. Replace it?")
                        builder.setCancelable(false)

                        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                            val idTemp = ArrayList<String>(0)
                            idTemp.add(encodedExistingId)
                            var accountIdTemp = arrayOfNulls<String>(idTemp.size)
                            accountIdTemp = idTemp.toArray(accountIdTemp)

                            databaseHandlerClass.removeRecycleBin(
                                    accountIdTemp,
                                    "AccountsTable",
                                    "account_id"
                            )
                            databaseHandlerClass.updateAccountPath(
                                    encodedId, encodingClass.encodeData(0.toString()), "",
                                    exEncodedPlatformId, exEncodedPlatformName, exEncodedCategoryName
                            )
                            increaseRestoreCount()
                        }
                        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                            dialog.cancel()
                            increaseRestoreCount()
                        }

                        val alert: AlertDialog = builder.create()
                        alert.setTitle(R.string.many_alert_title)
                        alert.show()
                    }
                }
            }
        }
    }

    private fun increaseRestoreCount() {
        restoreCount++

        if (restoreCount == totalRestoreNum) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.recycle_bin_selected_items_restore_mes,
                    Toast.LENGTH_SHORT
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }

            populateDeletedAccounts()
            totalRestoreNum = 0
            restoreCount = 0
        }
    }
}