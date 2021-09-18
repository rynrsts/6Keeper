package com.example.sixkeeper

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView
    private lateinit var tvNavigationHeaderUsername: TextView
    private lateinit var ivNavigationHeaderPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)


        populate()
        setUsername()
        setOnClick()
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
        tvNavigationHeaderUsername = headerView.findViewById(R.id.tvNavigationHeaderUsername)
        ivNavigationHeaderPhoto = headerView.findViewById(R.id.ivNavigationHeaderPhoto)

        tvNavigationHeaderUsername.text = username
//        ivNavigationHeaderPhoto.setImageResource(R.drawable.ic_visibility_gray)
    }

    private fun setOnClick() {
        tvNavigationHeaderUsername.setOnClickListener {
            goToUserAccount()
        }

        ivNavigationHeaderPhoto.setOnClickListener {
            goToUserAccount()
        }
    }

    private fun goToUserAccount() {
        drawerLayout.closeDrawers()

        findNavController(R.id.fIndexNavigationHost).navigate(                                      // Go to User Account
                R.id.action_fragments_to_userAccountFragment
        )
    }

//    TODO: Show dialog box when in dashboard and back button was clicked
//    override fun onBackPressed() {                                                                  // Override back button function
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setMessage(R.string.many_exit_mes)
//        builder.setCancelable(false)
//
//        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
//            super.onBackPressed()
//        }
//        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
//            dialog.cancel()
//        }
//
//        val alert: AlertDialog = builder.create()
//        alert.setTitle(R.string.many_alert_title)
//        alert.show()
//    }
}