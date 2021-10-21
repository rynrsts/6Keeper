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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FavoritesFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

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
        encodingClass = EncodingClass()

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

    @SuppressLint("DefaultLocale", "InflateParams")
    private fun populateAccounts(accountName: String) {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "accountIsFavorites",
                encodingClass.encodeData(1.toString()),
                encodingClass.encodeData(0.toString())
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
                val uAccountName = encodingClass.decodeData(u.accountName)

                if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
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
                                                                                                    // Appear in or remove from Favorites
            llAccountsFavorites.setOnClickListener {
                val status = databaseHandlerClass.updateIsFavorites(
                        encodingClass.encodeData(selectedAccountId),
                        encodingClass.encodeData(0.toString())
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
                    actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(actionLogId.toString()),
                                encodingClass.encodeData("Account '$selectedAccountName' " +
                                        "was removed from Favorites."),
                                encodingClass.encodeData(date)
                        )
                )

                alert.cancel()
                populateAccounts("")
            }

            true
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