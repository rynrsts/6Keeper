package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        populatePlatforms("")
        setOnClick()
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
            closeKeyboard()
        })
    }
}