package com.example.sixkeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResetSelectionFragment : Fragment() {
    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setOnclick()
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
    }

    private fun setOnclick() {
        val acbResetSelectionEnterInfo: Button =
                appCompatActivity.findViewById(R.id.acbResetSelectionEnterInfo)
        val acbResetSelectionMobileNumber: Button =
                appCompatActivity.findViewById(R.id.acbResetSelectionMobileNumber)
        val acbResetSelectionEmail: Button =
                appCompatActivity.findViewById(R.id.acbResetSelectionEmail)

        val forgotCredentialsActivity: ForgotCredentialsActivity =
                activity as ForgotCredentialsActivity

        acbResetSelectionEnterInfo.setOnClickListener {
            forgotCredentialsActivity.manageForgotFragments("securityQuestion")
        }

        acbResetSelectionMobileNumber.setOnClickListener {
            forgotCredentialsActivity.manageForgotFragments("mobileNumberValidation")
        }
    }
}