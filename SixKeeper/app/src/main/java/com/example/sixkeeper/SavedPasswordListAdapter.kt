package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SavedPasswordListAdapter(
        private val context: Activity,
        private val id: Array<String>,
        private val password: Array<String>
) : ArrayAdapter<String>(context, R.layout.layout_saved_password_list_adapter, password) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_saved_password_list_adapter,
                null,
                true
        )

        val tvSavedPassId = rowView.findViewById(R.id.tvSavedPassId) as TextView
        val tvSavedPassPassword = rowView.findViewById(R.id.tvSavedPassPassword) as TextView

        tvSavedPassId.text = "ID: ${id[position]}"
        tvSavedPassPassword.text = "Password: ${password[position]}"

        return rowView
    }
}