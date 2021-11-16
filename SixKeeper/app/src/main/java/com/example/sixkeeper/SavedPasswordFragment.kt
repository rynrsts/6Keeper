package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SavedPasswordFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass

    private lateinit var cbSavedPassSelectAll: CheckBox
    private lateinit var lvSavedPasswordContainer: ListView

    private val modelArrayList = ArrayList<SavedPasswordModelClass>(0)
    private lateinit var savedPasswordModelClass: SavedPasswordModelClass
    
    private lateinit var key: ByteArray

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        viewSavedPass()
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()

        cbSavedPassSelectAll = appCompatActivity.findViewById(R.id.cbSavedPassSelectAll)
        lvSavedPasswordContainer = appCompatActivity.findViewById(R.id.lvSavedPasswordContainer)

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
    }

    @SuppressLint("InflateParams")
    private fun viewSavedPass() {                                                                   // View saved password
        val userSavedPass: List<UserSavedPassModelClass> =
                databaseHandlerClass.viewSavedPass(encryptionClass.encrypt(0.toString(), key))
        val userSavedPassId = ArrayList<String>(0)
        val userSavedPassPassword = ArrayList<String>(0)
        val userSavedPassCreationDate = ArrayList<String>(0)

        if (cbSavedPassSelectAll.isChecked) {
            cbSavedPassSelectAll.performClick()
        }

        modelArrayList.clear()
        lvSavedPasswordContainer.adapter = null

        if (userSavedPass.isNullOrEmpty()) {
            val llSavedPasswordNoItem: LinearLayout =
                    appCompatActivity.findViewById(R.id.llSavedPasswordNoItem)
            val inflatedView = layoutInflater.inflate(
                    R.layout.layout_accounts_no_item,
                    null,
                    true
            )
            val tvSavedPassNoItem: TextView = inflatedView.findViewById(R.id.tvAccountsNoItem)

            tvSavedPassNoItem.setText(R.string.saved_password_no_item)
            llSavedPasswordNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userSavedPass) {
                val uId = encryptionClass.decrypt(u.passId, key)
                val uPassword = encryptionClass.decrypt(u.generatedPassword, key)

                userSavedPassId.add(uId)
                userSavedPassPassword.add(uPassword)
                userSavedPassCreationDate.add(encryptionClass.decrypt(u.creationDate, key))

                savedPasswordModelClass = SavedPasswordModelClass()
                savedPasswordModelClass.setSelected(false)
                savedPasswordModelClass.setId(Integer.parseInt(uId))
                savedPasswordModelClass.setPassword(uPassword)
                modelArrayList.add(savedPasswordModelClass)
            }

            val savedPasswordListAdapter = SavedPasswordListAdapter(
                    attActivity,
                    userSavedPassId,
                    userSavedPassPassword,
                    userSavedPassCreationDate,
                    modelArrayList
            )

            lvSavedPasswordContainer.adapter = savedPasswordListAdapter
        }
    }

    @SuppressLint("ShowToast")
    private fun setOnClick() {
        val clSavedPassDelete: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSavedPassDelete)
        val clSavedPassCopy: ConstraintLayout = appCompatActivity.findViewById(R.id.clSavedPassCopy)

        clSavedPassDelete.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                var itemCheck = false

                for (i in 0 until modelArrayList.size) {
                    if (modelArrayList[i].getSelected()) {
                        itemCheck = true
                        break
                    }
                }

                if (itemCheck) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    builder.setMessage(R.string.saved_password_delete_alert)
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        if (InternetConnectionClass().isConnected()) {
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
                        } else {
                            internetToast()
                        }
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()
                    }

                    val alert: AlertDialog = builder.create()
                    alert.setTitle(R.string.many_alert_title_confirm)
                    alert.show()
                } else {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.many_nothing_to_delete,
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            } else {
                internetToast()
            }

            it.apply {
                clSavedPassDelete.isClickable = false                                               // Set un-clickable for 1 second
                postDelayed(
                        {
                            clSavedPassDelete.isClickable = true
                        }, 500
                )
            }
        }

        clSavedPassCopy.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                var numOfSelected = 0
                var password = ""
                val toast: Toast?

                for (i in 0 until modelArrayList.size) {
                    if (modelArrayList[i].getSelected()) {
                        numOfSelected++
                        password = modelArrayList[i].getPassword()

                        if (numOfSelected == 2) {
                            break
                        }
                    }
                }

                if (numOfSelected > 0) {
                    if (numOfSelected == 1) {
                        val clipboard: ClipboardManager =
                                appCompatActivity.getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                ) as ClipboardManager
                        val clip = ClipData.newPlainText("pw", password)
                        clipboard.setPrimaryClip(clip)

                        toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.saved_password_pass_copy,
                                Toast.LENGTH_SHORT
                        )

                        databaseHandlerClass.addEventToActionLog(                                   // Add event to Action Log
                                UserActionLogModelClass(
                                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                                        encryptionClass.encrypt(
                                                "Selected saved password was copied.", key
                                        ),
                                        encryptionClass.encrypt(getCurrentDate(), key)
                                )
                        )
                    } else {
                        toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.saved_password_one_pass,
                                Toast.LENGTH_SHORT
                        )
                    }
                } else {
                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.pass_generator_nothing_to_copy,
                            Toast.LENGTH_SHORT
                    )
                }

                toast?.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            } else {
                internetToast()
            }

            it.apply {
                clSavedPassCopy.isClickable = false                                                 // Set un-clickable for 1 second
                postDelayed(
                        {
                            clSavedPassCopy.isClickable = true
                        }, 100
                )
            }
        }
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

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                val container = ArrayList<String>(0)

                for (i in 0 until modelArrayList.size) {
                    if (modelArrayList[i].getSelected()) {
                        container.add(
                                encryptionClass.encrypt(
                                        modelArrayList[i].getId().toString(),
                                        key
                                )
                        )
                    }
                }

                var selectedId = arrayOfNulls<String>(container.size)
                selectedId = container.toArray(selectedId)

                val status = databaseHandlerClass.updateDeleteMultipleAccount(
                        selectedId,
                        encryptionClass.encrypt(1.toString(), key),
                        encryptionClass.encrypt(getCurrentDate(), key),
                        "SavedPasswordTable",
                        "pass_id",
                        "pass_deleted",
                        "pass_delete_date"
                )

                if (status > -1) {
                    val toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.saved_password_delete_mes,
                            Toast.LENGTH_SHORT
                    )
                    toast?.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                databaseHandlerClass.addEventToActionLog(                                           // Add event to Action Log
                        UserActionLogModelClass(
                                encryptionClass.encrypt(getLastActionLogId().toString(), key),
                                encryptionClass.encrypt(
                                        "Selected saved password/s were deleted.", key
                                ),
                                encryptionClass.encrypt(getCurrentDate(), key)
                        )
                )

                viewSavedPass()
            }
        }
    }
}