package com.example.sixkeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
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
        populateDeletedItems("")
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

                populateDeletedItems("")
            }
        }

        clRecycleBinPassword.setOnClickListener {
            if (getSelectedTab() == "accounts") {
                vRecycleBinDivisionA.setBackgroundResource(R.color.white)
                vRecycleBinDivisionB.setBackgroundResource(R.color.blue)
                setSelectedTab("password generator")
            }
        }

        getLvRecycleBinContainer().onItemClickListener = (OnItemClickListener { _, view, pos, _ ->
//            val selectedItem = getLvRecycleBinContainer().getItemAtPosition(pos).toString()
//            val selectedItemValue = selectedItem.split("ramjcammjar")
//            val selectedItemId = selectedItemValue[0]
//            val cbRecycleBinCheckBox: CheckBox = view.findViewById(R.id.cbRecycleBinCheckBox)
//
//            if (cbRecycleBinCheckBox.isChecked) {
//                cbRecycleBinCheckBox.isChecked = false
//                selectedIdContainer.remove(encodingClass.encodeData(selectedItemId))
//            } else {
//                cbRecycleBinCheckBox.isChecked = true
//                selectedIdContainer.add(encodingClass.encodeData(selectedItemId))
//            }
        })

        clRecycleBinDelete.setOnClickListener {
//            var selectedId = arrayOfNulls<String>(selectedIdContainer.size)
//            selectedId = selectedIdContainer.toArray(selectedId)
//
//            val status = databaseHandlerClass.removeRecycleBin(selectedId)
//
//            if (status > -1) {
//                val toast = Toast.makeText(
//                        appCompatActivity.applicationContext,
//                        R.string.recycle_bin_selected_items_delete_mes,
//                        Toast.LENGTH_SHORT
//                )
//                toast?.apply {
//                    setGravity(Gravity.CENTER, 0, 0)
//                    show()
//                }
//            }
//
//            populateDeletedItems("")
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