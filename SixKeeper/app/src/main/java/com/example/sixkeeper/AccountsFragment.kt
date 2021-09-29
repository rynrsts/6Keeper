package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController


class AccountsFragment : AccountsProcessClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        populateCategories("")
        setOnClick()
    }

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val ivAccountsSearchButton: ImageView =
                getAppCompatActivity().findViewById(R.id.ivAccountsSearchButton)
        val ivAccountsAddCategories: ImageView =
                getAppCompatActivity().findViewById(R.id.ivAccountsAddCategories)

        ivAccountsSearchButton.setOnClickListener {
            val search = getEtAccountsSearchBox().text.toString()

            populateCategories(search)
            closeKeyboard()
        }

        ivAccountsAddCategories.setOnClickListener {
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

            tvAccountsAddNew.setText(R.string.accounts_category_name)
            ivAccountsAddNew.setImageResource(R.drawable.ic_format_list_bulleted_light_black)

            builder.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                val newCategory = etAccountsAddNew.text.toString()

                if (newCategory.isNotEmpty()) {
                    addNewCategory(newCategory)
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
                                getAppCompatActivity(), R.drawable.layout_alert_dialog
                        )
                )
                setTitle(R.string.accounts_new_category)
                show()
            }
        }

        getLvAccountsContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedCategoryId = getLvAccountsContainer().getItemAtPosition(i).toString()

            val action = AccountsFragmentDirections
                    .actionAccountsFragmentToSpecificCategoryFragment(selectedCategoryId)
            findNavController().navigate(action)

            getEtAccountsSearchBox().setText("")
            closeKeyboard()
        })
    }
}