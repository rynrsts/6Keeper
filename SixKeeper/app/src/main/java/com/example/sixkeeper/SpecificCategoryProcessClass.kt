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
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

open class SpecificCategoryProcessClass : Fragment() {
    private val args: SpecificCategoryFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etSpecificCatSearchBox: EditText
    private lateinit var lvSpecificCatContainer: ListView

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
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

        etSpecificCatSearchBox = getAppCompatActivity().findViewById(R.id.etSpecificCatSearchBox)
        lvSpecificCatContainer = appCompatActivity.findViewById(R.id.lvSpecificCatContainer)
    }

    fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = args.specificCategoryName
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
    fun populatePlatforms(platformName: String) {
        val userPlatform: List<UserPlatformModelClass> =
                databaseHandlerClass.viewPlatform(encodingClass.encodeData(args.specificCategoryId))
        var size = 0

        for (u in userPlatform) {
            val uPlatformName = encodingClass.decodeData(u.platformName)

            if (uPlatformName.toLowerCase().startsWith(platformName.toLowerCase())) {
                size++
            }
        }

        val userPlatformId = Array(size) { "0" }
        val userPlatformName = Array(size) { "0" }
        val userNumberOfAccounts = Array(size) { "0" }
        var index = 0

        for (u in userPlatform) {
            val uPlatformName = encodingClass.decodeData(u.platformName)

            if (uPlatformName.toLowerCase().startsWith(platformName.toLowerCase())) {
                userPlatformId[index] = encodingClass.decodeData(u.platformId) + uPlatformName
                userPlatformName[index] = uPlatformName
                userNumberOfAccounts[index] =
                        (databaseHandlerClass.viewNumberOfAccounts(u.categoryId)).toString()

                index++
            }
        }

        val categoriesPlatformsListAdapter = CategoriesPlatformsListAdapter(
                attActivity,
                userPlatformId,
                userPlatformName,
                userNumberOfAccounts
        )

        lvSpecificCatContainer.adapter = categoriesPlatformsListAdapter
    }

    @SuppressLint("ShowToast")
    fun addNewPlatform(platformName: String) {                                                      // Add new platform
        val userPlatform: List<UserPlatformModelClass> =
                databaseHandlerClass.viewPlatform(encodingClass.encodeData(args.specificCategoryId))
        var platformId = 10001
        var existing = false
        var toast: Toast? = null

        if (!userPlatform.isNullOrEmpty()) {
            for (u in userPlatform) {
                if (
                        platformName.equals(
                                encodingClass.decodeData(u.platformName),
                                ignoreCase = true
                        )
                ) {
                    existing = true
                    break
                }

                platformId = Integer.parseInt(encodingClass.decodeData(u.platformId)) + 1
            }
        }

        if (!existing) {
            val status = databaseHandlerClass.addPlatform(
                    UserPlatformModelClass(
                            encodingClass.encodeData(platformId.toString()),
                            encodingClass.encodeData(platformName),
                            encodingClass.encodeData(args.specificCategoryId)
                    )
            )

            if (status > -1) {
                toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        "Platform '$platformName' added!",
                        Toast.LENGTH_LONG
                )

                populatePlatforms("")
            }
        } else {
            toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    R.string.specific_category_new_platform_alert_existing,
                    Toast.LENGTH_LONG
            )
        }

        toast?.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}