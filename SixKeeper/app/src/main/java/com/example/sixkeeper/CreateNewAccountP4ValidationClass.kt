package com.example.sixkeeper

import android.content.DialogInterface
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        return tvCreateNewAccP4MasterPinMes.text.toString() == getString(R.string.many_setup_complete)
    }

    fun isTermsChecked(): Boolean {
        return cbCreateNewAccP4AgreeToTerms.isChecked
    }

    fun showTermsOrPrivacy(dialogView: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())

        builder.apply {
            setView(dialogView)
            setCancelable(false)
        }
        builder.setNegativeButton("Ok") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.apply {
            window?.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                            context, R.drawable.layout_alert_dialog
                    )
            )
            show()
        }
    }
}