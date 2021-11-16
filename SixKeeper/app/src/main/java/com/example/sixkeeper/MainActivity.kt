package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
        }

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        val key = (userId + userId + userId.substring(0, 2)).toByteArray()
        val databaseReference = firebaseDatabase.getReference(userId)
        val statusReference = databaseReference.child("status")
        var status = ""
        val button = Button(this)

        statusReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    status = dataSnapshot.getValue(String::class.java).toString()

                    button.performClick()
                } else {
                    goToLoginActivity()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            if (encryptionClass.encrypt(1.toString(), key) == status) {                             // Go to login (Master PIN)
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
}