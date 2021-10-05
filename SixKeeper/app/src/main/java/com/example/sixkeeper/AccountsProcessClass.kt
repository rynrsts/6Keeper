package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

open class AccountsProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etAccountsSearchBox: EditText
    private lateinit var lvAccountsContainer: ListView
    private lateinit var llAccountsNoItem: LinearLayout

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtAccountsSearchBox(): EditText {
        return etAccountsSearchBox
    }

    fun getLvAccountsContainer(): ListView {
        return lvAccountsContainer
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        etAccountsSearchBox = appCompatActivity.findViewById(R.id.etAccountsSearchBox)
        lvAccountsContainer = appCompatActivity.findViewById(R.id.lvAccountsContainer)
        llAccountsNoItem = appCompatActivity.findViewById(R.id.llAccountsNoItem)
    }

    fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = getString(R.string.accounts_categories)
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
    fun populateCategories(categoryName: String) {                                                  // Populate list view with categories
        val userCategory: List<UserCategoryModelClass> =
                databaseHandlerClass.viewCategory("", "")
        val userCategoryItemValue = ArrayList<String>(0)
        val userCategoryName = ArrayList<String>(0)
        val userNumberOfPlatforms = ArrayList<String>(0)

        if (userCategory.isNullOrEmpty()) {
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvAccountsNoItem.setText(R.string.many_no_category)
            llAccountsNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userCategory) {
                val uCategoryName = encodingClass.decodeData(u.categoryName)

                if (uCategoryName.toLowerCase().startsWith(categoryName.toLowerCase())) {
                    userCategoryItemValue.add(
                            encodingClass.decodeData(u.categoryId) + uCategoryName
                    )
                    userCategoryName.add(uCategoryName)
                    userNumberOfPlatforms.add(
                            (databaseHandlerClass.viewNumberOfPlatforms(u.categoryId)).toString()
                    )
                }
            }

            val categoriesPlatformsListAdapter = CategoriesPlatformsListAdapter(
                    attActivity,
                    "Platforms:",
                    userCategoryItemValue,
                    userCategoryName,
                    userNumberOfPlatforms
            )

            lvAccountsContainer.adapter = categoriesPlatformsListAdapter
        }
    }

    @SuppressLint("ShowToast")
    fun addOrUpdateCategory(
            addOrUpdate: String,
            categoryName: String,
            selectedCategoryId: String,
            selectedCategoryName: String
    ) {
        val userCategory: List<UserCategoryModelClass> =
                databaseHandlerClass.viewCategory("", "")
        var categoryId = 10001
        var existing = false
        var toast: Toast? = null

        if (!userCategory.isNullOrEmpty()) {
            for (u in userCategory) {
                if (
                        categoryName.equals(
                                encodingClass.decodeData(u.categoryName),
                                ignoreCase = true
                        ) &&
                        !categoryName.equals(selectedCategoryName, ignoreCase = true)
                ) {
                    existing = true
                    break
                }

                categoryId = Integer.parseInt(encodingClass.decodeData(u.categoryId)) + 1
            }
        }

        if (!existing) {
            if (addOrUpdate == "add") {
                val status = databaseHandlerClass.addCategory(                                      // Add Category
                        UserCategoryModelClass(
                                encodingClass.encodeData(categoryId.toString()),
                                encodingClass.encodeData(categoryName)
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Category '$categoryName' added!",
                            Toast.LENGTH_SHORT
                    )
                }
            } else if (addOrUpdate == "update") {
                val status = databaseHandlerClass.updateCategory(                                   // Update Category
                        UserCategoryModelClass(
                                encodingClass.encodeData(selectedCategoryId),
                                encodingClass.encodeData(categoryName)
                        )
                )

                if (status > -1) {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Category updated to '$categoryName'",
                            Toast.LENGTH_SHORT
                    )
                }
            }

            populateCategories("")
            llAccountsNoItem.removeAllViews()
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.accounts_new_category_alert_existing,
                    Toast.LENGTH_SHORT
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        closeKeyboard()
    }
}