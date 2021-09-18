package com.example.sixkeeper

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs


class UserAccountEditFragment : Fragment() {
    private val args: UserAccountEditFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpecifiedView()
    }

    private fun setSpecifiedView() {
        appCompatActivity = activity as AppCompatActivity

        when (args.userAccountEditId) {
            2 -> {
                val vsUserAccountEditContainer: ViewStub =
                    appCompatActivity.findViewById(R.id.vsUserAccountEditContainer)
                vsUserAccountEditContainer.layoutResource = R.layout.layout_user_edit_1
                vsUserAccountEditContainer.inflate()

                val tvUserEditLabel: TextView = appCompatActivity.findViewById(R.id.tvUserEditLabel)
                tvUserEditLabel.setText(R.string.many_first_name)

                val etUserEditTextBox: TextView =
                    appCompatActivity.findViewById(R.id.etUserEditTextBox)
                etUserEditTextBox.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                etUserEditTextBox.filters = arrayOf<InputFilter>(LengthFilter(30))

                val ivUserEditIcon: ImageView = appCompatActivity.findViewById(R.id.ivUserEditIcon)
                ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
            }
            3 -> {

            }
        }
    }
}