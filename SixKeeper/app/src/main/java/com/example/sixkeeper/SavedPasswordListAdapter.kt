package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class SavedPasswordListAdapter(
        private val context: Activity,
        id: ArrayList<String>,
        private val password: ArrayList<String>,
        private val creationDate: ArrayList<String>,
        private val modelArrayList: ArrayList<SavedPasswordModelClass>
) : ArrayAdapter<String>(context, R.layout.layout_saved_password_list_adapter, id) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_saved_password_list_adapter,
                null,
                true
        )

        val cbSavedPassSelectAll = context.findViewById(R.id.cbSavedPassSelectAll) as CheckBox
        val llSavedPasswordListAdapter =
                rowView.findViewById(R.id.llSavedPasswordListAdapter) as LinearLayout
        val cbSavedPassCheckBox = rowView.findViewById(R.id.cbSavedPassCheckBox) as CheckBox
        val tvSavedPassPassword = rowView.findViewById(R.id.tvSavedPassPassword) as TextView
        val tvSavedPassDate = rowView.findViewById(R.id.tvSavedPassDate) as TextView

        llSavedPasswordListAdapter.tag = position
        cbSavedPassCheckBox.isChecked = modelArrayList[position].getSelected()
        tvSavedPassPassword.text = "Password: ${password[position]}"
        tvSavedPassDate.text = "Creation Date: ${creationDate[position]}"

        cbSavedPassSelectAll.setOnClickListener {
            if (!modelArrayList.isNullOrEmpty()) {
                for (i in 0 until modelArrayList.size) {
                    modelArrayList[i].setSelected(cbSavedPassSelectAll.isChecked)
                }

                notifyDataSetChanged()
            }
        }

        llSavedPasswordListAdapter.setOnClickListener { v ->
            val currentPos = v.tag as Int
            val reverseSelect = !modelArrayList[currentPos].getSelected()

            modelArrayList[currentPos].setSelected(reverseSelect)

            if (!reverseSelect && cbSavedPassSelectAll.isChecked) {
                cbSavedPassSelectAll.isChecked = false
            }

            modelArrayList[currentPos].setSelected(reverseSelect)
            modelArrayList[currentPos].getSelected()
            notifyDataSetChanged()
        }

        return rowView
    }
}