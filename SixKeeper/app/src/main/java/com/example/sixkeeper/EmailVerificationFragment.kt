package com.example.sixkeeper

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

@Suppress("DEPRECATION")
class EmailVerificationFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private var mAuth: FirebaseAuth? = null

    private lateinit var etEmailVerificationEmail: EditText

    private var userId = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_email_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setEmail()
    }

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

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)

        mAuth = FirebaseAuth.getInstance()

        etEmailVerificationEmail = appCompatActivity.findViewById(R.id.etEmailVerificationEmail)
    }

    private fun setEmail() {
        val button = Button(appCompatActivity)
        val emailRef = databaseReference.child("email")
        var email = ""
        var count = 0

        emailRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                email = encryptionClass.decrypt(value, userId)
                count++

                if (count == 1) {
                    button.performClick()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            etEmailVerificationEmail.setText(email)
            setOnClick()
        }
    }

    private fun setOnClick() {
        val acbEmailVerificationVerify: Button =
                appCompatActivity.findViewById(R.id.acbEmailVerificationVerify)

        acbEmailVerificationVerify.setOnClickListener {
            val email = etEmailVerificationEmail.text.toString()
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setAndroidPackageName(
                            requireContext().packageName,
                            true,
                            null
                    )
                    .setHandleCodeInApp(true)
                    .setUrl("https://sixkeeper.page.link")
                    .build()
//            var emailLink = ""
//            val button = Button(appCompatActivity)

            mAuth!!.sendSignInLinkToEmail(email, actionCodeSettings)                                // Send email
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.email_verification_email_sent,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }

//                            val intent = activity?.intent
//                            emailLink = intent?.data.toString()
//
//                            button.performClick()
                        } else {
                            Objects.requireNonNull(task.exception)?.printStackTrace()
                        }
                    }

//            button.setOnClickListener {
//                if (mAuth!!.isSignInWithEmailLink(emailLink)) {
//                    mAuth!!.signInWithEmailLink(email, emailLink)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    val forgotCredentialsActivity: ForgotCredentialsActivity =
//                                            activity as ForgotCredentialsActivity
//
//                                    forgotCredentialsActivity.manageForgotFragments(
//                                            forgotCredentialsActivity.getCredential()
//                                    )
//                                }
//                            }
//                } else {
//                    val toast: Toast = Toast.makeText(
//                            appCompatActivity.applicationContext,
//                            "error",
//                            Toast.LENGTH_SHORT
//                    )
//                    toast.apply {
//                        setGravity(Gravity.CENTER, 0, 0)
//                        show()
//                    }
//                }
//            }
        }
    }
}