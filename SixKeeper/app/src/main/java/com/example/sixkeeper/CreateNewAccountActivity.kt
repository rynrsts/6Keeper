package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.jakewharton.processphoenix.ProcessPhoenix
import java.text.SimpleDateFormat
import java.util.*


class CreateNewAccountActivity : CreateNewAccountManageFragmentsClass() {
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseUserAccountModelClass: FirebaseUserAccountModelClass

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var birthDate: String
    private lateinit var email: String
    private var mobileNumber: Long = 0
    private lateinit var username: String
    private lateinit var password: String
    private var masterPin: Int = 0

    private lateinit var encryptedFirstName: String
    private lateinit var encryptedLastName: String
    private lateinit var encryptedBirthDate: String
    private lateinit var encryptedEmail: String
    private lateinit var encryptedMobileNumber: String
    private lateinit var encryptedUsername: String
    private lateinit var encryptedPassword: String
    private lateinit var encryptedMasterPin: String
    private lateinit var encryptedStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        setVariables()
        manageCreateNewAccFragments(getCreateNewAccP1())
        setButtonOnClick()
    }

    private fun setVariables() {
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUserAccountModelClass = FirebaseUserAccountModelClass()
    }

    private fun setButtonOnClick() {
        val ivActionBarBackArrow: ImageView = findViewById(R.id.ivActionBarBackArrow)

        ivActionBarBackArrow.setOnClickListener {
            if (getFragmentNum() == 1) {
                it.apply {
                    ivActionBarBackArrow.isClickable = false                                        // Set button un-clickable for 1 second
                    postDelayed(
                            {
                                ivActionBarBackArrow.isClickable = true
                            }, 1000
                    )
                }
            }

            onBackPressed()
        }
    }

    override fun onBackPressed() {                                                                  // Override back button function
        val immKeyboard: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(currentFocus?.windowToken, 0)                   // Close keyboard
        }

        when {
            getFragmentNum() == 1 -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.create_new_acc_alert_message)
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    val goToLoginActivity =
                            Intent(this, LoginActivity::class.java)

                    startActivity(goToLoginActivity)
                    overridePendingTransition(
                            R.anim.anim_enter_left_to_right_2,
                            R.anim.anim_exit_left_to_right_2
                    )

                    finish()
                }
                builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }

                val alert: AlertDialog = builder.create()
                alert.setTitle(R.string.many_alert_title)
                alert.show()
            }

//            TODO: Second Fragment
//            getFragmentNum() == 2 -> {
//                manageCreateNewAccFragments(getCreateNewAccP1())
//            }
            getFragmentNum() == 3 -> {
                manageCreateNewAccFragments(getCreateNewAccP1())
            }
            getFragmentNum() == 4 -> {
                manageCreateNewAccFragments(getCreateNewAccP3())
            }
        }
    }

    internal fun setCreateNewAccountP1Data(f: String, l: String, b: String, e: String, m: Long) {   // CreateNewAccountP1 data
        firstName = f
        lastName = l
        birthDate = b
        email = e
        mobileNumber = m
    }

    internal fun setCreateNewAccountP3Data(u: String, p: String) {                                  // CreateNewAccountP3 data
        username = u
        password = p
    }

    internal fun setCreateNewAccountP4Data(mp: Int) {                                               // CreateNewAccountP4 data
        masterPin = mp
    }

    @SuppressLint("SimpleDateFormat")
    internal fun saveAccount() {                                                                    // Save account data to database
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val userId: Int = (1000000..9999999).random()
        val key = (
                userId.toString() + userId.toString() + userId.toString().substring(0, 2)
                )
                .toByteArray()

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        val date: String = dateFormat.format(calendar.time)

        val encodedUserId = encryptionClass.encode(userId.toString())
        encryptedUsername = encryptionClass.encrypt(username, key)
        val aesPassword = encryptionClass.encrypt(password, key)
        encryptedPassword = encryptionClass.hash(aesPassword)
        val aesMasterPin = encryptionClass.encrypt(masterPin.toString(), key)
        encryptedMasterPin = encryptionClass.hash(aesMasterPin)
        encryptedStatus = encryptionClass.encrypt(0.toString(), key)
        encryptedFirstName = encryptionClass.encrypt(firstName, key)
        encryptedLastName = encryptionClass.encrypt(lastName, key)
        encryptedBirthDate = encryptionClass.encrypt(birthDate, key)
        encryptedEmail = encryptionClass.encrypt(email, key)
        encryptedMobileNumber = encryptionClass.encrypt(mobileNumber.toString(), key)

        val tableStatus = databaseHandlerClass.truncateAllTables()
        val userAccStatus = databaseHandlerClass.addUserAcc(
                UserAccModelClass(encodedUserId)
        )
        val userSettingsStatus = databaseHandlerClass.addSettings(
                UserSettingsModelClass(
                        encodedUserId,
                        encryptionClass.encrypt(0.toString(), key),
                        encryptionClass.encrypt(1.toString(), key),
                        encryptionClass.encrypt("10 sec", key),
                        encryptionClass.encrypt(0.toString(), key),
                )
        )

        if (
                tableStatus > -1 &&
                userAccStatus > -1 &&
                userSettingsStatus > -1
        ) {
            val toast: Toast = Toast.makeText(
                    applicationContext,
                    R.string.create_new_acc_success,
                    Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        val actionLogId = 1000001

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(actionLogId.toString(), key),
                        encryptionClass.encrypt("App account was created.", key),
                        encryptionClass.encrypt(date, key)
                )
        )

        databaseReference = firebaseDatabase.getReference(userId.toString())
        sendData()
    }

    private fun sendData() {
        val button = Button(this)
        var stack = 0

        firebaseUserAccountModelClass.setUsername(encryptedUsername)
        firebaseUserAccountModelClass.setPassword(encryptedPassword)
        firebaseUserAccountModelClass.setMasterPin(encryptedMasterPin)
        firebaseUserAccountModelClass.setStatus(encryptedStatus)
        firebaseUserAccountModelClass.setFirstName(encryptedFirstName)
        firebaseUserAccountModelClass.setLastName(encryptedLastName)
        firebaseUserAccountModelClass.setBirthDate(encryptedBirthDate)
        firebaseUserAccountModelClass.setEmail(encryptedEmail)
        firebaseUserAccountModelClass.setMobileNumber(encryptedMobileNumber)
        firebaseUserAccountModelClass.setProfilePhoto("")
        firebaseUserAccountModelClass.setPwWrongAttempt("")
        firebaseUserAccountModelClass.setPwLockTime("")
        firebaseUserAccountModelClass.setMPinWrongAttempt("")
        firebaseUserAccountModelClass.setFWrongAttempt("")
        firebaseUserAccountModelClass.setMPinLockTime("")
        firebaseUserAccountModelClass.setFnEditCount("")
        firebaseUserAccountModelClass.setLnEditCount("")
        firebaseUserAccountModelClass.setBdEditCount("")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(firebaseUserAccountModelClass)
                stack++

                if (stack == 2) {
                    button.performClick()
                }

                button.setOnClickListener {
                    ProcessPhoenix.triggerRebirth(applicationContext)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val toast: Toast = Toast.makeText(
                        applicationContext,
                        "Failed to add data $error",
                        Toast.LENGTH_SHORT
                )
                toast.apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            }
        })
    }
}