package com.example.sixkeeper

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        setActionBarTitle()
        disableMenuItem()
        closeKeyboard()
        populateCategories("")
        setOnClick()
        setOnLongClick()
        setEditTextOnChange()
    }

    private fun setOnClick() {
        val ivAccountsAddCategories: ImageView =
                getAppCompatActivity().findViewById(R.id.ivAccountsAddCategories)

        ivAccountsAddCategories.setOnClickListener {
            showAddUpdateCategory("add", "", "")

            it.apply {
                ivAccountsAddCategories.isClickable = false                                         // Set un-clickable for 1 second
                postDelayed(
                        {
                            ivAccountsAddCategories.isClickable = true
                        }, 1000
                )
            }
        }

        getLvAccountsContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedCategory = getLvAccountsContainer().getItemAtPosition(i).toString()
            val selectedCategoryValue = selectedCategory.split("ramjcammjar")
            val selectedCategoryId = selectedCategoryValue[0]
            val selectedCategoryName = selectedCategoryValue[1]

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
            val selectedCategoryValue = selectedCategory.split("ramjcammjar")
            val selectedCategoryId = selectedCategoryValue[0]
            val selectedCategoryName = selectedCategoryValue[1]
            val selectedCategoryNum = selectedCategoryValue[2]

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
                alert.cancel()
                showDeleteCategory(selectedCategoryId, selectedCategoryName, selectedCategoryNum)
            }

            true
        })
    }

    private fun setEditTextOnChange() {                                                             // Search real-time
        getEtAccountsSearchBox().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val search = getEtAccountsSearchBox().text.toString()
                populateCategories(search)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}