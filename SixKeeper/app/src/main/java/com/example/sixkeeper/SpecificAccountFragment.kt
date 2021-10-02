package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class SpecificAccountFragment : Fragment() {
    private val args: SpecificAccountFragmentArgs by navArgs()

    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

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
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
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

    @SuppressLint("SetTextI18n")
    private fun populateAccountData() {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val userAccount: List<UserAccountModelClass> =
                databaseHandlerClass.viewAccount(encodingClass.encodeData(args.specificPlatformId))
        var platformName = ""
        var categoryId = ""
        var categoryName = ""

        val tvSpecificAccName: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccName)
        val tvSpecificAccCategoryPlatform: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCategoryPlatform)
        val tvSpecificAccCredentialField: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredentialField)
        val tvSpecificAccCredential: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccCredential)
        val tvSpecificAccPassword: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccPassword)
        val tvSpecificAccWebsiteURL: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccWebsiteURL)
        val tvSpecificAccDescription: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccDescription)
        val tvSpecificAccFavorites: TextView =
                appCompatActivity.findViewById(R.id.tvSpecificAccFavorites)

        for (u in userAccount) {
            if (args.specificAccountId == encodingClass.decodeData(u.accountId)) {
                tvSpecificAccName.text = encodingClass.decodeData(u.accountName)
                tvSpecificAccCredentialField.text = encodingClass.decodeData(u.accountCredentialField)
                tvSpecificAccCredential.text = encodingClass.decodeData(u.accountCredential)
                tvSpecificAccPassword.text = encodingClass.decodeData(u.accountPassword)
                tvSpecificAccWebsiteURL.text = encodingClass.decodeData(u.accountWebsiteURL)
                tvSpecificAccDescription.text = encodingClass.decodeData(u.accountDescription)

                if (encodingClass.decodeData(u.accountIsFavorites) == "1") {
                    tvSpecificAccFavorites.text = "Yes"
                } else {
                    tvSpecificAccFavorites.text = "No"
                }
            }
        }

        val userPlatform: List<UserPlatformModelClass> = databaseHandlerClass.viewPlatform(
                "platform",
                encodingClass.encodeData(args.specificPlatformId)
        )

        for (u in userPlatform) {
            platformName = encodingClass.decodeData(u.platformName)
            categoryId = encodingClass.decodeData(u.categoryId)
        }

        val userCategory: List<UserCategoryModelClass> = databaseHandlerClass.viewCategory(
                "category",
                encodingClass.encodeData(categoryId)
        )

        for (u in userCategory) {
            categoryName = encodingClass.decodeData(u.categoryName)
        }

        tvSpecificAccCategoryPlatform.text = "$categoryName > $platformName"
    }

    private fun setOnClick() {
        val clSpecificAccEdit: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSpecificAccEdit)

        clSpecificAccEdit.setOnClickListener {
            val action = SpecificAccountFragmentDirections
                    .actionSpecificAccountFragmentToAddAccountFragment(
                            args.specificPlatformId,
                            "edit",
                            args.specificAccountId
                    )
            findNavController().navigate(action)
        }
    }
}