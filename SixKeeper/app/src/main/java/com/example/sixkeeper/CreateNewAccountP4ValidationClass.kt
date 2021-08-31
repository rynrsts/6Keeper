package com.example.sixkeeper

import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class CreateNewAccountP4ValidationClass : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var createNewAccountActivity: CreateNewAccountActivity

    private lateinit var tvCreateNewAccP4MasterPinMes: TextView
    private lateinit var cbCreateNewAccP4AgreeToTerms: CheckBox

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        createNewAccountActivity = activity as CreateNewAccountActivity

        tvCreateNewAccP4MasterPinMes =
                getAppCompatActivity().findViewById(R.id.tvCreateNewAccP4MasterPINMes)
        cbCreateNewAccP4AgreeToTerms =
                getAppCompatActivity().findViewById(R.id.cbCreateNewAccP4AgreeToTerms)
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getCreateNewAccountActivity(): CreateNewAccountActivity {
        return createNewAccountActivity
    }

    fun getTvCreateNewAccP4MasterPinMes(): TextView {
        return tvCreateNewAccP4MasterPinMes
    }

    fun isMasterPINSetup(): Boolean {
        return tvCreateNewAccP4MasterPinMes.text.toString() ==
                getString(R.string.many_setup_complete)
    }

    fun isTermsChecked(): Boolean {
        return cbCreateNewAccP4AgreeToTerms.isChecked
    }
}