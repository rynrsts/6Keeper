package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class RecycleBinFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var lvRecycleBinContainer: ListView

    private val selectedIdContainer = ArrayList<String>(0)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycle_bin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        populateDeletedItems("")
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
        encodingClass = EncodingClass()

        lvRecycleBinContainer = appCompatActivity.findViewById(R.id.lvRecycleBinContainer)
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

    @SuppressLint("DefaultLocale", "InflateParams")
    private fun populateDeletedItems(accountName: String) {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "deleted",
                "",
                encodingClass.encodeData(1.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)

        if (userAccount.isNullOrEmpty()) {
            val llRecycleBinNoItem: LinearLayout =
                    appCompatActivity.findViewById(R.id.llRecycleBinNoItem)
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvAccountsNoItem.setText(R.string.many_no_account)
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

                    val userPlatform: List<UserPlatformModelClass> =
                            databaseHandlerClass.viewPlatform("platform", u.platformId)
                    var uPlatformName: String

                    for (up in userPlatform) {
                        uPlatformName = encodingClass.decodeData(up.platformName)

                        val userCategory: List<UserCategoryModelClass> =
                                databaseHandlerClass.viewCategory("category", up.categoryId)
                        var uCategoryName = ""

                        for (uc in userCategory) {
                            uCategoryName = encodingClass.decodeData(uc.categoryName)
                        }

                        userAccountDirectory.add("$uCategoryName > $uPlatformName")
                    }
                }
            }

            val accountsListAdapter = RecycleBinListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName,
                    userAccountDirectory
            )

            lvRecycleBinContainer.adapter = accountsListAdapter
        }
    }

    private fun setOnClick() {
        val clRecycleBinRestore: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clRecycleBinRestore)

        lvRecycleBinContainer.onItemClickListener = (OnItemClickListener { _, view, pos, _ ->
            val selectedItem = lvRecycleBinContainer.getItemAtPosition(pos).toString()
            val selectedItemValue = selectedItem.split("ramjcammjar")
            val selectedItemId = selectedItemValue[0]
            val cbRecycleBinCheckBox: CheckBox = view.findViewById(R.id.cbRecycleBinCheckBox)

            if (cbRecycleBinCheckBox.isChecked) {
                cbRecycleBinCheckBox.isChecked = false
                selectedIdContainer.remove(encodingClass.encodeData(selectedItemId))
            } else {
                cbRecycleBinCheckBox.isChecked = true
                selectedIdContainer.add(encodingClass.encodeData(selectedItemId))
            }
        })

        clRecycleBinRestore.setOnClickListener {
            var selectedIdNoDuplicate = arrayOfNulls<String>(selectedIdContainer.size)
            selectedIdNoDuplicate = selectedIdContainer.toArray(selectedIdNoDuplicate)

            databaseHandlerClass.updateDeleteMultipleAccount(
                    encodingClass.encodeData(0.toString()),
                    "",
                    selectedIdNoDuplicate,
                    "account_id"
            )

            selectedIdContainer.clear()
            populateDeletedItems("")
        }
    }
}