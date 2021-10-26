package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*


class UserAccountFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private var field = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
        disableHeaderItem()
        closeKeyboard()
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun disableHeaderItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = false
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
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

    @SuppressLint("SimpleDateFormat")
    private fun setOnClick() {
        val clUserAccountFirstName: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountFirstName)
        val clUserAccountLastName: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountLastName)
        val clUserAccountBirthDate: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountBirthDate)
        val clUserAccountEmail: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountEmail)
        val clUserAccountMobileNum: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountMobileNum)
        val clUserAccountUsername: ConstraintLayout =
            appCompatActivity.findViewById(R.id.clUserAccountUsername)
        val clUserAccountPassword: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountPassword)
        val clUserAccountMasterPIN: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountMasterPIN)
        val clUserAccountExportData: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountExportData)

        clUserAccountFirstName.setOnClickListener {
            field = "first name"
            openConfirmActionActivity()

            it.apply {
                clUserAccountFirstName.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountFirstName.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountLastName.setOnClickListener {
            field = "last name"
            openConfirmActionActivity()

            it.apply {
                clUserAccountLastName.isClickable = false                                           // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountLastName.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountBirthDate.setOnClickListener {
            field = "birth date"
            openConfirmActionActivity()

            it.apply {
                clUserAccountBirthDate.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountBirthDate.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountEmail.setOnClickListener {
            field = "email"
            openConfirmActionActivity()

            it.apply {
                clUserAccountEmail.isClickable = false                                              // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountEmail.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountMobileNum.setOnClickListener {
            field = "mobile number"
            openConfirmActionActivity()

            it.apply {
                clUserAccountMobileNum.isClickable = false                                          // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountMobileNum.isClickable = true
                        }, 1000
                )
            }
        }

        clUserAccountUsername.setOnClickListener {
            field = "username"
            val action = UserAccountFragmentDirections
                    .actionUserAccountFragmentToUserAccountEditFragment(field)
            findNavController().navigate(action)
        }

        clUserAccountPassword.setOnClickListener {
            field = "password"
            val action = UserAccountFragmentDirections
                    .actionUserAccountFragmentToUserAccountEditFragment(field)
            findNavController().navigate(action)
        }

        clUserAccountMasterPIN.setOnClickListener {
            field = "master pin"
            val action = UserAccountFragmentDirections
                    .actionUserAccountFragmentToUserAccountEditFragment(field)
            findNavController().navigate(action)
        }

        clUserAccountExportData.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            builder.setMessage("Export data to 'SixKeeper' folder in the phone storage?")
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                val packageName = context?.packageName

                @Suppress("DEPRECATION")
                val sd = Environment.getExternalStorageDirectory()

                val data = Environment.getDataDirectory()
                val source: FileChannel?
                val destination: FileChannel?
                val currentDBPath = "/data/$packageName/databases/SixKeeperDatabase"
                val backupDBPath = "SixKeeper/SixKeeperDatabase"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, backupDBPath)

                try {
                    source = FileInputStream(currentDB).channel
                    destination = FileOutputStream(backupDB).channel
                    destination.transferFrom(source, 0, source.size())                               // Save data to folder
                    source.close()
                    destination.close()

                    val toast: Toast = Toast.makeText(
                            appCompatActivity,
                            R.string.user_export_data_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }

                    val databaseHandlerClass = DatabaseHandlerClass(attActivity)
                    val encodingClass = EncodingClass()

                    var actionLogId = 1000001
                    val lastId = databaseHandlerClass.getLastIdOfActionLog()

                    if (lastId.isNotEmpty()) {
                        actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
                    }

                    val calendar: Calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                    val date: String = dateFormat.format(calendar.time)

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encodingClass.encodeData(actionLogId.toString()),
                                    encodingClass.encodeData("Data was exported."),
                                    encodingClass.encodeData(date)
                            )
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title_confirm)
            alert.show()

            it.apply {
                clUserAccountExportData.isClickable = false                                         // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserAccountExportData.isClickable = true
                        }, 500
                )
            }
        }
    }

    private fun openConfirmActionActivity() {
        val goToConfirmActivity = Intent(
                appCompatActivity,
                ConfirmActionActivity::class.java
        )

        @Suppress("DEPRECATION")
        startActivityForResult(goToConfirmActivity, 16914)
        appCompatActivity.overridePendingTransition(
                R.anim.anim_enter_bottom_to_top_2,
                R.anim.anim_0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                view?.apply {
                    postDelayed(
                            {
                                val action = UserAccountFragmentDirections
                                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                                findNavController().navigate(action)
                            }, 250
                    )
                }
            }
        }
    }
}