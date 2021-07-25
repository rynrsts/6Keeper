package com.example.sixkeeper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class CreateNewAccountManageFragmentsClass : AppCompatActivity() {
    private val createNewAccP1 = "createNewAccP1"
    private val createNewAccP2 = "createNewAccP2"
    private val createNewAccP3 = "createNewAccP3"
    private val createNewAccP4 = "createNewAccP4"
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val createNewAccountP1Fragment: Fragment = CreateNewAccountP1Fragment()
    private val createNewAccountP2Fragment: Fragment = CreateNewAccountP2Fragment()
    private val createNewAccountP3Fragment: Fragment = CreateNewAccountP3Fragment()
    private val createNewAccountP4Fragment: Fragment = CreateNewAccountP4Fragment()
    private var fragmentNum = 0

    fun getCreateNewAccP1(): String {
        return createNewAccP1
    }

    fun getCreateNewAccP2(): String {
        return createNewAccP2
    }

    fun getCreateNewAccP3(): String {
        return createNewAccP3
    }

    fun getCreateNewAccP4(): String {
        return createNewAccP4
    }

    fun getFragmentNum(): Int {
        return fragmentNum
    }

    fun manageCreateNewAccFragments(selectedFragment: String) {
        when (selectedFragment) {
            createNewAccP1 -> {                                                                     // First fragment
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 2) {
                        setCustomAnimations(
                                R.anim.anim_enter_left_to_right_1,
                                R.anim.anim_exit_left_to_right_1
                        )
                    }

                    replace(R.id.clCreateNewAccContainer, createNewAccountP1Fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 1
            }
            createNewAccP2 -> {                                                                     // Second fragment
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 1) {
                        setCustomAnimations(
                                R.anim.anim_enter_right_to_left_1,
                                R.anim.anim_exit_right_to_left_1
                        )
                    } else if (fragmentNum == 3) {
                        setCustomAnimations(
                                R.anim.anim_enter_left_to_right_1,
                                R.anim.anim_exit_left_to_right_1
                        )
                    }

                    replace(R.id.clCreateNewAccContainer, createNewAccountP2Fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 2
            }
            createNewAccP3 -> {                                                                     // Third fragment
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 2) {
                        setCustomAnimations(
                                R.anim.anim_enter_right_to_left_1,
                                R.anim.anim_exit_right_to_left_1
                        )
                    } else if (fragmentNum == 4) {
                        setCustomAnimations(
                                R.anim.anim_enter_left_to_right_1,
                                R.anim.anim_exit_left_to_right_1
                        )
                    }

                    replace(R.id.clCreateNewAccContainer, createNewAccountP3Fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 3
            }
            createNewAccP4 -> {                                                                     // Fourth fragment
                fragmentManager.beginTransaction().apply {
                    if (fragmentNum == 3) {
                        setCustomAnimations(
                                R.anim.anim_enter_right_to_left_1,
                                R.anim.anim_exit_right_to_left_1
                        )
                    }

                    replace(R.id.clCreateNewAccContainer, createNewAccountP4Fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }

                fragmentNum = 4
            }
        }
    }
}