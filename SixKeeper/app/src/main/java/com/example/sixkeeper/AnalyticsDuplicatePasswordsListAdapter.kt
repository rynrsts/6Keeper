package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AnalyticsDuplicatePasswordsListAdapter(
        private val context: Activity,
        idPass: ArrayList<String>,
        private val count: ArrayList<String>,
) : ArrayAdapter<String>(
        context,
        R.layout.layout_duplicate_passwords_list_adapter,
        idPass
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_duplicate_passwords_list_adapter,
                null,
                true
        )
        val tvDuplicateName = rowView.findViewById(R.id.tvDuplicateName) as TextView

        tvDuplicateName.text = "(" + count[position] + ") Accounts have similar password"

        return rowView
    }
}