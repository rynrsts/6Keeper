package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AddAccountAppListAdapter(
        private val context: Activity,
        name: ArrayList<String>,
        private val packageList: ArrayList<PackageInfo>
) : ArrayAdapter<String>(
        context,
        R.layout.layout_add_account_app_list_adapter,
        name
) {

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_add_account_app_list_adapter,
                null,
                true
        )
        val temp = packageList[position].applicationInfo

        val ivAddAccountAppIcon = rowView.findViewById(R.id.ivAddAccountAppIcon) as ImageView
        val tvAddAccountAppName = rowView.findViewById(R.id.tvAddAccountAppName) as TextView

        ivAddAccountAppIcon.setImageDrawable(temp.loadIcon(context.packageManager))
        tvAddAccountAppName.text = temp.loadLabel(context.packageManager).toString()

        return rowView
    }
}