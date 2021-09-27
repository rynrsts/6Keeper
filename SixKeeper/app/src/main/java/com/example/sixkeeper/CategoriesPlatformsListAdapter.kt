package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CategoriesPlatformsListAdapter(
        private val context: Activity,
        private val categoryName: Array<String>,
        private val numberOfPlatforms: Array<String>
) : ArrayAdapter<String>(context, R.layout.layout_categories_platforms_list_adapter, categoryName) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_categories_platforms_list_adapter,
                null,
                true
        )

        val tvCategoriesPlatformsName =
                rowView.findViewById(R.id.tvCategoriesPlatformsName) as TextView
        val tvCategoriesPlatformsNumber =
                rowView.findViewById(R.id.tvCategoriesPlatformsNumber) as TextView

        tvCategoriesPlatformsName.text = categoryName[position]
        tvCategoriesPlatformsNumber.text = "Platforms: ${numberOfPlatforms[position]}"

        return rowView
    }
}