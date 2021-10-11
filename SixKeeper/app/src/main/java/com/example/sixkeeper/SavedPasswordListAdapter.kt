package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class SavedPasswordListAdapter(
        private val context: Activity,
        id: ArrayList<String>,
        private val password: ArrayList<String>,
        private val creationDate: ArrayList<String>,
        private val modelArrayList: ArrayList<SavedPasswordModelClass>
) : ArrayAdapter<String>(context, R.layout.layout_saved_password_list_adapter, id) {

    private var savedPasswordModelClass = SavedPasswordModelClass()

    @SuppressLint("ViewHolder", "SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(                                                             // Inflate list adapter
                R.layout.layout_saved_password_list_adapter,
                null,
                true
        )

        val cbSavedPassSelectAll = context.findViewById(R.id.cbSavedPassSelectAll) as CheckBox
        val llSavedPasswordListAdapter =
                rowView.findViewById(R.id.llSavedPasswordListAdapter) as LinearLayout
        val cbSavedPassCheckBox = rowView.findViewById(R.id.cbSavedPassCheckBox) as CheckBox
        val tvSavedPassPassword = rowView.findViewById(R.id.tvSavedPassPassword) as TextView
        val tvSavedPassDate = rowView.findViewById(R.id.tvSavedPassDate) as TextView

        savedPasswordModelClass = modelArrayList[position]
        cbSavedPassCheckBox.isChecked = savedPasswordModelClass.getSelected()
        llSavedPasswordListAdapter.tag = position
        tvSavedPassPassword.text = "Password: ${password[position]}"
        tvSavedPassDate.text = "Creation Date: ${creationDate[position]}"

        cbSavedPassSelectAll.setOnClickListener {
            var isChecked = false

            if (cbSavedPassSelectAll.isChecked) {
                isChecked = true
            }

            for (i in 0 until password.size) {
                modelArrayList[i].setSelected(isChecked)
            }

            notifyDataSetChanged()
        }

        llSavedPasswordListAdapter.setOnClickListener { v ->
            val currentPos = v.tag as Int
            var isChecked = false

            if (!modelArrayList[currentPos].getSelected()){
                isChecked = true
            }

            modelArrayList[currentPos].setSelected(isChecked)
            notifyDataSetChanged()
        }

        return rowView
    }
}

//class SavedPasswordListAdapter(
//        id: ArrayList<String>,
//        private val password: ArrayList<String>,
//        private val creationDate: ArrayList<String>
//) : RecyclerView.Adapter<SavedPasswordListAdapter.ViewHolder>() {
//    private val checkState: ArrayList<Boolean> = ArrayList()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val listItem: View = layoutInflater.inflate(
//                R.layout.layout_saved_password_list_adapter,
//                parent,
//                false
//        )
//        return ViewHolder(listItem)
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.cbSavedPassCheckBox.tag = position
//        holder.tvSavedPassPassword.text = "Password: ${password[position]}"
//        holder.tvSavedPassDate.text = "Creation Date: ${creationDate[position]}"
//        checkState.add(false)
//        holder.cbSavedPassCheckBox.isChecked = checkState[position]
//
//        holder.llSavedPasswordListAdapter.setOnClickListener { view ->
//            val currentPos = view.tag
//
//            if (holder.cbSavedPassCheckBox.tag == currentPos) {
//                holder.cbSavedPassCheckBox.isChecked
//            }
//
//            notifyDataSetChanged()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return password.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val cbSavedPassCheckBox: CheckBox = itemView.findViewById(R.id.cbSavedPassCheckBox)
//        val llSavedPasswordListAdapter: LinearLayout =
//                itemView.findViewById(R.id.llSavedPasswordListAdapter)
//        val tvSavedPassDate: TextView = itemView.findViewById(R.id.tvSavedPassDate)
//        val tvSavedPassPassword: TextView = itemView.findViewById(R.id.tvSavedPassPassword)
//    }
//}