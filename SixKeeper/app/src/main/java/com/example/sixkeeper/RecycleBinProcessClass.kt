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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class RecycleBinProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var cbRecycleBinSelectAll: CheckBox
    private lateinit var llRecycleBinNoItem: LinearLayout
    private lateinit var lvRecycleBinContainer: ListView

    private val modelArrayList = ArrayList<RecycleBinModelClass>(0)
    private lateinit var recycleBinModelClass: RecycleBinModelClass

    private lateinit var key: ByteArray
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
        encryptionClass = EncryptionClass()

        cbRecycleBinSelectAll = appCompatActivity.findViewById(R.id.cbRecycleBinSelectAll)
        llRecycleBinNoItem = appCompatActivity.findViewById(R.id.llRecycleBinNoItem)
        lvRecycleBinContainer = appCompatActivity.findViewById(R.id.lvRecycleBinContainer)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()

        selectedTab = "accounts"
    }

    fun disableMenuItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = true
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = false
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
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
                encryptionClass.encrypt(1.toString(), key)
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)

        clearAll()

        if (userAccount.isNullOrEmpty()) {
            inflateNoItem()
        } else {
            for (u in userAccount) {
                val uId = encryptionClass.decrypt(u.accountId, key)

                userAccountId.add(uId)
                userAccountName.add(encryptionClass.decrypt(u.accountName, key))

                recycleBinModelClass = RecycleBinModelClass()
                recycleBinModelClass.setSelected(false)
                recycleBinModelClass.setId(Integer.parseInt(uId))
                recycleBinModelClass.setAccountName(encryptionClass.decrypt(u.accountName, key))
                recycleBinModelClass.setPlatformName(encryptionClass.decrypt(u.platformName, key))
                recycleBinModelClass.setCategoryName(encryptionClass.decrypt(u.categoryName, key))
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
                databaseHandlerClass.viewSavedPass(encryptionClass.encrypt(1.toString(), key))
        val userPasswordId = ArrayList<String>(0)
        val userPasswordPass = ArrayList<String>(0)

        clearAll()

        if (userSavedPass.isNullOrEmpty()) {
            inflateNoItem()
        } else {
            for (u in userSavedPass) {
                val uId = encryptionClass.decrypt(u.passId, key)

                userPasswordId.add(uId)
                userPasswordPass.add(encryptionClass.decrypt(u.generatedPassword, key))

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
        lvRecycleBinContainer.adapter = null
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

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
            val container = ArrayList<String>(0)

            for (i in 0 until modelArrayList.size) {
                if (modelArrayList[i].getSelected()) {
                    container.add(
                            encryptionClass.encrypt(
                                    modelArrayList[i].getId().toString(), key
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

            val status = databaseHandlerClass.removeRecycleBin(selectedId, tableName, field)        // Delete selected items

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
            var actionLogMessage = ""

            if (selectedTab == "accounts") {
                populateDeletedAccounts()
                actionLogMessage = "Selected account/s were deleted from Recycle Bin."
            } else if (selectedTab == "password generator") {
                populateDeletedPasswords()
                actionLogMessage = "Selected generated password/s were deleted from Recycle Bin."
            }

            databaseHandlerClass.addEventToActionLog(                                               // Add event to Action Log
                    UserActionLogModelClass(
                            encryptionClass.encrypt(getLastActionLogId().toString(), key),
                            encryptionClass.encrypt(actionLogMessage, key),
                            encryptionClass.encrypt(getCurrentDate(), key)
                    )
            )
        }
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
                    containerId.add(encryptionClass.encrypt(modelArrayList[i].getId().toString(), key))
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

        var actionLogMessage = ""

        if (selectedTab == "accounts") {
            restoreAccounts(
                    selectedId, selectedAccountName, selectedPlatformName, selectedCategoryName
            )
            actionLogMessage = "Selected account/s were restored to Accounts."
        } else if (selectedTab == "password generator") {
            restoreSavedPasswords(selectedId)
            actionLogMessage = "Selected generated password/s were restored to Saved Passwords."
        }

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt(actionLogMessage, key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )
    }

    private fun restoreSavedPasswords(selectedId: Array<String?>) {                                 // Restore Saved Passwords
        val status = databaseHandlerClass.updateDeleteMultipleAccount(
                selectedId,
                encryptionClass.encrypt(0.toString(), key),
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
            val encryptedId = encryptionClass.encrypt(selectedId[i]!!, key)
            val encryptedPlatformName = encryptionClass.encrypt(selectedPlatformName[i]!!, key)
            val encryptedCategoryName = encryptionClass.encrypt(selectedCategoryName[i]!!, key)

            val userCategory: List<UserCategoryModelClass> =
                    databaseHandlerClass.viewCategory("", "")
            var categoryId = 10001
            var isExisting = false
            var exencryptedCategoryId = ""
            var exencryptedCategoryName = ""

            for (u in userCategory) {
                if (selectedCategoryName[i].equals(
                                encryptionClass.decrypt(u.categoryName, key), ignoreCase = true)
                ) {
                    isExisting = true
                    exencryptedCategoryId = u.categoryId
                    exencryptedCategoryName = u.categoryName
                    break
                }

                categoryId = Integer.parseInt(encryptionClass.decrypt(u.categoryId, key)) + 1
            }

            var platformId = 10001
            val lastId = databaseHandlerClass.getLastIdOfPlatform()

            if (lastId.isNotEmpty()) {
                platformId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
            }
            val encryptedPlatformId = encryptionClass.encrypt(platformId.toString(), key)

            if (!isExisting) {                                                                      // If Category is not existing...
                val encryptedCategoryId = encryptionClass.encrypt(categoryId.toString(), key)

                databaseHandlerClass.addCategory(                                                   // Add Category
                        UserCategoryModelClass(encryptedCategoryId, encryptedCategoryName)
                )
                databaseHandlerClass.addPlatform(                                                   // Add Platform
                        UserPlatformModelClass(
                                encryptedPlatformId,
                                encryptedPlatformName,
                                encryptedCategoryId,
                                encryptedCategoryName
                        )
                )
                databaseHandlerClass.updateAccountPath(
                        encryptedId, encryptionClass.encrypt(0.toString(), key), "",
                        encryptedPlatformId, encryptedPlatformName, encryptedCategoryName
                )
                increaseRestoreCount()
            } else {                                                                                // If Category is existing...
                val userPlatform: List<UserPlatformModelClass> = databaseHandlerClass.viewPlatform(
                        "category",
                        exencryptedCategoryId
                )
                var isExistingP = false
                var exencryptedPlatformId = ""
                var exencryptedPlatformName = ""

                for (up in userPlatform) {
                    if (selectedPlatformName[i].equals(
                                    encryptionClass.decrypt(up.platformName, key), ignoreCase = true)
                    ) {
                        isExistingP = true
                        exencryptedPlatformId = up.platformId
                        exencryptedPlatformName = up.platformName
                        break
                    }
                }

                if (!isExistingP) {                                                                 // If Platform is not existing...
                    databaseHandlerClass.addPlatform(                                               // Add Platform
                            UserPlatformModelClass(
                                    encryptedPlatformId,
                                    encryptedPlatformName,
                                    exencryptedCategoryId,
                                    exencryptedCategoryName
                            )
                    )
                    databaseHandlerClass.updateAccountPath(
                            encryptedId, encryptionClass.encrypt(0.toString(), key), "",
                            encryptedPlatformId, encryptedPlatformName, exencryptedCategoryName
                    )
                    increaseRestoreCount()
                } else {                                                                            // If Platform is existing...
                    val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                            "platformId",
                            exencryptedPlatformId,
                            encryptionClass.encrypt(0.toString(), key)
                    )
                    var isExistingA = false
                    var encryptedExistingId = ""

                    for (ua in userAccount) {
                        if (selectedAccountName[i].equals(
                                        encryptionClass.decrypt(ua.accountName, key), ignoreCase = true)
                        ) {
                            isExistingA = true
                            encryptedExistingId = ua.accountId
                            break
                        }
                    }

                    if (!isExistingA) {                                                             // If account is not existing...
                        databaseHandlerClass.updateAccountPath(
                                encryptedId, encryptionClass.encrypt(0.toString(), key), "",
                                exencryptedPlatformId, exencryptedPlatformName, exencryptedCategoryName
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
                            idTemp.add(encryptedExistingId)
                            var accountIdTemp = arrayOfNulls<String>(idTemp.size)
                            accountIdTemp = idTemp.toArray(accountIdTemp)

                            databaseHandlerClass.removeRecycleBin(
                                    accountIdTemp,
                                    "AccountsTable",
                                    "account_id"
                            )
                            databaseHandlerClass.updateAccountPath(
                                    encryptedId, encryptionClass.encrypt(0.toString(), key), "",
                                    exencryptedPlatformId, exencryptedPlatformName, exencryptedCategoryName
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