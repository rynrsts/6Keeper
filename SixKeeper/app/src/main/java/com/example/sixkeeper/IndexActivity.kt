package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class IndexActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView
    private lateinit var clNavigationHeader: ConstraintLayout
    private lateinit var tvNavigationHeaderUsername: TextView

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private var backgroundDate = ""
    private var status = "unlocked"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        populate()
        setUsername()
        setOnClick()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)                                                      // Stop
    fun onAppBackgrounded() {
        backgroundDate = getCurrentDate()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)                                                    // Resume
    fun onAppForegrounded() {
        if (status == "unlocked") {
            if (backgroundDate.isNotEmpty()) {
                val dateToday: Date = dateFormat.parse(getCurrentDate())
                val dateBackground: Date = dateFormat.parse(backgroundDate)
                val timeDifference: Long = dateToday.time - dateBackground.time

                val databaseHandlerClass =  DatabaseHandlerClass(this)
                val encodingClass = EncodingClass()
                val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()
                var autoLock = false
                var timer = 0

                for (u in userSettings) {
                    if (encodingClass.decodeData(u.autoLock) == "1") {                              // If Auto Lock is 1
                        autoLock = true

                        val autoLockTimer = encodingClass.decodeData(u.autoLockTimer)
                        timer = Integer.parseInt(
                            autoLockTimer.replace(" sec", "")
                        )
                    }
                }

                if (autoLock) {
                    if (TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= 1) {
                        status = "locked"

                        val goToAutoLockLoginActivity = Intent(
                            this,
                            AutoLockLoginActivity::class.java
                        )

                        @Suppress("DEPRECATION")
                        startActivityForResult(goToAutoLockLoginActivity, 1215311)
                        overridePendingTransition(
                            R.anim.anim_enter_bottom_to_top_2,
                            R.anim.anim_0
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    fun setBackgroundDate() {
        backgroundDate = ""
    }

    private fun populate() {                                                                        // Populate menu and fragments
        val toolbar: Toolbar = findViewById(R.id.tAppBarToolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.dlIndexDrawerLayout)
        val navigationView: NavigationView = findViewById(R.id.nvIndexNavigationView)
        val navigationController = findNavController(R.id.fIndexNavigationHost)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userAccountFragment,
                R.id.dashboardFragment,
                R.id.accountsFragment,
                R.id.favoritesFragment,
                R.id.passwordGeneratorFragment,
                R.id.recycleBinFragment,
                R.id.settingsFragment,
                R.id.aboutUsFragment,
//                        R.id.helpFragment,
                R.id.termsConditionsFragment,
                R.id.privacyPolicyFragment,
//                        R.id.contactUsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        navigationView.setupWithNavController(navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fIndexNavigationHost)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUsername() {                                                                     // Show username in the navigation header
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val encodingClass = EncodingClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var username = ""

        for (u in userAccList) {
            username = encodingClass.decodeData(u.username)
        }

        navigationView = findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        clNavigationHeader = headerView.findViewById(R.id.clNavigationHeader)
        tvNavigationHeaderUsername = headerView.findViewById(R.id.tvNavigationHeaderUsername)

        tvNavigationHeaderUsername.text = username
    }

    private fun setOnClick() {
        clNavigationHeader.setOnClickListener {
            drawerLayout.closeDrawers()

            findNavController(R.id.fIndexNavigationHost).navigate(                                  // Go to User Account
                R.id.action_fragments_to_userAccountFragment
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 1215311 && resultCode == 1215311 -> {                                    // If Master PIN is correct
                status = "unlocked"
            }
            requestCode == 1215311 && resultCode == 31143512 -> {                                   // If canceled
                val homeClick = Intent(Intent.ACTION_MAIN)
                homeClick.addCategory(Intent.CATEGORY_HOME)
                startActivity(homeClick)
            }
        }
    }
}