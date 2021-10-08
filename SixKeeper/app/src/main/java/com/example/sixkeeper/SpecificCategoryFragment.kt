package com.example.sixkeeper

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        setHierarchy()
        closeKeyboard()
        populatePlatforms("")
        setOnClick()
        setOnLongClick()
        setEditTextOnChange()
    }

    @SuppressLint("InflateParams")
    private fun setOnClick() {
        val ivSpecificCatAddPlatforms: ImageView =
                getAppCompatActivity().findViewById(R.id.ivSpecificCatAddPlatforms)

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

        getLvSpecificCatContainer().onItemClickListener = (OnItemClickListener { _, _, i, _ ->
            val selectedPlatform = getLvSpecificCatContainer().getItemAtPosition(i).toString()
            val selectedPlatformValue = selectedPlatform.split("ramjcammjar")
            val selectedPlatformId = selectedPlatformValue[0]
            val selectedPlatformName = selectedPlatformValue[1]

            val action = SpecificCategoryFragmentDirections
                    .actionSpecificCategoryFragmentToSpecificPlatformFragment(
                            selectedPlatformId,
                            selectedPlatformName,
                            getArgsSpecificCategoryName()
                    )
            findNavController().navigate(action)

            getEtSpecificCatSearchBox().setText("")
        })
    }

    @SuppressLint("InflateParams")
    private fun setOnLongClick() {                                                                  // Set item long click
        getLvSpecificCatContainer().onItemLongClickListener = (OnItemLongClickListener { _, _, pos, _ ->
            val selectedPlatform = getLvSpecificCatContainer().getItemAtPosition(pos).toString()
            val selectedPlatformValue = selectedPlatform.split("ramjcammjar")
            val selectedPlatformId = selectedPlatformValue[0]
            val selectedPlatformName = selectedPlatformValue[1]
            val selectedPlatformNum = selectedPlatformValue[2]

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
                        selectedPlatformId
                )
            }

            llCategoryPlatformDelete.setOnClickListener {
                alert.cancel()
                showDeletePlatform(selectedPlatformId, selectedPlatformName, selectedPlatformNum)
            }

            true
        })
    }

    private fun setEditTextOnChange() {                                                             // Search real-time
        getEtSpecificCatSearchBox().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val search = getEtSpecificCatSearchBox().text.toString()
                populatePlatforms(search)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}