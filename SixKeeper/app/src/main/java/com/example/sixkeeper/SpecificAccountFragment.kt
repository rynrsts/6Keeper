package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*

class SpecificAccountFragment : Fragment() {
    private val args: SpecificAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var etSpecificAccPassword: EditText

    private var passwordVisibility: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setActionBarTitle()
        closeKeyboard()
        populateAccountData()
        setImageViewOnClick()
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

        etSpecificAccPassword = appCompatActivity.findViewById(R.id.etSpecificAccPassword)
    }

    private fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = args.specificAccountName
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

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun populateAccountData() {
        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(
                "platformId",
                encodingClass.encodeData(args.specificPlatformId),
                encodingClass.encodeData(0.toString())
        )
        var platformName = ""
        var categoryName = ""

        val tvSpecificAccName: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccName)
        val tvSpecificAccCategoryPlatform: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCategoryPlatform)
        val tvSpecificAccCredentialField: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredentialField)
        val tvSpecificAccCredential: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredential)
        val tvSpecificAccWebsiteURL: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccWebsiteURL)
        val tvSpecificAccDescription: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccDescription)
        val llSpecificAccFavorites: LinearLayout =
                appCompatActivity.findViewById(R.id.llSpecificAccFavorites)

        for (u in userAccount) {
            if (args.specificAccountId == encodingClass.decodeData(u.accountId)) {
                tvSpecificAccName.text = encodingClass.decodeData(u.accountName)
                tvSpecificAccCredentialField.text =
                        encodingClass.decodeData(u.accountCredentialField)
                tvSpecificAccCredential.text = encodingClass.decodeData(u.accountCredential)
                etSpecificAccPassword.setText(encodingClass.decodeData(u.accountPassword))
                tvSpecificAccWebsiteURL.text = encodingClass.decodeData(u.accountWebsiteURL)
                tvSpecificAccDescription.text = encodingClass.decodeData(u.accountDescription)

                if (encodingClass.decodeData(u.accountIsFavorites) == "1") {
                    val inflatedView = layoutInflater.inflate(
                            R.layout.layout_favorites_star,
                            null,
                            true
                    )
                    llSpecificAccFavorites.addView(inflatedView)
                }

                platformName = encodingClass.decodeData(u.platformName)
                categoryName = encodingClass.decodeData(u.categoryName)
            }
        }

        tvSpecificAccCategoryPlatform.text = "$categoryName > $platformName"
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        val ivSpecificAccTogglePass: ImageView =
                appCompatActivity.findViewById(R.id.ivSpecificAccTogglePass)

        ivSpecificAccTogglePass.setOnClickListener {
            when (passwordVisibility) {
                0 -> {                                                                              // Show password
                    ivSpecificAccTogglePass.setImageResource(R.drawable.ic_visibility_light_black)
                    etSpecificAccPassword.transformationMethod = null
                    passwordVisibility = 1
                }
                1 -> {                                                                              // Hide password
                    ivSpecificAccTogglePass.setImageResource(
                            R.drawable.ic_visibility_off_light_black
                    )
                    etSpecificAccPassword.transformationMethod = PasswordTransformationMethod()
                    passwordVisibility = 0
                }
            }
        }
    }

    private fun setOnClick() {
        val clSpecificAccEdit: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSpecificAccEdit)
        val clSpecificAccDelete: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSpecificAccDelete)

        clSpecificAccEdit.setOnClickListener {
            val action = SpecificAccountFragmentDirections
                    .actionSpecificAccountFragmentToAddAccountFragment(
                            args.specificPlatformId,
                            "",
                            "",
                            "edit",
                            args.specificAccountId,
                    )
            findNavController().navigate(action)
        }

        clSpecificAccDelete.setOnClickListener {
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
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title_confirm)
            alert.show()

            it.apply {
                clSpecificAccDelete.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed(
                        {
                            clSpecificAccDelete.isClickable = true
                        }, 1000
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
            view?.apply {
                val calendar: Calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                val date: String = dateFormat.format(calendar.time)

                val status = databaseHandlerClass.updateDeleteAccount(
                        encodingClass.encodeData(args.specificAccountId),
                        encodingClass.encodeData(1.toString()),
                        encodingClass.encodeData(date),
                        "AccountsTable",
                        "account_id",
                        "account_deleted",
                        "account_delete_date"
                )

                if (status > -1) {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            "Account '${args.specificAccountName}' deleted!",
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                var actionLogId = 1000001
                val lastId = databaseHandlerClass.getLastIdOfActionLog()

                if (lastId.isNotEmpty()) {
                    actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encodingClass.encodeData(actionLogId.toString()),
                                encodingClass.encodeData(
                                        "Account '${args.specificAccountName}' was deleted."
                                ),
                                encodingClass.encodeData(date)
                        )
                )

                postDelayed(
                        {
                            appCompatActivity.onBackPressed()
                        }, 250
                )
            }
        }
    }
}