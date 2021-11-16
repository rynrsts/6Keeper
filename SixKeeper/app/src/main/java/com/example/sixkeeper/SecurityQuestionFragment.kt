package com.example.sixkeeper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class SecurityQuestionFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var etSecurityQuestionFirstName: EditText
    private lateinit var etSecurityQuestionLastName: EditText
    private lateinit var etSecurityQuestionBirthDate: EditText
    private lateinit var etSecurityQuestionEmail: EditText
    private lateinit var etSecurityQuestionMobileNumber: EditText

    private var userId = ""
    private lateinit var key: ByteArray
    private lateinit var firstNameD: String
    private lateinit var lastNameD: String
    private lateinit var birthDateD: String
    private lateinit var emailD: String
    private lateinit var mobileNumberD: String

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private var lastLength = 0
    private lateinit var email: String
    private lateinit var mobileNumber: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_security_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setBirthDateOnClick()
        setEditTextOnChange()
        setButtonOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userAccList = databaseHandlerClass.validateUserAcc()

        etSecurityQuestionFirstName =
                appCompatActivity.findViewById(R.id.etSecurityQuestionFirstName)
        etSecurityQuestionLastName =
                appCompatActivity.findViewById(R.id.etSecurityQuestionLastName)
        etSecurityQuestionBirthDate =
                appCompatActivity.findViewById(R.id.etSecurityQuestionBirthDate)
        etSecurityQuestionEmail = appCompatActivity.findViewById(R.id.etSecurityQuestionEmail)
        etSecurityQuestionMobileNumber =
                appCompatActivity.findViewById(R.id.etSecurityQuestionMobileNumber)

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
        databaseReference = firebaseDatabase.getReference(userId)

        val firstNameRef = databaseReference.child("firstName")
        val lastNameRef = databaseReference.child("lastName")
        val birthDateRef = databaseReference.child("birthDate")
        val emailRef = databaseReference.child("email")
        val mobileNumberRef = databaseReference.child("mobileNumber")

        firstNameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                firstNameD = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        lastNameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                lastNameD = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        birthDateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                birthDateD = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        emailRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                emailD = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        mobileNumberRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mobileNumberD = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setBirthDateOnClick() {
        etSecurityQuestionBirthDate.setOnClickListener {
            etSecurityQuestionBirthDate.setSelection(
                    etSecurityQuestionBirthDate.text.length
            )
        }
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        etSecurityQuestionBirthDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                birthDate = etSecurityQuestionBirthDate.text.toString()

                if (birthDate.isNotEmpty()) {
                    if (
                            (lastLength == 0 && birthDate.length == 1) ||
                            (lastLength == 3 && birthDate.length == 4) ||
                            (lastLength == 6 && birthDate.length == 7) ||
                            (lastLength == 7 && birthDate.length == 8) ||
                            (lastLength == 8 && birthDate.length == 9) ||
                            (lastLength == 9 && birthDate.length == 10)
                    ) {
                        lastLength++
                    } else if (
                            (lastLength == 1 && birthDate.isEmpty()) ||
                            (lastLength == 4 && birthDate.length == 3) ||
                            (lastLength == 7 && birthDate.length == 6) ||
                            (lastLength == 8 && birthDate.length == 7) ||
                            (lastLength == 9 && birthDate.length == 8) ||
                            (lastLength == 10 && birthDate.length == 9)
                    ) {
                        lastLength--
                    } else if (
                            (lastLength == 1 && birthDate.length == 2) ||
                            (lastLength == 4 && birthDate.length == 5)
                    ) {
                        val birthDateSlash = "$birthDate/"

                        etSecurityQuestionBirthDate.setText(birthDateSlash)
                        lastLength += 2
                    } else if (
                            (lastLength == 3 && birthDate.length == 2) ||
                            (lastLength == 6 && birthDate.length == 5)
                    ) {
                        etSecurityQuestionBirthDate.setText(
                                birthDate.substring(0, birthDate.length -1)
                        )
                        lastLength -= 2
                    }

                    etSecurityQuestionBirthDate.setSelection(
                            etSecurityQuestionBirthDate.text.length
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setButtonOnClick() {
        val acbSecurityQuestionConfirm: Button =
                appCompatActivity.findViewById(R.id.acbSecurityQuestionConfirm)

        acbSecurityQuestionConfirm.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                if (isNotEmpty()) {
                    val encryptedFirstName = encryptionClass.encrypt(firstName, key)
                    val encryptedLastName = encryptionClass.encrypt(lastName, key)
                    val encryptedBirthDate = encryptionClass.encrypt(birthDate, key)
                    val encryptedEmail = encryptionClass.encrypt(email, key)
                    val encryptedMobileNumber = encryptionClass.encrypt(mobileNumber, key)

                    val immKeyboard: InputMethodManager =
                            appCompatActivity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            ) as InputMethodManager

                    if (
                            encryptedFirstName == firstNameD &&
                            encryptedLastName == lastNameD &&
                            encryptedBirthDate == birthDateD &&
                            encryptedEmail == emailD &&
                            encryptedMobileNumber == mobileNumberD
                    ) {
                        val forgotCredentialsActivity: ForgotCredentialsActivity =
                                activity as ForgotCredentialsActivity

                        if (immKeyboard.isActive) {
                            immKeyboard.hideSoftInputFromWindow(                                    // Close keyboard
                                    appCompatActivity.currentFocus?.windowToken,
                                    0
                            )
                        }

                        forgotCredentialsActivity.manageForgotFragments(
                                forgotCredentialsActivity.getCredential()
                        )
                    } else {
                        if (immKeyboard.isActive) {
                            immKeyboard.hideSoftInputFromWindow(                                    // Close keyboard
                                    appCompatActivity.currentFocus?.windowToken,
                                    0
                            )
                        }

                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.security_question_invalid,
                                Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            appCompatActivity.applicationContext,
                            R.string.many_fill_missing_fields,
                            Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            } else {
                internetToast()
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun isNotEmpty(): Boolean {                                                             // Validate EditTexts are not empty
        firstName = etSecurityQuestionFirstName.text.toString()
        lastName = etSecurityQuestionLastName.text.toString()
        birthDate = etSecurityQuestionBirthDate.text.toString()
        email = etSecurityQuestionEmail.text.toString()
        mobileNumber = etSecurityQuestionMobileNumber.text.toString()

        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                birthDate.isNotEmpty() &&
                email.isNotEmpty() &&
                mobileNumber.isNotEmpty()
    }
}