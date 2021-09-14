package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView

class SettingsFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
        closeKeyboard()
        setOnClick()
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    appCompatActivity.currentFocus?.windowToken,
                    0
            )
        }
    }

    private fun setOnClick() {
        val navigationView: NavigationView =
            appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val tvNavigationHeaderUsername: TextView =
            headerView.findViewById(R.id.tvNavigationHeaderUsername)
        val ivNavigationHeaderPhoto: ImageView =
            headerView.findViewById(R.id.ivNavigationHeaderPhoto)

        tvNavigationHeaderUsername.setOnClickListener {
            goToUserAccount()
        }

        ivNavigationHeaderPhoto.setOnClickListener {
            goToUserAccount()
        }
    }

    private fun goToUserAccount() {
        val drawerLayout: DrawerLayout = appCompatActivity.findViewById(R.id.dlIndexDrawerLayout)
        drawerLayout.closeDrawers()

        findNavController().navigate(                                                               // Go to User Account
            R.id.action_settingsFragment_to_userAccountFragment
        )
    }
}