package com.example.sixkeeper

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

class IndexActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView
    private lateinit var clNavigationHeader: ConstraintLayout
    private lateinit var tvNavigationHeaderUsername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        populate()
        setUsername()
        setOnClick()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
//        this.finish()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppForegrounded() {

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
}