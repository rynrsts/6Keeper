package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class SpecificCategoryFragment : SpecificCategoryProcessClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        populatePlatforms()
        setOnClick()
    }

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val ivSpecificCatAddPlatforms: ImageView =
                getAppCompatActivity().findViewById(R.id.ivSpecificCatAddPlatforms)

        ivSpecificCatAddPlatforms.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_add_new, null)

            builder.apply {
                setView(dialogView)
                setCancelable(false)
            }

            val tvAccountsAddNew: TextView = dialogView.findViewById(R.id.tvAccountsAddNew)
            val etAccountsAddNew: EditText = dialogView.findViewById(R.id.etAccountsAddNew)
            val ivAccountsAddNew: ImageView = dialogView.findViewById(R.id.ivAccountsAddNew)

            tvAccountsAddNew.setText(R.string.specific_category_platform_name)
            ivAccountsAddNew.setImageResource(R.drawable.ic_format_list_bulleted_light_black)

            builder.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                val newPlatform = etAccountsAddNew.text.toString()

                if (newPlatform.isNotEmpty()) {
                    addNewPlatform(newPlatform)
                } else {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.many_nothing_to_add,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }

                it.apply {
                    postDelayed(
                            {
                                closeKeyboard()
                            }, 50
                    )
                }
            }
            builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.cancel()

                it.apply {
                    postDelayed(
                            {
                                closeKeyboard()
                            }, 50
                    )
                }
            }

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                getAppCompatActivity(),
                                R.drawable.layout_alert_dialog
                        )
                )
                setTitle(R.string.specific_category_new_platform)
                show()
            }
        }

//        getLvAccountsContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
//            val selectedCategoryId = getLvAccountsContainer().getItemAtPosition(i).toString()
//
//            val action = AccountsFragmentDirections
//                    .actionAccountsFragmentToSpecificCategoryFragment(selectedCategoryId)
//            findNavController().navigate(action)
//
//            closeKeyboard()
//        })
    }
}