package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AnalyticsWeakPasswordsListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>,
        private val number: ArrayList<String>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_categories_platforms_list_adapter,
        idName
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_categories_platforms_list_adapter,
                null,
                true
        )
        val ivCategoriesPlatformsIcon =
                rowView.findViewById(R.id.ivCategoriesPlatformsIcon) as ImageView
        val tvCategoriesPlatformsName =
                rowView.findViewById(R.id.tvCategoriesPlatformsName) as TextView
        val tvCategoriesPlatformsNumber =
                rowView.findViewById(R.id.tvCategoriesPlatformsNumber) as TextView

        ivCategoriesPlatformsIcon.setImageResource(R.drawable.ic_account_circle_light_gray)
        tvCategoriesPlatformsName.text = name[position]
        tvCategoriesPlatformsNumber.text = number[position]

        return rowView
    }
}