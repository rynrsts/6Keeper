package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class ResetPasswordFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var etResetPasswordNewPass: EditText
    private lateinit var etResetPasswordConfirmPass: EditText
    private lateinit var ivResetPasswordNewTogglePass: ImageView
    private lateinit var ivResetPasswordConfirmTogglePass: ImageView

    private var userId = ""
    private lateinit var key: ByteArray
    private var newPass = ""
    private var newPassVisibility: Int = 0
    private var confirmPassVisibility: Int = 0
    private var usernameVal = ""
    private var passwordVal = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        setOnclick()
        setEditTextOnChange()
        setImageViewOnClick()
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

        etResetPasswordNewPass = appCompatActivity.findViewById(R.id.etResetPasswordNewPass)
        etResetPasswordConfirmPass = appCompatActivity.findViewById(R.id.etResetPasswordConfirmPass)
        ivResetPasswordNewTogglePass =
                appCompatActivity.findViewById(R.id.ivResetPasswordNewTogglePass)
        ivResetPasswordConfirmTogglePass =
                appCompatActivity.findViewById(R.id.ivResetPasswordConfirmTogglePass)

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
        databaseReference = firebaseDatabase.getReference(userId)

        databaseReference = firebaseDatabase.getReference(userId)

        val usernameRef = databaseReference.child("username")
        val passwordRef = databaseReference.child("password")

        usernameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                val decryptedValue = encryptionClass.decrypt(value, key)
                usernameVal = encryptionClass.hash(decryptedValue)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        passwordRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                passwordVal = dataSnapshot.getValue(String::class.java).toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun setOnclick() {
        val acbResetPasswordReset: Button =
                appCompatActivity.findViewById(R.id.acbResetPasswordReset)

        acbResetPasswordReset.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val tvResetPasswordConfirmPassNote: TextView =
                        appCompatActivity.findViewById(R.id.tvResetPasswordConfirmPassNote)

                newPass = etResetPasswordNewPass.text.toString()
                val confirmPass = etResetPasswordConfirmPass.text.toString()

                if (newPass.isNotEmpty() && confirmPass.isNotEmpty()) {
                    val encryptedInput = encryptionClass.hash(newPass)

                    if (
                            isPasswordValid(newPass) && confirmPass == newPass &&
                            !usernameVal.contentEquals(encryptedInput) &&
                            !passwordVal.contentEquals(encryptedInput)
                    ) {
                        tvResetPasswordConfirmPassNote.text = ""

                        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                        builder.setMessage(R.string.reset_password_alert)
                        builder.setCancelable(false)

                        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                            if (InternetConnectionClass().isConnected()) {

                                var actionLogId = 1000001
                                val lastId = databaseHandlerClass.getLastIdOfActionLog()

                                val calendar: Calendar = Calendar.getInstance()
                                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                                val date = dateFormat.format(calendar.time)

                                if (lastId.isNotEmpty()) {
                                    actionLogId =
                                            Integer.parseInt(
                                                    encryptionClass.decrypt(lastId, key)
                                            ) + 1
                                }

                                databaseReference.child("password").setValue(encryptedInput)
                                databaseReference.child("pwWrongAttempt").setValue("")
                                databaseReference.child("pwLockTime").setValue("")

                                databaseHandlerClass.addEventToActionLog(                           // Add event to Action Log
                                        UserActionLogModelClass(
                                                encryptionClass.encrypt(
                                                        actionLogId.toString(), key
                                                ),
                                                encryptionClass.encrypt("App account " +
                                                        "password was modified.", key),
                                                encryptionClass.encrypt(date, key)
                                        )
                                )

                                view?.apply {
                                    postDelayed(
                                            {
                                                appCompatActivity.finish()
                                                appCompatActivity.overridePendingTransition(
                                                        R.anim.anim_enter_left_to_right_2,
                                                        R.anim.anim_exit_left_to_right_2
                                                )
                                            }, 250
                                    )
                                }

                                val toast: Toast = Toast.makeText(
                                        appCompatActivity.applicationContext,
                                        R.string.reset_password_mes,
                                        Toast.LENGTH_SHORT
                                )
                                toast.apply {
                                    setGravity(Gravity.CENTER, 0, 0)
                                    show()
                                }
                            } else {
                                internetToast()
                            }
                        }
                        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                            dialog.cancel()
                        }

                        val alert: AlertDialog = builder.create()
                        alert.setTitle(R.string.many_alert_title_confirm)
                        alert.show()

                        it.apply {
                            acbResetPasswordReset.isClickable = false                               // Set un-clickable for 1 second
                            postDelayed(
                                    {
                                        acbResetPasswordReset.isClickable = true
                                    }, 1000
                            )
                        }
                    } else {
                        if (confirmPass == newPass) {
                            tvResetPasswordConfirmPassNote.text = ""
                        } else if (confirmPass != newPass) {
                            tvResetPasswordConfirmPassNote.setText(R.string.many_validate_confirm_pass)
                        }

                        if (
                                isPasswordValid(newPass) && confirmPass == newPass &&
                                usernameVal.contentEquals(encryptedInput)
                        ) {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_password_mes_2,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        } else if (
                                isPasswordValid(newPass) && confirmPass == newPass &&
                                !usernameVal.contentEquals(encryptedInput) &&
                                passwordVal.contentEquals(encryptedInput)
                        ) {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_pass_not_the_same_mes,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
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

    private fun isPasswordValid(s: String): Boolean {                                               // Accept 1 lowercase, uppercase, number, (.), (_) and (-) only
        val exp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])(?=.*[._-])(?=\\S+\$)(?=.{8,})(^[a-zA-Z0-9._-]+\$)"
        val pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun setEditTextOnChange() {                                                             // Set action when EditText changes
        etResetPasswordNewPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                newPass = etResetPasswordNewPass.text.toString()

                if (newPass.isNotEmpty()) {
                    if (newPassVisibility == 0) {
                        ivResetPasswordNewTogglePass.setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        newPassVisibility = 1

                        etResetPasswordNewPass.apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(etResetPasswordNewPass.text.length)
                        }
                    }
                } else {
                    ivResetPasswordNewTogglePass.setImageResource(0)
                    newPassVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        etResetPasswordConfirmPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val confirmPass = etResetPasswordConfirmPass.text.toString()

                if (confirmPass.isNotEmpty()) {
                    if (confirmPassVisibility == 0) {
                        ivResetPasswordConfirmTogglePass.setImageResource(
                                R.drawable.ic_visibility_off_gray
                        )
                        confirmPassVisibility = 1

                        etResetPasswordConfirmPass.apply {
                            transformationMethod = PasswordTransformationMethod()
                            setSelection(etResetPasswordConfirmPass.text.length)
                        }
                    }
                } else {
                    ivResetPasswordConfirmTogglePass.setImageResource(0)
                    confirmPassVisibility = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setImageViewOnClick() {                                                             // Set action when image was clicked
        ivResetPasswordNewTogglePass.setOnClickListener {
            when (newPassVisibility) {
                1 -> {                                                                              // Show password
                    ivResetPasswordNewTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    etResetPasswordNewPass.apply {
                        transformationMethod = null
                        setSelection(etResetPasswordNewPass.text.length)
                    }
                    newPassVisibility = 2
                }
                2 -> {                                                                              // Hide password
                    ivResetPasswordNewTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    etResetPasswordNewPass.apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(etResetPasswordNewPass.text.length)
                    }
                    newPassVisibility = 1
                }
            }
        }

        ivResetPasswordConfirmTogglePass.setOnClickListener {
            when (confirmPassVisibility) {
                1 -> {                                                                              // Show confirm password
                    ivResetPasswordConfirmTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_gray)
                    }
                    etResetPasswordConfirmPass.apply {
                        transformationMethod = null
                        setSelection(etResetPasswordConfirmPass.text.length)
                    }
                    confirmPassVisibility = 2
                }
                2 -> {                                                                              // Hide confirm password
                    ivResetPasswordConfirmTogglePass.apply {
                        setImageResource(R.drawable.ic_visibility_off_gray)
                    }
                    etResetPasswordConfirmPass.apply {
                        transformationMethod = PasswordTransformationMethod()
                        setSelection(etResetPasswordConfirmPass.text.length)
                    }
                    confirmPassVisibility = 1
                }
            }
        }
    }
}