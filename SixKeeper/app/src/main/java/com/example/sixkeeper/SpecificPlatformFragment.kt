package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SpecificPlatformFragment : Fragment() {
    private val args: SpecificPlatformFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etSpecificPlatSearchBox: EditText
    private lateinit var lvSpecificPlatContainer: ListView

    private lateinit var selectedAccountId: String
    private lateinit var selectedAccountName: String
    private lateinit var selectedAccountIsFavorites: String
    private lateinit var accountIdTemp: String
    private lateinit var accountNameTemp: String
    private var setIsFavorites: Int = 0
    private lateinit var clickAction: String

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
        setHierarchy()
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

        etSpecificPlatSearchBox = appCompatActivity.findViewById(R.id.etSpecificPlatSearchBox)
        lvSpecificPlatContainer = appCompatActivity.findViewById(R.id.lvSpecificPlatContainer)
    }

    private fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = getString(R.string.many_accounts)
    }

    private fun setHierarchy() {
        val tvSpecificPlatHierarchy: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificPlatHierarchy)
        val titleText = args.specificCategoryName + " > " + args.specificPlatformName
        tvSpecificPlatHierarchy.text = titleText
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
                "platformId",
                encodingClass.encodeData(args.specificPlatformId),
                encodingClass.encodeData(0.toString())
        )
        val userAccountId = ArrayList<String>(0)
        val userAccountName = ArrayList<String>(0)
        val userAccountIsFavorites = ArrayList<String>(0)

        if (userAccount.isNullOrEmpty()) {
            val llSpecificPlatNoItem: LinearLayout =
                    appCompatActivity.findViewById(R.id.llSpecificPlatNoItem)
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvAccountsNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvAccountsNoItem.setText(R.string.many_no_account)
            lvSpecificPlatContainer.adapter = null
            llSpecificPlatNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userAccount) {
                val uAccountName = encodingClass.decodeData(u.accountName)

                if (uAccountName.toLowerCase().startsWith(accountName.toLowerCase())) {
                    userAccountId.add(
                            encodingClass.decodeData(u.accountId) + "ramjcammjar" +
                                    uAccountName + "ramjcammjar" +
                                    encodingClass.decodeData(u.accountIsFavorites)
                    )
                    userAccountName.add(uAccountName)
                    userAccountIsFavorites.add(encodingClass.decodeData(u.accountIsFavorites))
                }
            }

            val accountsListAdapter = AccountsListAdapter(
                    attActivity,
                    userAccountId,
                    userAccountName,
                    userAccountIsFavorites
            )

            lvSpecificPlatContainer.adapter = accountsListAdapter
        }
    }

    private fun setOnClick() {
        val ivSpecificPlatAddAccounts: ImageView =
                appCompatActivity.findViewById(R.id.ivSpecificPlatAddAccounts)

        ivSpecificPlatAddAccounts.setOnClickListener {
            val action = SpecificPlatformFragmentDirections
                    .actionSpecificPlatformFragmentToAddAccountFragment(
                            args.specificPlatformId,
                            args.specificPlatformName,
                            args.specificCategoryName,
                            "add",
                            ""
                    )
            findNavController().navigate(action)
        }

        lvSpecificPlatContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            lvSpecificPlatContainer.apply {
                lvSpecificPlatContainer.isEnabled = false                                           // Set un-clickable for 1 second

                val selectedAccount = lvSpecificPlatContainer.getItemAtPosition(i).toString()
                val selectedAccountValue = selectedAccount.split("ramjcammjar")
                selectedAccountId = selectedAccountValue[0]
                selectedAccountName = selectedAccountValue[1]
                selectedAccountIsFavorites = selectedAccountValue[2]
                clickAction = "View Account"

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
                            lvSpecificPlatContainer.isEnabled = true
                        }, 1000
                )
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun setOnLongClick() {                                                                  // Set item long click
        lvSpecificPlatContainer.onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedAccount = lvSpecificPlatContainer.getItemAtPosition(pos).toString()
            val selectedAccountValue = selectedAccount.split("ramjcammjar")
            selectedAccountId = selectedAccountValue[0]
            selectedAccountName = selectedAccountValue[1]
            selectedAccountIsFavorites = selectedAccountValue[2]
            accountIdTemp = selectedAccountId
            accountNameTemp = selectedAccountName

            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_favorites, null)
            val ivAccountsIcon: ImageView = dialogView.findViewById(R.id.ivAccountsIcon)
            val tvAccountsText: TextView = dialogView.findViewById(R.id.tvAccountsText)
            var message = ""
            var actionMessage = ""

            if (selectedAccountIsFavorites == "1") {
                ivAccountsIcon.setImageResource(R.drawable.ic_star_outline_light_black)
                tvAccountsText.setText(R.string.specific_account_remove)
                setIsFavorites = 0
                message = "Account '$selectedAccountName' removed from favorites!"
                actionMessage = "Account '$selectedAccountName' was removed from Favorites."
            } else if (selectedAccountIsFavorites == "0") {
                ivAccountsIcon.setImageResource(R.drawable.ic_star_yellow)
                tvAccountsText.setText(R.string.specific_account_appear)
                setIsFavorites = 1
                message = "Account '$selectedAccountName' added in favorites!"
                actionMessage = "Account '$selectedAccountName' was added in Favorites."
            }

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

            val llAccountsFavorites: LinearLayout =
                    dialogView.findViewById(R.id.llAccountsFavorites)
            val llAccountsDelete: LinearLayout =
                    dialogView.findViewById(R.id.llAccountsDelete)
                                                                                                    // Appear in or remove from Favorites
            llAccountsFavorites.setOnClickListener {
                val status = databaseHandlerClass.updateIsFavorites(
                        encodingClass.encodeData(selectedAccountId),
                        encodingClass.encodeData(setIsFavorites.toString())
                )

                if (status > -1) {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            message,
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData(actionMessage),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )

                alert.cancel()
                populateAccounts("")
            }
                                                                                                    // Delete Account
            llAccountsDelete.setOnClickListener {
                alert.cancel()
                showDelete()
            }

            true
        })
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

    private fun showDelete() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
        builder.setMessage(R.string.specific_account_delete)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
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

            clickAction = "Delete Account"
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                if (clickAction == "View Account") {
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
                } else if (clickAction == "Delete Account") {
                    val status = databaseHandlerClass.updateDeleteAccount(
                            encodingClass.encodeData(accountIdTemp),
                            encodingClass.encodeData(1.toString()),
                            encodingClass.encodeData(getCurrentDate()),
                            "AccountsTable",
                            "account_id",
                            "account_deleted",
                            "account_delete_date"
                    )

                    if (status > -1) {
                        val toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                "Account '$accountNameTemp' deleted!",
                                Toast.LENGTH_SHORT
                        )
                        toast?.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encodingClass.encodeData(getLastActionLogId().toString()),
                                    encodingClass.encodeData(
                                            "Account '$accountNameTemp' was deleted."
                                    ),
                                    encodingClass.encodeData(getCurrentDate())
                            )
                    )

                    populateAccounts("")
                }
            }
        }
    }

    private fun setEditTextOnChange() {                                                             // Search real-time
        etSpecificPlatSearchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val search = etSpecificPlatSearchBox.text.toString()
                populateAccounts(search)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}