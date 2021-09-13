package com.example.sixkeeper

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
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
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
            findNavController().navigate(                                                           // Go to Saved Password
                    R.id.action_passwordGeneratorFragment_to_savedPasswordFragment
            )
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
        }
    }
}