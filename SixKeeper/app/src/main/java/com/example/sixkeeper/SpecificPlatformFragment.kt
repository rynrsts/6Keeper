package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class SpecificPlatformFragment : Fragment() {
    private val args: SpecificPlatformFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var etSpecificPlatSearchBox: EditText
    private lateinit var lvSpecificPlatContainer: ListView

    private lateinit var selectedAccountId: String
    private lateinit var selectedAccountName: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_platform, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setActionBarTitle()
        closeKeyboard()
        populateAccounts("")
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etSpecificPlatSearchBox = appCompatActivity.findViewById(R.id.etSpecificPlatSearchBox)
        lvSpecificPlatContainer = appCompatActivity.findViewById(R.id.lvSpecificPlatContainer)
    }

    private fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = args.specificPlatformName
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

    @SuppressLint("DefaultLocale")
    private fun populateAccounts(accountName: String) {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "platformId",
                encodingClass.encodeData(args.specificPlatformId)
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)

        for (u in userAccount) {
            val uAccountName = encodingClass.decodeData(u.accountName)

            if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
                userAccountId.add(encodingClass.decodeData(u.accountId) + uAccountName)
                userAccountName.add(uAccountName)
            }
        }

        val accountsListAdapter = AccountsListAdapter(
                attActivity,
                userAccountId,
                userAccountName
        )

        lvSpecificPlatContainer.adapter = accountsListAdapter
    }

    private fun setOnClick() {
        val ivSpecificPlatSearchButton: ImageView =
                appCompatActivity.findViewById(R.id.ivSpecificPlatSearchButton)
        val ivSpecificPlatAddAccounts: ImageView =
                appCompatActivity.findViewById(R.id.ivSpecificPlatAddAccounts)

        ivSpecificPlatSearchButton.setOnClickListener {
            val search = etSpecificPlatSearchBox.text.toString()

            populateAccounts(search)
            closeKeyboard()
        }

        ivSpecificPlatAddAccounts.setOnClickListener {
            val action = SpecificPlatformFragmentDirections
                    .actionSpecificPlatformFragmentToAddAccountFragment(
                            args.specificPlatformId,
                            "add",
                            ""
                    )
            findNavController().navigate(action)
        }

        lvSpecificPlatContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedAccount = lvSpecificPlatContainer.getItemAtPosition(i).toString()
            selectedAccountId = selectedAccount.substring(0, 6)
            selectedAccountName = selectedAccount.substring(6, selectedAccount.length)

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
                                val action = SpecificPlatformFragmentDirections
                                        .actionSpecificPlatformFragmentToSpecificAccountFragment(
                                                selectedAccountId,
                                                selectedAccountName,
                                                args.specificPlatformId
                                        )
                                findNavController().navigate(action)

                                etSpecificPlatSearchBox.setText("")
                            }, 250
                    )
                }
            }
        }
    }
}