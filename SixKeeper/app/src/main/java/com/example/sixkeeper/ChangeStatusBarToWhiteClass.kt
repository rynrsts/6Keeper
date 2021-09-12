package com.example.sixkeeper

import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class ChangeStatusBarToWhiteClass : AppCompatActivity() {
    fun changeStatusBarColor() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {                                     // If android version is Marshmallow and above
                window.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    statusBarColor = ContextCompat.getColor(context, R.color.white)                 // Change status bar color to white

                    @Suppress("DEPRECATION")
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR             // Change the font color
                }
            }
        }
    }
}