package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ActionLogListAdapter(
        private val context: Activity,
        id: ArrayList<String>,
        private val description: ArrayList<String>,
        private val date: ArrayList<String>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_action_log_list_adapter,
        id
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_action_log_list_adapter,
                null,
                true
        )

        val tvActionLogDescription = rowView.findViewById(R.id.tvActionLogDescription) as TextView
        val tvActionLogDate = rowView.findViewById(R.id.tvActionLogDate) as TextView

        tvActionLogDescription.text = description[position]
        tvActionLogDate.text = date[position]

        return rowView
    }
}