package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

class FavoritesFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var lvFavoritesContainer: ListView
    private lateinit var etFavoritesSearchBox: EditText

    private lateinit var selectedAccountId: String
    private lateinit var selectedAccountName: String
    private lateinit var selectedPlatformId: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        populateAccounts("")
        setOnClick()
        setEditTextOnChange()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        lvFavoritesContainer = appCompatActivity.findViewById(R.id.lvFavoritesContainer)
        etFavoritesSearchBox = appCompatActivity.findViewById(R.id.etFavoritesSearchBox)
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
                "accountIsFavorites",
                encodingClass.encodeData("1"),
                encodingClass.encodeData(1.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)

        for (u in userAccount) {
            val uAccountName = encodingClass.decodeData(u.accountName)

            if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
                userAccountId.add(
                        encodingClass.decodeData(u.platformId) +
                                encodingClass.decodeData(u.accountId) + uAccountName
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

        val accountsListAdapter = FavoritesListAdapter(
                attActivity,
                userAccountId,
                userAccountName,
                userAccountDirectory
        )

        lvFavoritesContainer.adapter = accountsListAdapter
    }

    private fun setOnClick() {
        lvFavoritesContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedAccount = lvFavoritesContainer.getItemAtPosition(i).toString()
            selectedPlatformId = selectedAccount.substring(0, 5)
            selectedAccountId = selectedAccount.substring(5, 11)
            selectedAccountName = selectedAccount.substring(11, selectedAccount.length)

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

    private fun setEditTextOnChange() {                                                             // Search real-time
        etFavoritesSearchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val search = etFavoritesSearchBox.text.toString()
                populateAccounts(search)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
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
                                val action = FavoritesFragmentDirections
                                        .actionFavoritesFragmentToSpecificAccountFragment(
                                                selectedAccountId,
                                                selectedAccountName,
                                                selectedPlatformId
                                        )
                                findNavController().navigate(action)

                                etFavoritesSearchBox.setText("")
                            }, 250
                    )
                }
            }
        }
    }
}