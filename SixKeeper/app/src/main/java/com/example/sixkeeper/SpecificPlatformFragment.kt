package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class SpecificPlatformFragment : SpecificPlatformProcessClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_platform, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setActionBarTitle()
        closeKeyboard()
        setOnClick()
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                getAppCompatActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    getAppCompatActivity().currentFocus?.windowToken,
                    0
            )
        }
    }

    private fun setOnClick() {
        val ivSpecificPlatAddAccounts: ImageView =
                getAppCompatActivity().findViewById(R.id.ivSpecificPlatAddAccounts)

        ivSpecificPlatAddAccounts.setOnClickListener {
            findNavController().navigate(                                                           // Go to Add Account
                R.id.action_specificPlatformFragment_to_addAccountFragment
            )
        }
    }


}