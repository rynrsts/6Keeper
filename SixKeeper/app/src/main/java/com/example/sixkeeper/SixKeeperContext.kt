package com.example.sixkeeper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SixKeeperContext : Application() {
    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()

        setVariables()
        setupActivityListener()
    }

    private fun setVariables() {
        context = applicationContext
        FirebaseApp.initializeApp(applicationContext)
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                blockCapture(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun blockCapture(activity: Activity) {
        val databaseHandlerClass = DatabaseHandlerClass(applicationContext)
        val encryptionClass = EncryptionClass()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""
        val button = Button(context)

        if (userAccList.isEmpty()) {
            return
        }

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        val databaseReference = firebaseDatabase.getReference(userId)

        val statusRef = databaseReference.child("status")
        var status = ""
        var count = 0

        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    val decryptedValue = encryptionClass.decrypt(value, userId)
                    val selectedValue = decryptedValue.split("ramjcammjar")
                    status = selectedValue[0]
                    count++

                    if (count == 1) {
                        button.performClick()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()

            if (status == "1") {
                for (u in userSettings) {
                    if (encryptionClass.decrypt(u.screenCapture, userId) == "0") {
                        activity.window.setFlags(
                                WindowManager.LayoutParams.FLAG_SECURE,
                                WindowManager.LayoutParams.FLAG_SECURE
                        )
                    }
                }
            }
        }
    }
}