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
import android.widget.AdapterView.OnItemClickListener
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
    private lateinit var encodingClass: EncodingClass

    private lateinit var cbSavedPassSelectAll: CheckBox
    private lateinit var lvSavedPasswordContainer: ListView

    private var selectedPositionContainer = ArrayList<Int>(0)
    private val selectedIdContainer = ArrayList<String>(0)
    private var selectedPasswordContainer = ArrayList<String>(0)
    private val itemSelected = ArrayList<Int>(0)

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
                val uPassword = encodingClass.decodeData(u.generatedPassword)

                userSavedPassId.add(
                        encodingClass.decodeData(u.passId) + "ramjcammjar" + uPassword
                )
                userSavedPassPassword.add(uPassword)
                userSavedPassCreationDate.add(encodingClass.decodeData(u.creationDate))
                itemSelected.add(0)
            }

            val savedPasswordListAdapter = SavedPasswordListAdapter(
                    attActivity,
                    userSavedPassId,
                    userSavedPassPassword,
                    userSavedPassCreationDate
            )

            lvSavedPasswordContainer.adapter = savedPasswordListAdapter
        }
    }

    @SuppressLint("ShowToast")
    private fun setOnClick() {
        val clSavedPassDelete: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clSavedPassDelete)
        val clSavedPassCopy: ConstraintLayout = appCompatActivity.findViewById(R.id.clSavedPassCopy)

        cbSavedPassSelectAll.setOnClickListener {
            if (cbSavedPassSelectAll.isChecked) {
                for (i in 0 until lvSavedPasswordContainer.childCount) {
                    if (itemSelected[i] == 0) {
                        lvSavedPasswordContainer.performItemClick(
                                lvSavedPasswordContainer.getChildAt(i),
                                i,
                                lvSavedPasswordContainer.adapter.getItemId(i)
                        )
                    }
                }
            } else {
                for (i in 0 until lvSavedPasswordContainer.childCount) {
                    if (itemSelected[i] == 1) {
                        lvSavedPasswordContainer.performItemClick(
                                lvSavedPasswordContainer.getChildAt(i),
                                i,
                                lvSavedPasswordContainer.adapter.getItemId(i)
                        )
                    }
                }
            }
        }

        lvSavedPasswordContainer.onItemClickListener = (OnItemClickListener { _, view, pos, _ ->
            val selectedItem = lvSavedPasswordContainer.getItemAtPosition(pos).toString()
            val selectedItemValue = selectedItem.split("ramjcammjar")
            val selectedItemId = selectedItemValue[0]
            val selectedItemPassword = selectedItemValue[1]
            val cbSavedPassCheckBox: CheckBox = view.findViewById(R.id.cbSavedPassCheckBox)

            if (cbSavedPassCheckBox.isChecked) {
                selectedPositionContainer.remove(pos)
                cbSavedPassCheckBox.isChecked = false
                selectedIdContainer.remove(encodingClass.encodeData(selectedItemId))
                selectedPasswordContainer.remove(selectedItemPassword)
                itemSelected[pos] = 0
            } else {
                cbSavedPassCheckBox.isChecked = true
                selectedPositionContainer.add(pos)
                selectedIdContainer.add(encodingClass.encodeData(selectedItemId))
                selectedPasswordContainer.add(selectedItemPassword)
                itemSelected[pos] = 1
            }
        })

        clSavedPassDelete.setOnClickListener {
            val userSavedPass: List<UserSavedPassModelClass> =
                    databaseHandlerClass.viewSavedPass(encodingClass.encodeData(0.toString()))

            if (!userSavedPass.isNullOrEmpty() && !selectedIdContainer.isNullOrEmpty()) {
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
            val toast: Toast

            if (!selectedPasswordContainer.isNullOrEmpty()) {
                if (selectedPasswordContainer.size == 1) {
                    val clipboard: ClipboardManager =
                            appCompatActivity.getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager
                    val clip = ClipData.newPlainText("pw", selectedPasswordContainer[0])
                    clipboard.setPrimaryClip(clip)

                    lvSavedPasswordContainer.performItemClick(
                            lvSavedPasswordContainer.getChildAt(selectedPositionContainer[0]),
                            selectedPositionContainer[0],
                            lvSavedPasswordContainer.adapter.getItemId(selectedPositionContainer[0])
                    )

                    cbSavedPassSelectAll.isChecked = false
                    selectedPositionContainer.clear()
                    selectedIdContainer.clear()
                    selectedPasswordContainer.clear()

                    toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.saved_password_pass_copy,
                            Toast.LENGTH_SHORT
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

            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }

            it.apply {
                clSavedPassCopy.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed(
                        {
                            clSavedPassCopy.isClickable = true
                        }, 1000
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                var selectedId = arrayOfNulls<String>(selectedIdContainer.size)
                selectedId = selectedIdContainer.toArray(selectedId)

                val calendar: Calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                val date: String = dateFormat.format(calendar.time)

                val status = databaseHandlerClass.updateDeleteMultipleAccount(
                        selectedId,
                        encodingClass.encodeData(1.toString()),
                        encodingClass.encodeData(date),
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

                cbSavedPassSelectAll.isChecked = false
                viewSavedPass()
                selectedPositionContainer.clear()
                selectedIdContainer.clear()
                selectedPasswordContainer.clear()
            }
        }
    }
}