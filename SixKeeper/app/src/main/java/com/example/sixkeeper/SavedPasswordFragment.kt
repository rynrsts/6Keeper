package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var encodingClass: EncodingClass

    private lateinit var cbSavedPassSelectAll: CheckBox
    private lateinit var lvSavedPasswordContainer: ListView

    private val modelArrayList = ArrayList<SavedPasswordModelClass>(0)
    private lateinit var savedPasswordModelClass: SavedPasswordModelClass

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
        encodingClass = EncodingClass()

        cbSavedPassSelectAll = appCompatActivity.findViewById(R.id.cbSavedPassSelectAll)
        lvSavedPasswordContainer = appCompatActivity.findViewById(R.id.lvSavedPasswordContainer)
    }

    @SuppressLint("InflateParams")
    private fun viewSavedPass() {                                                                   // View saved password
        val userSavedPass: List<UserSavedPassModelClass> =
                databaseHandlerClass.viewSavedPass(encodingClass.encodeData(0.toString()))
        val userSavedPassId = ArrayList<String>(0)
        val userSavedPassPassword = ArrayList<String>(0)
        val userSavedPassCreationDate = ArrayList<String>(0)

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
            lvSavedPasswordContainer.adapter = null
            llSavedPasswordNoItem.apply {
                removeAllViews()
                addView(inflatedView)
            }
        } else {
            for (u in userSavedPass) {
                val uId = encodingClass.decodeData(u.passId)
                val uPassword = encodingClass.decodeData(u.generatedPassword)

                userSavedPassId.add(uId)
                userSavedPassPassword.add(uPassword)
                userSavedPassCreationDate.add(encodingClass.decodeData(u.creationDate))

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

            it.apply {
                clSavedPassDelete.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed(
                        {
                            clSavedPassDelete.isClickable = true
                        }, 1000
                )
            }
        }

        clSavedPassCopy.setOnClickListener {
            var numOfSelected = 0
            var onlyOneIsSelected =  true
            var password = ""
            val toast: Toast?

            for (i in 0 until modelArrayList.size) {
                if (modelArrayList[i].getSelected()) {
                    numOfSelected++
                    password = modelArrayList[i].getPassword()

                    if (numOfSelected == 2) {
                        onlyOneIsSelected = false
                        break
                    }
                }
            }

            if (numOfSelected > 0) {
                if (onlyOneIsSelected) {
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

                    databaseHandlerClass.addEventToActionLog(                                       // Add event to Action Log
                            UserActionLogModelClass(
                                    encodingClass.encodeData(getLastActionLogId().toString()),
                                    encodingClass.encodeData(
                                            "Selected saved password was copied."
                                    ),
                                    encodingClass.encodeData(getCurrentDate())
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

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
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
                val cbSavedPassSelectAll: CheckBox =
                        appCompatActivity.findViewById(R.id.cbSavedPassSelectAll)

                for (i in 0 until modelArrayList.size) {
                    if (modelArrayList[i].getSelected()) {
                        container.add(
                                encodingClass.encodeData(
                                        modelArrayList[i].getId().toString()
                                )
                        )
                    }
                }

                var selectedId = arrayOfNulls<String>(container.size)
                selectedId = container.toArray(selectedId)

                val status = databaseHandlerClass.updateDeleteMultipleAccount(
                        selectedId,
                        encodingClass.encodeData(1.toString()),
                        encodingClass.encodeData(getCurrentDate()),
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
                                encodingClass.encodeData(getLastActionLogId().toString()),
                                encodingClass.encodeData(
                                        "Selected saved password/s were deleted."
                                ),
                                encodingClass.encodeData(getCurrentDate())
                        )
                )

                if (cbSavedPassSelectAll.isChecked) {
                    cbSavedPassSelectAll.performClick()
                }

                modelArrayList.clear()
                viewSavedPass()
            }
        }
    }
}