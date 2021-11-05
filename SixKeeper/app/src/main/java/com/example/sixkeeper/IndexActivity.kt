package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class IndexActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView
    private lateinit var headerView: View
    private lateinit var clNavigationHeader: ConstraintLayout

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

    private var userId = ""
    private var backgroundDate = ""
    private var status = "unlocked"
    private var start = true
    private var timer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setVariables()
        populate()
        setUsername()
        setProfilePhoto()
        setOnClick()
    }

    override fun onStart() {
        super.onStart()
        start = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)                                                      // Stop
    fun onAppBackgrounded() {
        backgroundDate = getCurrentDate()
        start = true
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)                                                    // Resume
    fun onAppForegrounded() {
        if (!start && status == "unlocked") {
            if (backgroundDate.isNotEmpty()) {
                val dateToday: Date = dateFormat.parse(getCurrentDate())
                val dateBackground: Date = dateFormat.parse(backgroundDate)
                val timeDifference: Long = dateToday.time - dateBackground.time

                val databaseHandlerClass = DatabaseHandlerClass(this)
                val encodingClass = EncodingClass()
                val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()
                var autoLock = false

                for (u in userSettings) {
                    if (encodingClass.decodeData(u.autoLock) == "1") {                              // If Auto Lock is 1
                        autoLock = true

                        if (timer == 0) {
                            val autoLockTimer = encodingClass.decodeData(u.autoLockTimer)
                            timer = Integer.parseInt(
                                    autoLockTimer.replace(" sec", "")
                            )
                        }
                    }
                }

                if (autoLock) {
                    if (TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= timer) {
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

    fun setTimer(i: Int) {
        timer = i
    }

    private fun setVariables() {
        databaseHandlerClass = DatabaseHandlerClass(this)
        encodingClass = EncodingClass()

        firebaseDatabase = FirebaseDatabase.getInstance()
        val userAccList = databaseHandlerClass.validateUserAcc()
        userId = ""

        for (u in userAccList) {
            userId = encodingClass.decodeData(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)
    }

    private fun populate() {                                                                        // Populate menu and fragments
        val toolbar: Toolbar = findViewById(R.id.tAppBarToolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.dlIndexDrawerLayout)
        navigationView = findViewById(R.id.nvIndexNavigationView)
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
        headerView = navigationView.getHeaderView(0)
        clNavigationHeader = headerView.findViewById(R.id.clNavigationHeader)

        val button = Button(this)

        val usernameRef = databaseReference.child("username")
        var username = ""
        var count = 0

        usernameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                username = dataSnapshot.getValue(String::class.java).toString()
                count++

                if (count == 1) {
                    button.performClick()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val tvNavigationHeaderUsername: TextView =
                    headerView.findViewById(R.id.tvNavigationHeaderUsername)
            val decodedUsername = encodingClass.decodeData(username)

            tvNavigationHeaderUsername.text = decodedUsername
        }
    }

    private fun setProfilePhoto() {
        val ivNavigationHeaderPhoto: ImageView =
                headerView.findViewById(R.id.ivNavigationHeaderPhoto)
        val button = Button(this)

        val profilePhotoRef = databaseReference.child("profilePhoto")
        var profilePhoto = ""
        var count = 0

        profilePhotoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                profilePhoto = dataSnapshot.getValue(String::class.java).toString()
                count++

                if (count == 1) {
                    button.performClick()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val profilePhotoB = encodingClass.decodeByteArray(profilePhoto)

            if (profilePhoto.isNotEmpty()) {
                val imageDrawable: Drawable = BitmapDrawable(
                        resources,
                        BitmapFactory.decodeByteArray(
                                profilePhotoB,
                                0,
                                profilePhotoB.size
                        )
                )

                ivNavigationHeaderPhoto.setImageDrawable(imageDrawable)
            } else {
                ivNavigationHeaderPhoto.setImageDrawable(null)
            }
        }
    }

    private fun setOnClick() {
        clNavigationHeader.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                drawerLayout.closeDrawers()

                findNavController(R.id.fIndexNavigationHost).navigate(                              // Go to User Account
                        R.id.action_fragments_to_userAccountFragment
                )
            } else {
                val toast: Toast = Toast.makeText(
                        applicationContext,
                        R.string.many_internet_connection,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 1215311 && resultCode == 1215311 -> {                                    // If Master PIN is correct
                status = "unlocked"
                timer = 0
            }
            requestCode == 1215311 && resultCode == 31143512 -> {                                   // If canceled
                val homeClick = Intent(Intent.ACTION_MAIN)
                homeClick.addCategory(Intent.CATEGORY_HOME)
                startActivity(homeClick)
            }
        }
    }
}