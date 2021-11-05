package com.example.sixkeeper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SixKeeperContext : Application() {
    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        setupActivityListener()
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
        val encodingClass = EncodingClass()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""
        val button = Button(context)

        for (u in userAccList) {
            userId = u.userId
        }

        val decodedUserId = encodingClass.decodeData(userId)
        val databaseReference = firebaseDatabase.getReference(decodedUserId)

        val statusRef = databaseReference.child("status")
        var status = ""
        var count = 0

        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                status = encodingClass.decodeData(value)
                count++

                if (count == 1) {
                    button.performClick()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()

            if (status == "1") {
                for (u in userSettings) {
                    if (encodingClass.decodeData(u.screenCapture) == "0") {
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