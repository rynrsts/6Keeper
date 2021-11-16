package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FavoritesFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var lvFavoritesContainer: ListView
    private lateinit var etFavoritesSearchBox: EditText

    private lateinit var key: ByteArray
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
        disableMenuItem()
        closeKeyboard()
        populateAccounts("")
        setOnClick()
        setOnLongClick()
        setEditTextOnChange()
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

        lvFavoritesContainer = appCompatActivity.findViewById(R.id.lvFavoritesContainer)
        etFavoritesSearchBox = appCompatActivity.findViewById(R.id.etFavoritesSearchBox)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
    }

    private fun disableMenuItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = true
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = false
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
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

    @SuppressLint("DefaultLocale", "InflateParams")
    private fun populateAccounts(accountName: String) {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "accountIsFavorites",
                encryptionClass.encrypt(1.toString(), key),
                encryptionClass.encrypt(0.toString(), key)
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountDirectory = ArrayList<String>(0)

        if (userAccount.isNullOrEmpty()) {
            val llFavoritesNoItem: LinearLayout =
                    appCompatActivity.findViewById(R.id.llFavoritesNoItem)
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvAccountsNoItem.setText(R.string.many_no_account)
            lvFavoritesContainer.adapter = null
            llFavoritesNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userAccount) {
                val uAccountName = encryptionClass.decrypt(u.accountName, key)

                if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
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
            }

            val accountsListAdapter = FavoritesListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName,
                    userAccountDirectory
            )

            lvFavoritesContainer.adapter = accountsListAdapter
        }
    }

    private fun setOnClick() {
        lvFavoritesContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            if (InternetConnectionClass().isConnected()) {
                lvFavoritesContainer.apply {
                    lvFavoritesContainer.isEnabled = false                                          // Set un-clickable for 1 second

                    val selectedAccount = lvFavoritesContainer.getItemAtPosition(i).toString()
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

                    postDelayed(
                            {
                                lvFavoritesContainer.isEnabled = true
                            }, 1000
                    )
                }
            } else {
                internetToast()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun setOnLongClick() {                                                                  // Set item long click
        lvFavoritesContainer.onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedAccount = lvFavoritesContainer.getItemAtPosition(pos).toString()
            val selectedAccountValue = selectedAccount.split("ramjcammjar")
            selectedAccountId = selectedAccountValue[0]
            selectedAccountName = selectedAccountValue[1]
            selectedPlatformId = selectedAccountValue[2]

            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_favorites, null)
            val ivAccountsIcon: ImageView = dialogView.findViewById(R.id.ivAccountsIcon)
            val tvAccountsText: TextView = dialogView.findViewById(R.id.tvAccountsText)
            val llAccountsFavorites: LinearLayout =
                    dialogView.findViewById(R.id.llAccountsFavorites)
            val llAccountsDelete: LinearLayout =  dialogView.findViewById(R.id.llAccountsDelete)

            ivAccountsIcon.setImageResource(R.drawable.ic_star_outline_light_black)
            tvAccountsText.setText(R.string.specific_account_remove)
            llAccountsDelete.removeAllViews()
            builder.setView(dialogView)

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                context, R.drawable.layout_alert_dialog
                        )
                )
                setTitle("Account: $selectedAccountName")
                show()
            }

            closeKeyboard()

            llAccountsFavorites.setOnClickListener {                                                // Remove from Favorites
                if (InternetConnectionClass().isConnected()) {
                    val status = databaseHandlerClass.updateIsFavorites(
                            encryptionClass.encrypt(selectedAccountId, key),
                            encryptionClass.encrypt(0.toString(), key)
                    )

                    if (status > -1) {
                        val toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                "Account '$selectedAccountName' removed from favorites!",
                                Toast.LENGTH_SHORT
                        )
                        toast?.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }

                    var actionLogId = 1000001
                    val lastId = databaseHandlerClass.getLastIdOfActionLog()

                    val calendar: Calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                    val date: String = dateFormat.format(calendar.time)

                    if (lastId.isNotEmpty()) {
                        actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
                    }

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encryptionClass.encrypt(actionLogId.toString(), key),
                                    encryptionClass.encrypt("Account " +
                                            "'$selectedAccountName' was removed from Favorites.",
                                            key),
                                    encryptionClass.encrypt(date, key)
                            )
                    )

                    alert.cancel()
                    populateAccounts("")
                } else {
                    internetToast()
                }
            }

            true
        })
    }

    private fun setEditTextOnChange() {                                                             // Search real-time
        etFavoritesSearchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (InternetConnectionClass().isConnected()) {
                    val search = etFavoritesSearchBox.text.toString()
                    populateAccounts(search)
                } else {
                    internetToast()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun internetToast() {
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