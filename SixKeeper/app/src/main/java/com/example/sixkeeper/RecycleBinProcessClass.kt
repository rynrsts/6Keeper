package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class RecycleBinProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var lvRecycleBinContainer: ListView

    private lateinit var selectedTab: String
    private val selectedIdContainer = ArrayList<String>(0)

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
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

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

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

    @SuppressLint("DefaultLocale", "InflateParams")
    fun populateDeletedItems(accountName: String) {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "deleted",
                "",
                encodingClass.encodeData(1.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)

        if (userAccount.isNullOrEmpty()) {
            val llRecycleBinNoItem: LinearLayout =
                    appCompatActivity.findViewById(R.id.llRecycleBinNoItem)
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
        } else {
            for (u in userAccount) {
                val uAccountName = encodingClass.decodeData(u.accountName)

                if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
                    userAccountId.add(
                            encodingClass.decodeData(u.accountId) + "ramjcammjar" + uAccountName
                    )
                    userAccountName.add(uAccountName)
                }
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName
            )

            lvRecycleBinContainer.adapter = accountsListAdapter
        }
    }
}