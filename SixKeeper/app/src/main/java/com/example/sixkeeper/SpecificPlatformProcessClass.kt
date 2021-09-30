package com.example.sixkeeper

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

open class SpecificPlatformProcessClass: Fragment() {
    private val args: SpecificPlatformFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity

    fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
    }

    fun setActionBarTitle() {
        val tAppBarToolbar: Toolbar = appCompatActivity.findViewById(R.id.tAppBarToolbar)
        tAppBarToolbar.title = args.specificPlatformName
    }
}