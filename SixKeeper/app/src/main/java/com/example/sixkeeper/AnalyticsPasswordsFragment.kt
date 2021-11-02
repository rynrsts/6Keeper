package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AnalyticsPasswordsFragment : Fragment() {
    private val args: AnalyticsPasswordsFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var lvAnalyticsPasswordsContainer: ListView

    private lateinit var selectedAccountId: String
    private lateinit var selectedAccountName: String
    private lateinit var selectedPlatformId: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analytics_passwords, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        populateView()
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

        lvAnalyticsPasswordsContainer =
                appCompatActivity.findViewById(R.id.lvAnalyticsPasswordsContainer)
    }

    private fun populateView() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)

        when (args.badPasswordType) {
            "weak" -> {
                tAppBarToolbar.title = getString(R.string.dashboard_weak_passwords)
                populateWeakPasswords()
            }
            "old" -> {
                tAppBarToolbar.title = getString(R.string.dashboard_old_passwords)
                populateOldPasswords()
            }
            "duplicate" -> {
                tAppBarToolbar.title = getString(R.string.dashboard_duplicate_passwords)
                populateDuplicatePasswords()
            }
        }
    }

    private fun populateWeakPasswords() {                                                           // Weak passwords
        val userWeakAccount: List<UserAccountModelClass> =
                databaseHandlerClass.viewWeakAccounts(encodingClass.encodeData("weak"))
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)

        for (u in userWeakAccount) {
            val uAccountName = encodingClass.decodeData(u.accountName)
            val uPlatformName = encodingClass.decodeData(u.platformName)
            val uCategoryName = encodingClass.decodeData(u.categoryName)

            userAccountId.add(
                    encodingClass.decodeData(u.accountId) + "ramjcammjar" +
                            uAccountName + "ramjcammjar" +
                            encodingClass.decodeData(u.platformId)
            )
            userAccountName.add(uAccountName)
            userAccountDirectory.add("$uCategoryName > $uPlatformName")
        }

        val accountsListAdapter = AnalyticsWeakPasswordsListAdapter(
                attActivity,
                userAccountId,
                userAccountName,
                userAccountDirectory
        )

        lvAnalyticsPasswordsContainer.adapter = accountsListAdapter
    }

    @SuppressLint("SimpleDateFormat")
    private fun populateOldPasswords() {                                                            // Old passwords
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "deleted",
                "",
                encodingClass.encodeData(0.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)
        val userAccountDate = ArrayList<String>(0)

        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val calendar: Calendar = Calendar.getInstance()
        val date: String = dateFormat.format(calendar.time)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val dateToday: Date = dateFormat.parse(date)

        for (u in userAccount) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val creationDate: Date = dateFormat.parse(encodingClass.decodeData(u.creationDate))
            val timeDifference: Long = dateToday.time - creationDate.time

            if (TimeUnit.MILLISECONDS.toDays(timeDifference) >= 90) {
                val uAccountName = encodingClass.decodeData(u.accountName)
                val uPlatformName = encodingClass.decodeData(u.platformName)
                val uCategoryName = encodingClass.decodeData(u.categoryName)

                userAccountId.add(
                        encodingClass.decodeData(u.accountId) + "ramjcammjar" +
                                uAccountName + "ramjcammjar" +
                                encodingClass.decodeData(u.platformId)
                )
                userAccountName.add(uAccountName)
                userAccountDirectory.add("$uCategoryName > $uPlatformName")
                userAccountDate.add(encodingClass.decodeData(u.creationDate))
            }
        }

        val accountsListAdapter = AnalyticsOldPasswordsListAdapter(
                attActivity,
                userAccountId,
                userAccountName,
                userAccountDirectory,
                userAccountDate
        )

        lvAnalyticsPasswordsContainer.adapter = accountsListAdapter
    }

    private fun populateDuplicatePasswords() {                                                      // Duplicate passwords
        val userDuplicateAccount = databaseHandlerClass.getDuplicateAccountsCount()
        val userAccountDuplicateId = ArrayList<String>(0)
        val userAccountCount = ArrayList<String>(0)
        var duplicateId = 1001

        for (u in userDuplicateAccount) {
            userAccountDuplicateId.add(u.accountPassword)
            userAccountCount.add(u.count)
            duplicateId++
        }

        val accountsListAdapter = AnalyticsDuplicatePasswordsListAdapter(
                attActivity,
                userAccountDuplicateId,
                userAccountCount,
        )

        lvAnalyticsPasswordsContainer.adapter = accountsListAdapter
    }

    private fun setOnClick() {
        lvAnalyticsPasswordsContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            if (InternetConnectionClass().isConnected()) {
                if (args.badPasswordType == "weak" || args.badPasswordType == "old") {
                    val selectedAccount =
                            lvAnalyticsPasswordsContainer.getItemAtPosition(i).toString()
                    val selectedAccountValue = selectedAccount.split("ramjcammjar")
                    selectedAccountId = selectedAccountValue[0]
                    selectedAccountName = selectedAccountValue[1]
                    selectedPlatformId = selectedAccountValue[2]

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
                } else if (args.badPasswordType == "duplicate") {
                    val selectedDuplicatePassword =
                            lvAnalyticsPasswordsContainer.getItemAtPosition(i).toString()

                    val action = AnalyticsPasswordsFragmentDirections
                            .actionAnalyticsPasswordsFragmentToDuplicatePasswordsFragment(
                                    selectedDuplicatePassword
                            )
                    findNavController().navigate(action)
                }
            } else {
                val toast: Toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.many_internet_connection,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                view?.apply {
                    postDelayed(
                            {
                                val action = AnalyticsPasswordsFragmentDirections
                                        .actionAnalyticsPasswordsFragmentToSpecificAccountFragment(
                                                selectedAccountId,
                                                selectedAccountName,
                                                selectedPlatformId
                                        )
                                findNavController().navigate(action)
                            }, 250
                    )
                }
            }
        }
    }
}