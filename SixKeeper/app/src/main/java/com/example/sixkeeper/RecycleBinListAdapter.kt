package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class RecycleBinListAdapter(
        private val context: Activity,
        idName: ArrayList<String>,
        private val name: ArrayList<String>,
        private val icon: String,
        private val modelArrayList: ArrayList<RecycleBinModelClass>
) : ArrayAdapter<String>(context, R.layout.layout_recycle_bin_list_adapter, idName) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_recycle_bin_list_adapter,
                null,
                true
        )

        val cbRecycleBinSelectAll = context.findViewById(R.id.cbRecycleBinSelectAll) as CheckBox
        val llRecycleBinListAdapter =
                rowView.findViewById(R.id.llRecycleBinListAdapter) as LinearLayout
        val cbRecycleBinCheckBox = rowView.findViewById(R.id.cbRecycleBinCheckBox) as CheckBox
        val tvRecycleBinName = rowView.findViewById(R.id.tvRecycleBinName) as TextView
        val ivRecycleBinIcon = rowView.findViewById(R.id.ivRecycleBinIcon) as ImageView

        llRecycleBinListAdapter.tag = position
        cbRecycleBinCheckBox.isChecked = modelArrayList[position].getSelected()
        tvRecycleBinName.text = name[position]

        if (icon == "accounts") {
            ivRecycleBinIcon.setImageResource(R.drawable.ic_account_circle_light_gray)
        } else if (icon == "passwords") {
            ivRecycleBinIcon.setImageResource(R.drawable.ic_lock_light_black)
        }

        cbRecycleBinSelectAll.setOnClickListener {
            if (!modelArrayList.isNullOrEmpty()) {
                for (i in 0 until name.size) {
                    modelArrayList[i].setSelected(cbRecycleBinSelectAll.isChecked)
                }

                notifyDataSetChanged()
            }
        }

        llRecycleBinListAdapter.setOnClickListener { v ->
            val currentPos = v.tag as Int
            val reverseSelect = !modelArrayList[currentPos].getSelected()

            modelArrayList[currentPos].setSelected(reverseSelect)

            if (!reverseSelect && cbRecycleBinSelectAll.isChecked) {
                cbRecycleBinSelectAll.isChecked = false
            }

            notifyDataSetChanged()
        }

        return rowView
    }
}