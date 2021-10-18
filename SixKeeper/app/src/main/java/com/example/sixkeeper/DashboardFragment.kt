package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class DashboardFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass

    private lateinit var llDashboardContainer: LinearLayout

    private lateinit var selectedTab: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            postDelayed(
                    {
                        setVariables()
                        closeKeyboard()
                        populateSummary()
                        setOnClick()
                    }, 10
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {                                            // To override onBackPressed in fragment
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {                                                    // Override back press
                val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                builder.setMessage(R.string.many_exit_mes)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    appCompatActivity.finish()
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title_confirm)
                alert.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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

        llDashboardContainer = appCompatActivity.findViewById(R.id.llDashboardContainer)

        selectedTab = "summary"
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

    @SuppressLint("InflateParams")
    private fun populateSummary() {
        val inflatedLayout = layoutInflater.inflate(
                R.layout.layout_dashboard_summary,
                null,
                true
        )

        val tvDashboardCategories: TextView =
                inflatedLayout.findViewById(R.id.tvDashboardCategories)
        val tvDashboardPlatforms: TextView = inflatedLayout.findViewById(R.id.tvDashboardPlatforms)
        val tvDashboardAccounts: TextView = inflatedLayout.findViewById(R.id.tvDashboardAccounts)
        val tvDashboardFavorites: TextView = inflatedLayout.findViewById(R.id.tvDashboardFavorites)
        val tvDashboardSavedPasswords: TextView =
                inflatedLayout.findViewById(R.id.tvDashboardSavedPasswords)
        val tvDashboardDeletedAccounts: TextView =
                inflatedLayout.findViewById(R.id.tvDashboardDeletedAccounts)
        val tvDashboardDeletedPasswords: TextView =
                inflatedLayout.findViewById(R.id.tvDashboardDeletedPasswords)

        tvDashboardCategories.text = databaseHandlerClass.viewTotalNumberDashboard1(
                "CategoriesTable"
        ).toString()
        tvDashboardPlatforms.text = databaseHandlerClass.viewTotalNumberDashboard1(
                "PlatformsTable"
        ).toString()
        tvDashboardAccounts.text = databaseHandlerClass.viewTotalNumberDashboard2(
                "AccountsTable",
                "account_deleted",
                encodingClass.encodeData("0"),
                ""
        ).toString()
        tvDashboardFavorites.text = databaseHandlerClass.viewTotalNumberDashboard2(
                "AccountsTable",
                "account_deleted",
                encodingClass.encodeData("0"),
                encodingClass.encodeData("1")
        ).toString()
        tvDashboardSavedPasswords.text = databaseHandlerClass.viewTotalNumberDashboard2(
                "SavedPasswordTable",
                "pass_deleted",
                encodingClass.encodeData("0"),
                ""
        ).toString()
        tvDashboardDeletedAccounts.text = databaseHandlerClass.viewTotalNumberDashboard2(
                "AccountsTable",
                "account_deleted",
                encodingClass.encodeData("1"),
                ""
        ).toString()
        tvDashboardDeletedPasswords.text = databaseHandlerClass.viewTotalNumberDashboard2(
                "SavedPasswordTable",
                "pass_deleted",
                encodingClass.encodeData("1"),
                ""
        ).toString()

        llDashboardContainer.removeAllViews()
        llDashboardContainer.addView(inflatedLayout)
    }

    private fun setOnClick() {
        val clDashboardSummary: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clDashboardSummary)
        val clDashboardAnalytics: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clDashboardAnalytics)
        val clDashboardActionLog: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clDashboardActionLog)

        val vDashboardDivision1: View = appCompatActivity.findViewById(R.id.vDashboardDivision1)
        val vDashboardDivision2: View = appCompatActivity.findViewById(R.id.vDashboardDivision2)
        val vDashboardDivision3: View = appCompatActivity.findViewById(R.id.vDashboardDivision3)

        clDashboardSummary.setOnClickListener {
            if (selectedTab == "analytics") {
                vDashboardDivision2.setBackgroundResource(R.color.white)
            } else if (selectedTab == "action log") {
                vDashboardDivision3.setBackgroundResource(R.color.white)
            }

            vDashboardDivision1.setBackgroundResource(R.color.blue)
            populateSummary()
            selectedTab = "summary"
        }

        clDashboardAnalytics.setOnClickListener {
            if (selectedTab == "summary") {
                vDashboardDivision1.setBackgroundResource(R.color.white)
            } else if (selectedTab == "action log") {
                vDashboardDivision3.setBackgroundResource(R.color.white)
            }

            vDashboardDivision2.setBackgroundResource(R.color.blue)
            populateAnalytics()
            selectedTab = "analytics"
        }

        clDashboardActionLog.setOnClickListener {
            if (selectedTab == "summary") {
                vDashboardDivision1.setBackgroundResource(R.color.white)
            } else if (selectedTab == "analytics") {
                vDashboardDivision2.setBackgroundResource(R.color.white)
            }

            vDashboardDivision3.setBackgroundResource(R.color.blue)
            populateActionLog()
            selectedTab = "action log"
        }
    }

    @SuppressLint("InflateParams")
    private fun populateAnalytics() {
        val inflatedLayout = layoutInflater.inflate(
                R.layout.layout_dashboard_analytics,
                null,
                true
        )

        llDashboardContainer.removeAllViews()
        llDashboardContainer.addView(inflatedLayout)
    }

    private fun populateActionLog() {
        llDashboardContainer.removeAllViews()
    }
}