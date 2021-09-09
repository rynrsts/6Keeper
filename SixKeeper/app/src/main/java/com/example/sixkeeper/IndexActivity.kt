package com.example.sixkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class IndexActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        val toolbar: Toolbar = findViewById(R.id.tAppBarToolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.dlIndexDrawerLayout)
        val navigationView: NavigationView = findViewById(R.id.nvIndexNavigationView)
        val navigationController = findNavController(R.id.fIndexNavigationHost)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment,
                R.id.accountsFragment,
                R.id.favoritesFragment,
                R.id.passwordGeneratorFragment,
                R.id.recycleBinFragment,
                R.id.settingsFragment,
                R.id.aboutUsFragment,
                R.id.helpFragment,
                R.id.termsConditionsFragment,
                R.id.contactUsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        navigationView.setupWithNavController(navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fIndexNavigationHost)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}