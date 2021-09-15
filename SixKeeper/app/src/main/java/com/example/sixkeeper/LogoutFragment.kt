package com.example.sixkeeper

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        closeKeyboard()
        showDialog()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                attActivity.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(attActivity.currentFocus?.windowToken, 0)           // Close keyboard
        }
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
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }

    private fun updateUserStatus() {                                                                // Update account status to 0
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = 0

        for (u in userAccList) {
            userId = Integer.parseInt(decodeData(u.userId))
        }

        databaseHandlerClass.updateUserStatus(
                encodeData(userId.toString()),
                encodeData(0.toString())
        )
    }

    private fun decodeData(data: String): String {                                                  // Decode data using Base64
        val decrypt = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        return String(decrypt)
    }

    private fun encodeData(data: String): String {                                                  // Encode data using Base64
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)
        return String(encrypt)
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