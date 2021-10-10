package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SavedPasswordListAdapter(
        private val context: Activity,
        id: ArrayList<String>,
        private val password: ArrayList<String>,
        private val creationDate: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.layout_saved_password_list_adapter, id) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_saved_password_list_adapter,
                null,
                true
        )

        val tvSavedPassPassword = rowView.findViewById(R.id.tvSavedPassPassword) as TextView
        val tvSavedPassDate = rowView.findViewById(R.id.tvSavedPassDate) as TextView

        tvSavedPassPassword.text = "Password: ${password[position]}"
        tvSavedPassDate.text = "Creation Date: ${creationDate[position]}"

        return rowView
    }
}