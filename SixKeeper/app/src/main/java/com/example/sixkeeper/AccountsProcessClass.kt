package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class AccountsProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etAccountsSearchBox: EditText
    private lateinit var lvAccountsContainer: ListView

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

        etAccountsSearchBox = getAppCompatActivity().findViewById(R.id.etAccountsSearchBox)
        lvAccountsContainer = appCompatActivity.findViewById(R.id.lvAccountsContainer)
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

    @SuppressLint("DefaultLocale")
    fun populateCategories(categoryName: String) {
        val userCategory: List<UserCategoryModelClass> = databaseHandlerClass.viewCategory()
        val userCategoryIdName = ArrayList<String>(0)
        val userCategoryName = ArrayList<String>(0)
        val userNumberOfPlatforms = ArrayList<String>(0)

        for (u in userCategory) {
            val uCategoryName = encodingClass.decodeData(u.categoryName)

            if (uCategoryName.toLowerCase().startsWith(categoryName.toLowerCase())) {
                userCategoryIdName.add(encodingClass.decodeData(u.categoryId) + uCategoryName)
                userCategoryName.add(uCategoryName)
                userNumberOfPlatforms.add(
                        (databaseHandlerClass.viewNumberOfPlatforms(u.categoryId)).toString()
                )
            }
        }

        val categoriesPlatformsListAdapter = CategoriesPlatformsListAdapter(
                attActivity,
                userCategoryIdName,
                userCategoryName,
                userNumberOfPlatforms
        )

        lvAccountsContainer.adapter = categoriesPlatformsListAdapter
    }

    @SuppressLint("ShowToast")
    fun addNewCategory(categoryName: String) {                                                      // Add new category
        val userCategory: List<UserCategoryModelClass> = databaseHandlerClass.viewCategory()
        var categoryId = 10001
        var existing = false
        var toast: Toast? = null

        if (!userCategory.isNullOrEmpty()) {
            for (u in userCategory) {
                if (
                        categoryName.equals(
                                encodingClass.decodeData(u.categoryName),
                                ignoreCase = true
                        )
                ) {
                    existing = true
                    break
                }

                categoryId = Integer.parseInt(encodingClass.decodeData(u.categoryId)) + 1
            }
        }

        if (!existing) {
            val status = databaseHandlerClass.addCategory(
                    UserCategoryModelClass(
                            encodingClass.encodeData(categoryId.toString()),
                            encodingClass.encodeData(categoryName)
                    )
            )

            if (status > -1) {
                toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        "Category '$categoryName' added!",
                        Toast.LENGTH_LONG
                )

                populateCategories("")
            }
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.accounts_new_category_alert_existing,
                    Toast.LENGTH_LONG
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}