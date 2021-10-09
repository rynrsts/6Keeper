package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class RecycleBinListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_recycle_bin_list_adapter,
        idName
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_recycle_bin_list_adapter,
                null,
                true
        )
        val tvRecycleBinName = rowView.findViewById(R.id.tvRecycleBinName) as TextView
        val ivRecycleBinIcon = rowView.findViewById(R.id.ivRecycleBinIcon) as ImageView

        tvRecycleBinName.text = name[position]
        ivRecycleBinIcon.setImageResource(R.drawable.ic_account_circle_light_gray)

        return rowView
    }
}