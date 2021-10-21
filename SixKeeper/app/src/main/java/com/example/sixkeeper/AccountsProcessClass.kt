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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class AccountsProcessClass : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etAccountsSearchBox: EditText
    private lateinit var lvAccountsContainer: ListView
    private lateinit var llAccountsNoItem: LinearLayout

    private lateinit var categoryIdTemp: String
    private lateinit var categoryNameTemp: String

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
            lvAccountsContainer.adapter = null
            llAccountsNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userCategory) {
                val uCategoryName = encodingClass.decodeData(u.categoryName)
                val uNumOfPlatforms =
                        databaseHandlerClass.viewNumberOfPlatforms(u.categoryId).toString()

                if (uCategoryName.toLowerCase().startsWith(categoryName.toLowerCase())) {
                    userCategoryItemValue.add(
                            encodingClass.decodeData(u.categoryId) + "ramjcammjar" +
                                    uCategoryName + "ramjcammjar" + uNumOfPlatforms
                    )
                    userCategoryName.add(uCategoryName)
                    userNumberOfPlatforms.add(uNumOfPlatforms)
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

    @SuppressLint("InflateParams")
    fun showAddUpdateCategory(
            addOrUpdate: String,
            selectedCategoryName: String,
            selectedCategoryId: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_accounts_add_new, null)

        builder.apply {
            setView(dialogView)
            setCancelable(false)
        }

        val tvAccountsAddNew: TextView = dialogView.findViewById(R.id.tvAccountsAddNew)
        val etAccountsAddNew: EditText = dialogView.findViewById(R.id.etAccountsAddNew)
        val ivAccountsAddNew: ImageView = dialogView.findViewById(R.id.ivAccountsAddNew)

        tvAccountsAddNew.setText(R.string.accounts_category_name)
        ivAccountsAddNew.setImageResource(R.drawable.ic_format_list_bulleted_light_black)

        var buttonName = ""
        var dialogTitle = ""
        var toastMes = ""

        if (addOrUpdate == "add") {
            buttonName = "Add"
            dialogTitle = resources.getString(R.string.accounts_new_category)
            toastMes = resources.getString(R.string.many_nothing_to_add)
        } else if (addOrUpdate == "update") {
            buttonName = "Update"
            dialogTitle = resources.getString(R.string.accounts_edit_category) + ": " +
                    selectedCategoryName
            toastMes = resources.getString(R.string.many_nothing_to_update)

            etAccountsAddNew.apply {
                setText(selectedCategoryName)
                setSelection(etAccountsAddNew.text.length)
            }
        }

        builder.setPositiveButton(buttonName) { _: DialogInterface, _: Int ->
            val categoryName = etAccountsAddNew.text.toString()

            if (categoryName.isNotEmpty()) {
                addOrUpdateCategory(
                        addOrUpdate,
                        categoryName,
                        selectedCategoryId,
                        selectedCategoryName
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
                            getAppCompatActivity(), R.drawable.layout_alert_dialog
                    )
            )
            setTitle(dialogTitle)
            show()
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

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData("Category '$categoryName' was added."),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )
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
                            "Category '$selectedCategoryName' changed to '$categoryName'",
                            Toast.LENGTH_SHORT
                    )
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData("Category '$selectedCategoryName'" +
                                        " was changed to '$categoryName'."),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )
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

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    fun showDeleteCategory(
            selectedCategoryId: String,
            selectedCategoryName: String,
            selectedCategoryNum: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)

        val toastMessage = if (selectedCategoryNum == "0") {
            "Delete category?"
        } else {
            "Selected category is not empty. Deleting it will also delete its contents. Delete anyway?"
        }

        builder.apply {
            setMessage(toastMessage)
            setCancelable(false)
        }

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            categoryIdTemp = selectedCategoryId
            categoryNameTemp = selectedCategoryName

            if (selectedCategoryNum == "0") {
                deleteCategory()
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

    @SuppressLint("SimpleDateFormat")
    private fun deleteCategory() {                                                                  // Delete Category
        val deleteCategoryStatus = databaseHandlerClass.removeCatPlatAcc(
                "CategoriesTable",
                "category_id",
                encodingClass.encodeData(categoryIdTemp)
        )

        if (deleteCategoryStatus > -1 ) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    "Category '$categoryNameTemp' deleted!",
                    Toast.LENGTH_SHORT
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encodingClass.encodeData(getLastActionLogId().toString()),
                        encodingClass.encodeData("Category '$categoryNameTemp' was deleted."),
                        encodingClass.encodeData(getCurrentDate())
                )
        )

        populateCategories("")
    }

    @SuppressLint("SimpleDateFormat")
    private fun deleteCategoryContents() {                                                          // Delete Categories' contents
        val userPlatform: List<UserPlatformModelClass> = databaseHandlerClass.viewPlatform(
                "category",
                encodingClass.encodeData(categoryIdTemp)
        )
        val userPlatformId = ArrayList<String>(0)

        for (u in userPlatform) {
            userPlatformId.add(u.platformId)
        }

        val linkedHashSet: LinkedHashSet<String> = LinkedHashSet(userPlatformId)
        var userPlatformIdNoDuplicate = arrayOfNulls<String>(linkedHashSet.size)
        userPlatformIdNoDuplicate = userPlatformId.toArray(userPlatformIdNoDuplicate)

        databaseHandlerClass.removeCatPlatAcc(
                "PlatformsTable",
                "category_id",
                encodingClass.encodeData(categoryIdTemp)
        )
        databaseHandlerClass.updateDeleteMultipleAccount(
                userPlatformIdNoDuplicate,
                encodingClass.encodeData(1.toString()),
                encodingClass.encodeData(getCurrentDate()),
                "AccountsTable",
                "platform_id",
                "account_deleted",
                "account_delete_date"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                deleteCategory()
                deleteCategoryContents()
            }
        }
    }
}