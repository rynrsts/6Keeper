package com.example.sixkeeper

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogoutFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        disableMenuItem()
        closeKeyboard()
        showDialog()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        firebaseDatabase = FirebaseDatabase.getInstance()
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
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = false
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

    private fun showDialog() {                                                                      // Show dialog for logout
        val builder: AlertDialog.Builder = AlertDialog.Builder(attActivity)
        builder.setMessage(R.string.many_logout_mes)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            if (InternetConnectionClass().isConnected()) {
                updateUserStatus()
                goToLoginActivity()
            } else {
                val toast: Toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.many_internet_connection,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
                activity?.onBackPressed()
            }
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
        val encodingClass = EncodingClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = u.userId
        }

        val decodedUserId = encodingClass.decodeData(userId)
        databaseReference = firebaseDatabase.getReference(decodedUserId)

        val encodedInactiveStatus = encodingClass.encodeData(0.toString())
        databaseReference.child("status").setValue(encodedInactiveStatus)
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