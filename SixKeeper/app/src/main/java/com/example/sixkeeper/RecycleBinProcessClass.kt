package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

    private lateinit var selectedTab: String
    private val itemSelected = ArrayList<Int>(0)
    private val selectedIdContainer = ArrayList<String>(0)

    fun getEncodingClass(): EncodingClass {
        return encodingClass
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getCbRecycleBinSelectAll(): CheckBox {
        return cbRecycleBinSelectAll
    }

    fun getLvRecycleBinContainer(): ListView {
        return lvRecycleBinContainer
    }

    fun setSelectedTab(s: String) {
        selectedTab = s
    }

    fun getSelectedTab(): String {
        return selectedTab
    }

    fun getItemSelected(): ArrayList<Int> {
        return itemSelected
    }

    fun getSelectedIdContainer(): ArrayList<String> {
        return selectedIdContainer
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
                userAccountId.add(encodingClass.decodeData(u.accountId))
                userAccountName.add(encodingClass.decodeData(u.accountName))
                itemSelected.add(0)
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName,
                    "accounts"
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
                userPasswordId.add(encodingClass.decodeData(u.passId))
                userPasswordPass.add(encodingClass.decodeData(u.generatedPassword))
                itemSelected.add(0)
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userPasswordId,
                    userPasswordPass,
                    "passwords"
            )

            llRecycleBinNoItem.removeAllViews()
            lvRecycleBinContainer.adapter = accountsListAdapter
        }
    }

    private fun clearAll() {
        cbRecycleBinSelectAll.isChecked = false
        itemSelected.clear()
        selectedIdContainer.clear()
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
                var selectedId = arrayOfNulls<String>(selectedIdContainer.size)
                selectedId = selectedIdContainer.toArray(selectedId)
                var tableName = ""
                var field = ""

                if (selectedTab == "accounts") {
                    tableName = "AccountsTable"
                    field = "account_id"
                } else if (selectedTab == "password generator") {
                    tableName = "SavedPasswordTable"
                    field = "pass_id"
                }

                val status = databaseHandlerClass.removeRecycleBin(selectedId, tableName, field)

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
}