package com.example.sixkeeper

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
//        val acbResetSelectionEmail: Button =
//                appCompatActivity.findViewById(R.id.acbResetSelectionEmail)

        val forgotCredentialsActivity: ForgotCredentialsActivity =
                activity as ForgotCredentialsActivity

        acbResetSelectionEnterInfo.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                forgotCredentialsActivity.manageForgotFragments("securityQuestion")
            } else {
                internetToast()
            }
        }

        acbResetSelectionMobileNumber.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                forgotCredentialsActivity.manageForgotFragments(
                        "mobileNumberValidation"
                )
            } else {
                internetToast()
            }
        }

//        acbResetSelectionEmail.setOnClickListener {
//            if (InternetConnectionClass().isConnected()) {
//                forgotCredentialsActivity.manageForgotFragments(
//                        "emailValidation"
//                )
//            } else {
//                internetToast()
//            }
//        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}