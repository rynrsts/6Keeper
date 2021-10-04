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
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

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
        setActionBarTitle()
        closeKeyboard()
        populatePlatforms("")
        setOnClick()
        setOnLongClick()
    }

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val ivSpecificCatSearchButton: ImageView =
                getAppCompatActivity().findViewById(R.id.ivSpecificCatSearchButton)
        val ivSpecificCatAddPlatforms: ImageView =
                getAppCompatActivity().findViewById(R.id.ivSpecificCatAddPlatforms)

        ivSpecificCatSearchButton.setOnClickListener {
            val search = getEtSpecificCatSearchBox().text.toString()

            populatePlatforms(search)
            closeKeyboard()
        }

        ivSpecificCatAddPlatforms.setOnClickListener {
            showAddUpdatePlatform("add", "", "")

            it.apply {
                ivSpecificCatAddPlatforms.isClickable = false                                       // Set un-clickable for 1 second
                postDelayed(
                        {
                            ivSpecificCatAddPlatforms.isClickable = true
                        }, 1000
                )
            }
        }

//        TODO: Fix bug when clicked many times
        getLvSpecificCatContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedPlatform = getLvSpecificCatContainer().getItemAtPosition(i).toString()
            val selectedPlatformId = selectedPlatform.substring(0, 5)
            val selectedPlatformName = selectedPlatform.substring(5, selectedPlatform.length)

            val action = SpecificCategoryFragmentDirections
                    .actionSpecificCategoryFragmentToSpecificPlatformFragment(
                            selectedPlatformId,
                            selectedPlatformName
                    )
            findNavController().navigate(action)

            getEtSpecificCatSearchBox().setText("")
        })
    }

    @SuppressLint("InflateParams")
    private fun setOnLongClick() {                                                                  // Set item long click
        getLvSpecificCatContainer().onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedPlatform = getLvSpecificCatContainer().getItemAtPosition(pos).toString()
            val selectedCPlatformId = selectedPlatform.substring(0, 5)
            val selectedPlatformName = selectedPlatform.substring(5, selectedPlatform.length)

            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.layout_accounts_edit_delete, null)
            val tvCategoryPlatformEdit: TextView =
                    dialogView.findViewById(R.id.tvCategoryPlatformEdit)
            val tvCategoryPlatformDelete: TextView =
                    dialogView.findViewById(R.id.tvCategoryPlatformDelete)

            tvCategoryPlatformEdit.setText(R.string.specific_category_edit_platform)
            tvCategoryPlatformDelete.setText(R.string.specific_category_delete_platform)
            builder.setView(dialogView)

            val alert: AlertDialog = builder.create()
            alert.apply {
                window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                                context, R.drawable.layout_alert_dialog
                        )
                )
                setTitle("Platform: $selectedPlatformName")
                show()
            }

            closeKeyboard()

            val llCategoryPlatformEdit: LinearLayout =
                    dialogView.findViewById(R.id.llCategoryPlatformEdit)
            val llCategoryPlatformDelete: LinearLayout =
                    dialogView.findViewById(R.id.llCategoryPlatformDelete)

            llCategoryPlatformEdit.setOnClickListener {
                alert.cancel()
                showAddUpdatePlatform(
                        "update",
                        selectedPlatformName,
                        selectedCPlatformId
                )
            }

            llCategoryPlatformDelete.setOnClickListener {

            }

            true
        })
    }

    @SuppressLint("InflateParams")
    private fun showAddUpdatePlatform(
            addOrUpdate: String,
            selectedPlatformName: String,
            selectedPlatformId: String
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

        tvAccountsAddNew.setText(R.string.specific_category_platform_name)
        ivAccountsAddNew.setImageResource(R.drawable.ic_globe_light_black)

        var buttonName = ""
        var dialogTitle = ""
        var toastMes = ""

        if (addOrUpdate == "add") {
            buttonName = "Add"
            dialogTitle = resources.getString(R.string.specific_category_new_platform)
            toastMes = resources.getString(R.string.many_nothing_to_add)
        } else if (addOrUpdate == "update") {
            buttonName = "Update"
            dialogTitle = resources.getString(R.string.specific_category_edit_platform) + ": " + selectedPlatformName
            toastMes = resources.getString(R.string.many_nothing_to_update)

            etAccountsAddNew.apply {
                setText(selectedPlatformName)
                setSelection(etAccountsAddNew.text.length)
            }
        }

        builder.setPositiveButton(buttonName) { _: DialogInterface, _: Int ->
            val platformName = etAccountsAddNew.text.toString()

            if (platformName.isNotEmpty()) {
                addOrUpdatePlatform(
                        addOrUpdate,
                        platformName,
                        selectedPlatformId,
                        selectedPlatformName
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
                            getAppCompatActivity(),
                            R.drawable.layout_alert_dialog
                    )
            )
            setTitle(dialogTitle)
            show()
        }
    }
}