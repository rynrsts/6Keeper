package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AnalyticsOldPasswordsListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>,
        private val directory: ArrayList<String>,
        private val date: ArrayList<String>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_old_passwords_list_adapter,
        idName
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_old_passwords_list_adapter,
                null,
                true
        )
        val tvOldPasswordsName = rowView.findViewById(R.id.tvOldPasswordsName) as TextView
        val tvOldPasswordsDirectory = rowView.findViewById(R.id.tvOldPasswordsDirectory) as TextView
        val tvOldPasswordsDate = rowView.findViewById(R.id.tvOldPasswordsDate) as TextView

        tvOldPasswordsName.text = name[position]
        tvOldPasswordsDirectory.text = directory[position]
        tvOldPasswordsDate.text = date[position]

        return rowView
    }
}