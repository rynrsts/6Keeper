package com.example.sixkeeper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager

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
        if (getCaptureState()) {
            activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    private fun getCaptureState(): Boolean {
        val databaseHandlerClass = DatabaseHandlerClass(applicationContext)
        val encodingClass = EncodingClass()
        val userStatus: String = databaseHandlerClass.viewAccStatus()
        val userSettings: List<UserSettingsModelClass> = databaseHandlerClass.viewSettings()
        var blockCapture = false

        if (encodingClass.decodeData(userStatus) == "0") {
            blockCapture = false
        } else if(encodingClass.decodeData(userStatus) == "1") {
            for (u in userSettings) {
                if (encodingClass.decodeData(u.screenCapture) == "0") {
                    blockCapture = true
                } else if (encodingClass.decodeData(u.screenCapture) == "1") {
                    blockCapture = false
                }
            }
        }

        return blockCapture
    }
}