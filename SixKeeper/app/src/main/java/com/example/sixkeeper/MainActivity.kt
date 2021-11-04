package com.example.sixkeeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        val databaseHandlerClass = DatabaseHandlerClass(this)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val encodingClass = EncodingClass()
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var userId = ""

        for (u in userAccList) {
            userId = encodingClass.decodeData(u.userId)
        }

        val databaseReference = firebaseDatabase.getReference(userId)
        val statusReference = databaseReference.child("status")
        var status = ""
        val button = Button(this)

        statusReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                status = dataSnapshot.getValue(String::class.java).toString()

                button.performClick()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            if (encodingClass.encodeData(1.toString()) == status) {                                 // Go to login (Master PIN)
                val goToMasterPINActivity =
                        Intent(this, MasterPINActivity::class.java)
                startActivity(goToMasterPINActivity)
                overridePendingTransition(
                        R.anim.anim_enter_bottom_to_top_2,
                        R.anim.anim_0
                )
            } else {                                                                                // Go to login (Username and Password)
                val goToLoginActivity = Intent(this, LoginActivity::class.java)
                startActivity(goToLoginActivity)
            }

            this.finish()
        }
    }
}