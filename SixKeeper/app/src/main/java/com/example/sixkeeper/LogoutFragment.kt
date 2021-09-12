package com.example.sixkeeper

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


class LogoutFragment : Fragment() {
    private lateinit var attActivity: Activity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun showDialog() {                                                                      // Show dialog for logout
        val builder: AlertDialog.Builder = AlertDialog.Builder(attActivity)
        builder.setMessage(R.string.many_logout_mes)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            updateUserStatus()
            goToLoginActivity()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            activity?.onBackPressed()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title)
        alert.show()
    }

    private fun updateUserStatus() {                                                                // Update account status to 0
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = 0
        val userUsername = ""
        val userPassword = ""
        val userMasterPIN = 0
        val userAccountStatus = 0

        for (u in userAccList) {
            userId = u.userId
        }

        databaseHandlerClass.updateUserStatus(
                UserAccModelClass(
                        userId,
                        userUsername,
                        userPassword,
                        userMasterPIN,
                        userAccountStatus
                )
        )
    }

    private fun goToLoginActivity() {                                                               // Go to login (Username and Password)
        val goToLoginActivity = Intent(activity, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        activity?.overridePendingTransition(
                R.anim.anim_enter_bottom_to_top_2,
                R.anim.anim_0
        )
        this.activity?.finish()
    }
}