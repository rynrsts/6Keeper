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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class MobileNumberValidationFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var etMobileNumberGetOTP: EditText
    private lateinit var etMobileNumberEnterOTP: EditText

    private lateinit var verificationId: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mobile_number_validation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setMobileNumber()
        setOnclick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity

        etMobileNumberGetOTP = appCompatActivity.findViewById(R.id.etMobileNumberGetOTP)
        etMobileNumberEnterOTP = appCompatActivity.findViewById(R.id.etMobileNumberEnterOTP)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun setMobileNumber() {
        val databaseHandlerClass = DatabaseHandlerClass(attActivity)
        val encodingClass = EncodingClass()
        val userInfoList: List<UserInfoModelClass> = databaseHandlerClass.viewUserInfo()
        var mobileNumber = ""

        for (u in userInfoList) {
            mobileNumber = encodingClass.decodeData(u.mobileNumber)
        }

        etMobileNumberGetOTP.setText(mobileNumber)
    }

    private fun setOnclick() {
        val acbMobileNumberGetOTP: Button =
                appCompatActivity.findViewById(R.id.acbMobileNumberGetOTP)
        val acbMobileNumberEnterOTP: Button =
                appCompatActivity.findViewById(R.id.acbMobileNumberEnterOTP)

        acbMobileNumberGetOTP.setOnClickListener {
            val phone = "+63" + etMobileNumberGetOTP.text.toString()
            sendVerificationCode(phone)
        }

        acbMobileNumberEnterOTP.setOnClickListener {
            val otp = etMobileNumberEnterOTP.text.toString()

            if (otp.isNotEmpty()) {
                verifyCode(otp)
            } else {
                val toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        R.string.mobile_number_enter_otp_mes,
                        Toast.LENGTH_SHORT
                )
                toast?.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val forgotCredentialsActivity: ForgotCredentialsActivity =
                        activity as ForgotCredentialsActivity

                forgotCredentialsActivity.manageForgotFragments(
                        forgotCredentialsActivity.getCredential()
                )
            } else {
                val toast = Toast.makeText(
                        appCompatActivity.applicationContext,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                )
                toast?.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(attActivity)
                .setCallbacks(firebaseCallBack)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val firebaseCallBack: OnVerificationStateChangedCallbacks =
            object : OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode

            if (code != null) {
                etMobileNumberEnterOTP.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            val toast = Toast.makeText(
                    appCompatActivity.applicationContext,
                    e.message,
                    Toast.LENGTH_LONG
            )
            toast?.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }
}