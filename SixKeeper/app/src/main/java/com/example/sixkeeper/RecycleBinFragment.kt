package com.example.sixkeeper

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout

class RecycleBinFragment : RecycleBinProcessClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycle_bin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        populateDeletedAccounts()
        setOnClick()
    }

    private fun setOnClick() {
        val clRecycleBinAccounts: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clRecycleBinAccounts)
        val clRecycleBinPassword: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clRecycleBinPassword)
        val clRecycleBinDelete: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clRecycleBinDelete)
        val clRecycleBinRestore: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clRecycleBinRestore)

        val vRecycleBinDivisionA: View =
                getAppCompatActivity().findViewById(R.id.vRecycleBinDivisionA)
        val vRecycleBinDivisionB: View =
                getAppCompatActivity().findViewById(R.id.vRecycleBinDivisionB)

        clRecycleBinAccounts.setOnClickListener {
            if (getSelectedTab() == "password generator") {
                vRecycleBinDivisionB.setBackgroundResource(R.color.white)
                vRecycleBinDivisionA.setBackgroundResource(R.color.blue)
                setSelectedTab("accounts")

                populateDeletedAccounts()
            }
        }

        clRecycleBinPassword.setOnClickListener {
            if (getSelectedTab() == "accounts") {
                vRecycleBinDivisionA.setBackgroundResource(R.color.white)
                vRecycleBinDivisionB.setBackgroundResource(R.color.blue)
                setSelectedTab("password generator")

                populateDeletedPasswords()
            }
        }

        getCbRecycleBinSelectAll().setOnClickListener {
            if (getCbRecycleBinSelectAll().isChecked) {
                for (i in 0 until getLvRecycleBinContainer().childCount) {
                    if (getItemSelected()[i] == 0) {
                        getLvRecycleBinContainer().performItemClick(
                                getLvRecycleBinContainer().getChildAt(i),
                                i,
                                getLvRecycleBinContainer().adapter.getItemId(i)
                        )
                    }
                }
            } else {
                for (i in 0 until getLvRecycleBinContainer().childCount) {
                    if (getItemSelected()[i] == 1) {
                        getLvRecycleBinContainer().performItemClick(
                                getLvRecycleBinContainer().getChildAt(i),
                                i,
                                getLvRecycleBinContainer().adapter.getItemId(i)
                        )
                    }
                }
            }
        }

        getLvRecycleBinContainer().onItemClickListener = (OnItemClickListener { _, view, pos, _ ->
            val selectedItem = getLvRecycleBinContainer().getItemAtPosition(pos).toString()
            val cbRecycleBinCheckBox: CheckBox = view.findViewById(R.id.cbRecycleBinCheckBox)

            if (cbRecycleBinCheckBox.isChecked) {
                cbRecycleBinCheckBox.isChecked = false
                getItemSelected()[pos] = 0
                getSelectedIdContainer().remove(getEncodingClass().encodeData(selectedItem))
            } else {
                cbRecycleBinCheckBox.isChecked = true
                getItemSelected()[pos] = 1
                getSelectedIdContainer().add(getEncodingClass().encodeData(selectedItem))
            }
        })

        clRecycleBinDelete.setOnClickListener {
            if (!getSelectedIdContainer().isNullOrEmpty()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
                builder.setMessage(R.string.recycle_bin_delete_mes)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    confirmMasterPIN()
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title_confirm)
                alert.show()
            } else {
                val toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        R.string.many_nothing_to_delete,
                        Toast.LENGTH_SHORT
                )
                toast?.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }

            it.apply {
                clRecycleBinDelete.isClickable = false                                              // Set un-clickable for 1 second
                postDelayed(
                        {
                            clRecycleBinDelete.isClickable = true
                        }, 1000
                )
            }
        }

        clRecycleBinRestore.setOnClickListener {
//            var selectedIdNoDuplicate = arrayOfNulls<String>(selectedIdContainer.size)
//            selectedIdNoDuplicate = selectedIdContainer.toArray(selectedIdNoDuplicate)
//
//            databaseHandlerClass.updateDeleteMultipleAccount(
//                    encodingClass.encodeData(0.toString()),
//                    "",
//                    selectedIdNoDuplicate,
//                    "account_id"
//            )
//
//            selectedIdContainer.clear()
//            populateDeletedItems("")
        }
    }
}