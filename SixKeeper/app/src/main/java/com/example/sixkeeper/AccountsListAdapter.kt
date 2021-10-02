package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AccountsListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_accounts_list_adapter,
        idName
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_accounts_list_adapter,
                null,
                true
        )

        val tvAccountsName = rowView.findViewById(R.id.tvAccountsName) as TextView
        tvAccountsName.text = name[position]

        return rowView
    }
}