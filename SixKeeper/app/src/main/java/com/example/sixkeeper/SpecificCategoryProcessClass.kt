package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

open class SpecificCategoryProcessClass : Fragment() {
    private val args: SpecificCategoryFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var lvSpecificCatContainer: ListView

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()

        lvSpecificCatContainer = appCompatActivity.findViewById(R.id.lvSpecificCatContainer)
    }

    fun populatePlatforms() {
        val userPlatform: List<UserPlatformModelClass> =
                databaseHandlerClass.viewPlatform(encodingClass.encodeData(args.specificCategoryId))
        val userPlatformId = Array(userPlatform.size) { "0" }
        val userPlatformName = Array(userPlatform.size) { "0" }
        val userNumberOfAccounts = Array(userPlatform.size) { "0" }

        for ((index, u) in userPlatform.withIndex()) {
            userPlatformId[index] = encodingClass.decodeData(u.platformId)
            userPlatformName[index] = encodingClass.decodeData(u.platformName)
            userNumberOfAccounts[index] =
                    (databaseHandlerClass.viewNumberOfAccounts(u.categoryId)).toString()
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
        val encodingClass = EncodingClass()
        val userPlatform: List<UserPlatformModelClass> =
                databaseHandlerClass.viewPlatform(encodingClass.encodeData(args.specificCategoryId))
        var platformId = 0
        var existing = false
        var toast: Toast? = null

        if (userPlatform.isNullOrEmpty()) {
            platformId = 10001
        } else {
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

                populatePlatforms()
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
}