package com.example.sixkeeper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

class UserAccountFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeKeyboard()
    }

    private fun closeKeyboard() {
        val appCompatActivity: AppCompatActivity = activity as AppCompatActivity

        val immKeyboard: InputMethodManager =
            appCompatActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                appCompatActivity.currentFocus?.windowToken,
                0
            )
        }
    }
}