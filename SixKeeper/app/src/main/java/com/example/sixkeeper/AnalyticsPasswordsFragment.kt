package com.example.sixkeeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

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

        if (args.badPasswordType == "weak") {
            tAppBarToolbar.title = getString(R.string.dashboard_weak_passwords)
            populateWeakPasswords()
        }
    }

    private fun populateWeakPasswords() {
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

    private fun setOnClick() {
        lvAnalyticsPasswordsContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedAccount = lvAnalyticsPasswordsContainer.getItemAtPosition(i).toString()
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