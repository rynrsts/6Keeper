package com.example.sixkeeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        updateUserStatus()
        goToLoginActivity()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        attActivity = activity
    }

    private fun updateUserStatus() {
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

    private fun goToLoginActivity() {
        val goToLoginActivity = Intent(activity, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        activity?.overridePendingTransition(
            R.anim.anim_enter_bottom_to_top_2,
            R.anim.anim_0
        )
        this.activity?.finish()
    }
}