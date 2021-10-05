package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

class AccountsListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>,
        private val isFavorites: ArrayList<String>
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
        val llAccountsFavorites = rowView.findViewById(R.id.llAccountsFavorites) as LinearLayout

        tvAccountsName.text = name[position]

        if (isFavorites[position] == "1") {
            val inflatedView = context.layoutInflater.inflate(
                    R.layout.layout_accounts_star,
                    null,
                    true
            )
            llAccountsFavorites.addView(inflatedView)
        }

        return rowView
    }
}