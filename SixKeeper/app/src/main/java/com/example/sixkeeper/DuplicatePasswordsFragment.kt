package com.example.sixkeeper

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class DuplicatePasswordsFragment : Fragment() {
    private val args: DuplicatePasswordsFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var lvDuplicatePasswordsContainer: ListView

    private lateinit var key: ByteArray
    private lateinit var selectedAccountId: String
    private lateinit var selectedAccountName: String
    private lateinit var selectedPlatformId: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_duplicate_passwords, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        populateDuplicatePasswords()
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
        encryptionClass = EncryptionClass()

        lvDuplicatePasswordsContainer =
                appCompatActivity.findViewById(R.id.lvDuplicatePasswordsContainer)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
    }

    private fun populateDuplicatePasswords() {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "password",
                args.encodedDuplicatePassword,
                encryptionClass.encrypt(0.toString(), key)
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)

        for (u in userAccount) {
            val uAccountName = encryptionClass.decrypt(u.accountName, key)
            val uPlatformName = encryptionClass.decrypt(u.platformName, key)
            val uCategoryName = encryptionClass.decrypt(u.categoryName, key)

            userAccountId.add(
                    encryptionClass.decrypt(u.accountId, key) + "ramjcammjar" +
                            uAccountName + "ramjcammjar" +
                            encryptionClass.decrypt(u.platformId, key)
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

        lvDuplicatePasswordsContainer.adapter = accountsListAdapter
    }

    private fun setOnClick() {
        lvDuplicatePasswordsContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            if (InternetConnectionClass().isConnected()) {
                val selectedAccount = lvDuplicatePasswordsContainer.getItemAtPosition(i).toString()
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
                                val action = DuplicatePasswordsFragmentDirections
                                        .actionDuplicatePasswordsFragmentToSpecificAccountFragment(
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