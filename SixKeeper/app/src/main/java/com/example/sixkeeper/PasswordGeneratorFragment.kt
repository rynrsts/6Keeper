package com.example.sixkeeper

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

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
        setButtonOnClick()
        setOnCheckBoxChecked()
    }

    private fun setButtonOnClick() {
        val acbPassGeneratorGenerate: Button =
                getAppCompatActivity().findViewById(R.id.acbPassGeneratorGenerate)

        acbPassGeneratorGenerate.setOnClickListener {
            setPasswordGeneratorLength(getEtPassGeneratorLength().text.toString())

            if (getPassGeneratorLength().isNotEmpty()) {
                if (getPassGeneratorLength() != "0") {
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
                    }
                    else {
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
    }

    private fun setOnCheckBoxChecked() {
        getCbPassGeneratorSpecialChar().setOnClickListener {
            val llPassGeneratorSpecialChar: LinearLayout =
                    getAppCompatActivity().findViewById(R.id.llPassGeneratorSpecialChar)

            if (getCbPassGeneratorSpecialChar().isChecked) {
                createSpecialCharViews()

                llPassGeneratorSpecialChar.apply {
                    addView(getLlSpecialCharacter())
                }
            } else {
                getLlSpecialCharacter().apply {
                    removeAllViews()
                }
                llPassGeneratorSpecialChar.apply {
                    removeAllViews()
                }
            }
        }
    }
}