package com.example.sixkeeper

import android.content.Context
import android.net.ConnectivityManager

class DetectInternetConnectionClass {

    @Suppress("DEPRECATION")
    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return (
                connectivityManager.activeNetworkInfo != null &&
                        connectivityManager.activeNetworkInfo!!.isAvailable &&
                        connectivityManager.activeNetworkInfo!!.isConnected
                )
    }
}