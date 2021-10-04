package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController

class PasswordGeneratorFragment : PasswordGeneratorProcessClass() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_generator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        closeKeyboard()
        setOnClick()
    }

    private fun setOnClick() {
        val acbPassGeneratorGenerate: Button =
                getAppCompatActivity().findViewById(R.id.acbPassGeneratorGenerate)
        val clPassGeneratorView: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clPassGeneratorView)
        val clPassGeneratorSave: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clPassGeneratorSave)


        acbPassGeneratorGenerate.setOnClickListener {
            setPasswordGeneratorLength(getEtPassGeneratorLength().text.toString())

            if (getPassGeneratorLength().isNotEmpty()) {
                if (Integer.parseInt(getPassGeneratorLength()) != 0) {
                    if (Integer.parseInt(getPassGeneratorLength()) >= 4) {
                        val etPassGeneratorLength: EditText =
                                getAppCompatActivity().findViewById(R.id.etPassGeneratorLength)

                        val length: Int = etPassGeneratorLength.text.toString().toInt()

                        if (
                                getCbPassGeneratorLowercase().isChecked ||
                                getCbPassGeneratorUppercase().isChecked ||
                                getCbPassGeneratorSpecialChar().isChecked ||
                                getCbPassGeneratorNumber().isChecked
                        ) {
                            val generatedPass = generatePassword(length)
                            getTvPassGeneratorGeneratedPass().text = generatedPass
                            closeKeyboard()
                        } else {
                            val toast: Toast = Toast.makeText(
                                    getAppCompatActivity().applicationContext,
                                    R.string.pass_generator_select_char,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }
                    } else {
                        val toast: Toast = Toast.makeText(
                                getAppCompatActivity().applicationContext,
                                R.string.pass_generator_less_than_four,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            getAppCompatActivity().applicationContext,
                            R.string.pass_generator_zero_length,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            } else {
                val toast: Toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        R.string.pass_generator_fill_length,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }

        clPassGeneratorView.setOnClickListener {
            closeKeyboard()

            val goToConfirmActivity = Intent(
                    getAppCompatActivity(),
                    ConfirmActionActivity::class.java
            )

            @Suppress("DEPRECATION")
            startActivityForResult(goToConfirmActivity, 16914)
            getAppCompatActivity().overridePendingTransition(
                    R.anim.anim_enter_bottom_to_top_2,
                    R.anim.anim_0
            )

            it.apply {
                clPassGeneratorView.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed(
                        {
                            clPassGeneratorView.isClickable = true
                        }, 1000
                )
            }
        }

        clPassGeneratorSave.setOnClickListener {
            val generatedPass = getTvPassGeneratorGeneratedPass().text.toString()

            if (generatedPass != "") {
                saveGeneratedPass(generatedPass)
            } else {
                val toast: Toast = Toast.makeText(
                        getAppCompatActivity().applicationContext,
                        R.string.pass_generator_generate_pass,
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }

            it.apply {
                clPassGeneratorSave.isClickable = false                                             // Set un-clickable for 1 second
                postDelayed(
                        {
                            clPassGeneratorSave.isClickable = true
                        }, 1000
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                view?.apply {
                    postDelayed(
                            {
                                findNavController().navigate(                                       // Go to Saved Password
                                        R.id.action_passwordGeneratorFragment_to_savedPasswordFragment
                                )
                            }, 250
                    )
                }
            }
        }
    }
}