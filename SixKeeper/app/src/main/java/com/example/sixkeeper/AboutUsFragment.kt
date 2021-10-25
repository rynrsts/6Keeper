package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView

class AboutUsFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
        disableMenuItem()
        closeKeyboard()
        setOnClick()
    }

    private fun disableMenuItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = true
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = false
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
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

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val tvAboutUsTermsAndConditions: TextView =
                appCompatActivity.findViewById(R.id.tvAboutUsTermsAndConditions)
        val tvAboutUsPrivacyPolicy: TextView =
                appCompatActivity.findViewById(R.id.tvAboutUsPrivacyPolicy)

        tvAboutUsTermsAndConditions.setOnClickListener {
            findNavController().navigate(                                                           // Go to Terms and Conditions
                    R.id.action_aboutUsFragment_to_termsConditionsFragment
            )
        }

        tvAboutUsPrivacyPolicy.setOnClickListener {
            findNavController().navigate(                                                           // Go to Privacy Policy
                    R.id.action_aboutUsFragment_to_privacyPolicyFragment
            )
        }
    }
}