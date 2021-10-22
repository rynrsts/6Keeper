package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

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
    private fun populateSummary() {                                                                 // Populate summary tab
        val layoutSummary = layoutInflater.inflate(
                R.layout.layout_dashboard_summary,
                null,
                true
        )
        layoutSummary.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val tvDashboardCategories: TextView =
                layoutSummary.findViewById(R.id.tvDashboardCategories)
        val tvDashboardPlatforms: TextView = layoutSummary.findViewById(R.id.tvDashboardPlatforms)
        val tvDashboardAccounts: TextView = layoutSummary.findViewById(R.id.tvDashboardAccounts)
        val tvDashboardFavorites: TextView = layoutSummary.findViewById(R.id.tvDashboardFavorites)
        val tvDashboardSavedPasswords: TextView =
                layoutSummary.findViewById(R.id.tvDashboardSavedPasswords)
        val tvDashboardDeletedAccounts: TextView =
                layoutSummary.findViewById(R.id.tvDashboardDeletedAccounts)
        val tvDashboardDeletedPasswords: TextView =
                layoutSummary.findViewById(R.id.tvDashboardDeletedPasswords)

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

        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_dashboard_scroll_view,
                null,
                true
        )
        layoutContainer.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val llDashboardSummaryAnalytics: LinearLayout =
                layoutContainer.findViewById(R.id.llDashboardSummaryAnalytics)

        llDashboardSummaryAnalytics.apply {
            removeAllViews()
            addView(layoutSummary)
        }
        llDashboardContainer.apply {
            removeAllViews()
            addView(layoutContainer)
        }
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

    @SuppressLint("InflateParams", "SimpleDateFormat", "SetTextI18n")
    private fun populateAnalytics() {                                                               // Populate analytics tab
        val layoutAnalytics = layoutInflater.inflate(
                R.layout.layout_dashboard_analytics,
                null,
                true
        )
        layoutAnalytics.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val pbDashboardAnalytics: ProgressBar =
                layoutAnalytics.findViewById(R.id.pbDashboardAnalytics)
        val tvDashboardAnalytics: TextView = layoutAnalytics.findViewById(R.id.tvDashboardAnalytics)
        val tvDashboardSecurityStatus: TextView =
                layoutAnalytics.findViewById(R.id.tvDashboardSecurityStatus)
        val clDashboardWeakPasswords: ConstraintLayout =
                layoutAnalytics.findViewById(R.id.clDashboardWeakPasswords)
        val tvDashboardNumOfWeak: TextView = layoutAnalytics.findViewById(R.id.tvDashboardNumOfWeak)
        val clDashboardOldPasswords: ConstraintLayout =
                layoutAnalytics.findViewById(R.id.clDashboardOldPasswords)
        val tvDashboardNumOfOld: TextView = layoutAnalytics.findViewById(R.id.tvDashboardNumOfOld)
        val clDashboardDuplicatePasswords: ConstraintLayout =
                layoutAnalytics.findViewById(R.id.clDashboardDuplicatePasswords)
        val tvDashboardNumOfDuplicate: TextView =
                layoutAnalytics.findViewById(R.id.tvDashboardNumOfDuplicate)

        val weakPasswords = databaseHandlerClass.viewTotalNumberOfWeakPasswords(                    // Start | Show number of weak passwords
                encodingClass.encodeData(0.toString()),
                encodingClass.encodeData("weak")
        )

        val userAccount: List<UserAccountModelClass> = databaseHandlerClass.viewAccount(            // Start | Show number of old passwords
                "deleted",
                "",
                encodingClass.encodeData(0.toString())
        )
        var oldPasswords = 0
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val calendar: Calendar = Calendar.getInstance()
        val date: String = dateFormat.format(calendar.time)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val dateToday: Date = dateFormat.parse(date)

        for (u in userAccount) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val creationDate: Date = dateFormat.parse(encodingClass.decodeData(u.creationDate))
            val timeDifference: Long = dateToday.time - creationDate.time

            if (TimeUnit.MILLISECONDS.toDays(timeDifference) >= 90) {
                oldPasswords++
            }
        }

        var numOfDuplicates = 0                                                                     // Start | Show number of duplicate passwords
        val duplicates = databaseHandlerClass.viewTotalNumberOfDuplicatePasswords(
                encodingClass.encodeData(0.toString())
        )

        if (duplicates.toString().isNotEmpty()) {
            numOfDuplicates = duplicates
        }

        val numberOfAccounts: Double = databaseHandlerClass.viewTotalNumberDashboard2(
                "AccountsTable",
                "account_deleted",
                encodingClass.encodeData("0"),
                ""
        ).toDouble()
        var score = 0

        if (numberOfAccounts > 0) {
            val accountsTimesThree: Double = numberOfAccounts * 3
            val percentage: Double = ((accountsTimesThree -
                    (weakPasswords + oldPasswords + numOfDuplicates).toDouble()) /
                    accountsTimesThree) * 100
            score = percentage.roundToInt()
        }

        pbDashboardAnalytics.progress = score
        tvDashboardAnalytics.text = "$score%"
        tvDashboardNumOfWeak.text = weakPasswords.toString()
        tvDashboardNumOfOld.text = oldPasswords.toString()
        tvDashboardNumOfDuplicate.text = numOfDuplicates.toString()

        when (score) {
            in 1..70 -> {
                tvDashboardSecurityStatus.text = "Bad"
            }
            in 71..85 -> {
                tvDashboardSecurityStatus.text = "Good"
            }
            in 86..100 -> {
                tvDashboardSecurityStatus.text = "Excellent"
            }
        }

        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_dashboard_scroll_view,
                null,
                true
        )
        layoutContainer.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val llDashboardSummaryAnalytics: LinearLayout =
                layoutContainer.findViewById(R.id.llDashboardSummaryAnalytics)

        llDashboardSummaryAnalytics.apply {
            removeAllViews()
            addView(layoutAnalytics)
        }
        llDashboardContainer.apply {
            removeAllViews()
            addView(layoutContainer)
        }

        clDashboardWeakPasswords.setOnClickListener {
            if (weakPasswords > 0) {
                val action = DashboardFragmentDirections
                        .actionDashboardFragmentToAnalyticsPasswordsFragment("weak")
                findNavController().navigate(action)
            } else {
                val toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.dashboard_weak_passwords_mes,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun populateActionLog() {
        val userActionLog: List<UserActionLogModelClass> = databaseHandlerClass.viewActionLog()
        val userActionLogId = ArrayList<String>(0)
        val userActionLogDescription = ArrayList<String>(0)
        val userActionLogDate = ArrayList<String>(0)

        for (u in userActionLog) {
            userActionLogId.add(encodingClass.decodeData(u.actionLogId))
            userActionLogDescription.add(encodingClass.decodeData(u.actionLogDescription))
            userActionLogDate.add(encodingClass.decodeData(u.actionLogDate))
        }

        val actionLogListAdapter = ActionLogListAdapter(
                attActivity,
                userActionLogId,
                userActionLogDescription,
                userActionLogDate
        )

        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_dashboard_list_view,
                null,
                true
        )
        layoutContainer.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val lvDashboardActionLog: ListView =
                layoutContainer.findViewById(R.id.lvDashboardActionLog)

        lvDashboardActionLog.adapter = actionLogListAdapter
        llDashboardContainer.apply {
            removeAllViews()
            addView(layoutContainer)
        }
    }
}