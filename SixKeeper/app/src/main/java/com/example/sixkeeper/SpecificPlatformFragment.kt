package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
    private var setIsFavorites: Int = 0

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

    private fun setHierarchy(){
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
                encodingClass.encodeData(args.specificPlatformId)
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
                            "add",
                            ""
                    )
            findNavController().navigate(action)
        }

//        TODO: Disable for 1 second on click
        lvSpecificPlatContainer.onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedAccount = lvSpecificPlatContainer.getItemAtPosition(i).toString()
//            selectedAccountId = selectedAccount.substring(0, 6)
//            selectedAccountName = selectedAccount.substring(6, selectedAccount.length)
            val selectedAccountValue = selectedAccount.split("ramjcammjar")
            selectedAccountId = selectedAccountValue[0]
            selectedAccountName = selectedAccountValue[1]
            selectedAccountIsFavorites = selectedAccountValue[2]

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

    @SuppressLint("InflateParams")
    private fun setOnLongClick() {                                                                  // Set item long click
        lvSpecificPlatContainer.onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedAccount = lvSpecificPlatContainer.getItemAtPosition(pos).toString()
            val selectedAccountValue = selectedAccount.split("ramjcammjar")
            selectedAccountId = selectedAccountValue[0]
            selectedAccountName = selectedAccountValue[1]
            selectedAccountIsFavorites = selectedAccountValue[2]

            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_favorites, null)
            val ivAccountsIcon: ImageView = dialogView.findViewById(R.id.ivAccountsIcon)
            val tvAccountsText: TextView = dialogView.findViewById(R.id.tvAccountsText)
            var message = ""

            if (selectedAccountIsFavorites == "1") {
                ivAccountsIcon.setImageResource(R.drawable.ic_star_outline_light_black)
                tvAccountsText.setText(R.string.specific_account_remove)
                setIsFavorites = 0
                message = "Account '$selectedAccountName' removed from favorites!"
            } else if (selectedAccountIsFavorites == "0") {
                ivAccountsIcon.setImageResource(R.drawable.ic_star_yellow)
                tvAccountsText.setText(R.string.specific_account_appear)
                setIsFavorites = 1
                message = "Account '$selectedAccountName' added in favorites!"
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

                alert.cancel()
                populateAccounts("")
            }

            true
        })
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