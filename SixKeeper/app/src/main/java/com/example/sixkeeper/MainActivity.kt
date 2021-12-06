package com.example.sixkeeper

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class MainActivity : ChangeStatusBarToWhiteClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor()
        goTo()
    }

    private fun goTo() {
        if (!InternetConnectionClass().isConnected()) {
            val tvMainMessage: TextView = findViewById(R.id.tvMainMessage)
            tvMainMessage.setText(R.string.main_internet_mes)
        }

        val databaseHandlerClass = DatabaseHandlerClass(this)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val encryptionClass = EncryptionClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        if (userAccList.isEmpty()) {
            goToLoginActivity()

            return
        }

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        val databaseReference = firebaseDatabase.getReference(userId)
        val statusReference = databaseReference.child("status")
        var status = ""
        var deviceId = ""
        val button = Button(this)

        statusReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    val decryptedValue = encryptionClass.decrypt(value, userId)
                    val splitValue = decryptedValue.split("ramjcammjar")
                    status = splitValue[0]

                    if (splitValue.size == 2) {
                        deviceId = splitValue[1]
                    }

                    button.performClick()
                } else {
                    goToLoginActivity()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            if (1.toString() == status && deviceId == getUniquePseudoID()) {                        // Go to login (Master PIN)
                val goToMasterPINActivity =
                        Intent(this, MasterPINActivity::class.java)
                startActivity(goToMasterPINActivity)
                overridePendingTransition(
                        R.anim.anim_enter_bottom_to_top_2,
                        R.anim.anim_0
                )

                this.finish()
            } else {                                                                                // Go to login (Username and Password)
                goToLoginActivity()
            }
        }
    }

    private fun goToLoginActivity() {
        val goToLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(goToLoginActivity)

        this.finish()
    }

    private fun getUniquePseudoID(): String {
        @Suppress("DEPRECATION")
        val mSzDevIDShort = "35" +
                Build.BOARD.length % 10 +
                Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 +
                Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 +
                Build.HOST.length % 10 +
                Build.ID.length % 10 +
                Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 +
                Build.TYPE.length % 10 +
                Build.USER.length % 10
        var serial: String

        try {
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            serial = Build::class.java.getField("SERIAL")[null].toString()

            return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: java.lang.Exception) {
            serial = "serial"
        }

        return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }
}