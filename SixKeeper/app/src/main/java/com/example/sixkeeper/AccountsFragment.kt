package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
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
        setOnLongClick()
    }

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
            showAddUpdateCategory("add", "", "")
        }

        getLvAccountsContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedCategory = getLvAccountsContainer().getItemAtPosition(i).toString()
            val selectedCategoryId = selectedCategory.substring(0, 5)
            val selectedCategoryName = selectedCategory.substring(5, selectedCategory.length)

            val action = AccountsFragmentDirections
                    .actionAccountsFragmentToSpecificCategoryFragment(
                            selectedCategoryId,
                            selectedCategoryName
                    )
            findNavController().navigate(action)

            getEtAccountsSearchBox().setText("")
        })
    }

    @SuppressLint("InflateParams")
    private fun setOnLongClick() {                                                                  // Set item long click
        getLvAccountsContainer().onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedCategory = getLvAccountsContainer().getItemAtPosition(pos).toString()
            val selectedCategoryId = selectedCategory.substring(0, 5)
            val selectedCategoryName = selectedCategory.substring(5, selectedCategory.length)

            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_edit_delete, null)
            val tvCategoryPlatformEdit: TextView =
                    dialogView.findViewById(R.id.tvCategoryPlatformEdit)
            val tvCategoryPlatformDelete: TextView =
                    dialogView.findViewById(R.id.tvCategoryPlatformDelete)

            tvCategoryPlatformEdit.setText(R.string.accounts_edit_category)
            tvCategoryPlatformDelete.setText(R.string.accounts_delete_category)
            builder.setView(dialogView)

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                context, R.drawable.layout_alert_dialog
                        )
                )
                setTitle("Category: $selectedCategoryName")
                show()
            }

            closeKeyboard()

            val llCategoryPlatformEdit: LinearLayout =
                    dialogView.findViewById(R.id.llCategoryPlatformEdit)
            val llCategoryPlatformDelete: LinearLayout =
                dialogView.findViewById(R.id.llCategoryPlatformDelete)

            llCategoryPlatformEdit.setOnClickListener {
                alert.cancel()
                showAddUpdateCategory(
                        "update",
                        selectedCategoryName,
                        selectedCategoryId
                )
            }

            llCategoryPlatformDelete.setOnClickListener {

            }

            true
        })
    }

    @SuppressLint("InflateParams")
    private fun showAddUpdateCategory(
            addOrUpdate: String,
            selectedCategoryName: String,
            selectedCategoryId: String
    ) {
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

        var buttonName = ""
        var dialogTitle = ""
        var toastMes = ""

        if (addOrUpdate == "add") {
            buttonName = "Add"
            dialogTitle = resources.getString(R.string.accounts_new_category)
            toastMes = resources.getString(R.string.many_nothing_to_add)
        } else if (addOrUpdate == "update") {
            buttonName = "Update"
            dialogTitle = resources.getString(R.string.accounts_edit_category) + ": " + selectedCategoryName
            toastMes = resources.getString(R.string.many_nothing_to_update)

            etAccountsAddNew.apply {
                setText(selectedCategoryName)
                setSelection(etAccountsAddNew.text.length)
            }
        }

        builder.setPositiveButton(buttonName) { _: DialogInterface, _: Int ->
            val categoryName = etAccountsAddNew.text.toString()

            if (categoryName.isNotEmpty()) {
                addOrUpdateCategory(
                        addOrUpdate,
                        categoryName,
                        selectedCategoryId,
                        selectedCategoryName
                )
            } else {
                val toast: Toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        toastMes,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }

            view?.apply {
                postDelayed(
                        {
                            closeKeyboard()
                        }, 50
                )
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            dialog.cancel()

            view?.apply {
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
            setTitle(dialogTitle)
            show()
        }
    }
}