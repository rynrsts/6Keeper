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
    
    private class ViewHolder {
        lateinit var cbSavedPassSelectAll: CheckBox
        lateinit var llSavedPasswordListAdapter: LinearLayout
        lateinit var cbSavedPassCheckBox: CheckBox
        lateinit var tvSavedPassPassword: TextView
        lateinit var tvSavedPassDate: TextView
    }

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_saved_password_list_adapter,
                null,
                true
        )
        val viewHolder = ViewHolder()
        val savedPasswordModelClass: SavedPasswordModelClass = modelArrayList[position]

        viewHolder.cbSavedPassSelectAll = context.findViewById(R.id.cbSavedPassSelectAll)
        viewHolder.llSavedPasswordListAdapter =
                rowView.findViewById(R.id.llSavedPasswordListAdapter)
        viewHolder.cbSavedPassCheckBox = rowView.findViewById(R.id.cbSavedPassCheckBox)
        viewHolder.tvSavedPassPassword = rowView.findViewById(R.id.tvSavedPassPassword)
        viewHolder.tvSavedPassDate = rowView.findViewById(R.id.tvSavedPassDate)

        viewHolder.cbSavedPassCheckBox.isChecked = savedPasswordModelClass.getSelected()
        viewHolder.tvSavedPassPassword.text = "Password: ${password[position]}"
        viewHolder.tvSavedPassDate.text = "Creation Date: ${creationDate[position]}"
        rowView.tag = position

        viewHolder.cbSavedPassSelectAll.setOnClickListener {
            if (!modelArrayList.isNullOrEmpty()) {
                for (i in 0 until modelArrayList.size) {
                    modelArrayList[i].setSelected(viewHolder.cbSavedPassSelectAll.isChecked)
                }

                notifyDataSetChanged()
            }
        }

        rowView.setOnClickListener { v ->
            val currentPos = v.tag as Int
            val savedPassword: SavedPasswordModelClass = modelArrayList[currentPos]
            val reverseSelect = !savedPassword.getSelected()

            savedPassword.setSelected(reverseSelect)

            if (!reverseSelect && viewHolder.cbSavedPassSelectAll.isChecked) {
                viewHolder.cbSavedPassSelectAll.isChecked = false
            }

            notifyDataSetChanged()
        }

        return rowView
    }
}