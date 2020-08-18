package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fmohammadi.whatsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {
    var mCurrentUser: FirebaseUser? = null
    var mDateBase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onStart() {
        super.onStart()
        var data = intent!!.extras
        if (data != null) {
            userId = data.get("userId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mDateBase = FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)

            setUpProfile()
        }
    }

    private fun setUpProfile() {
        mDateBase!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userName = dataSnapshot.child("userName").value.toString()
                var userEmail = dataSnapshot.child("userEmail").value.toString()
                var userStatus = dataSnapshot.child("userStatus").value.toString()
                var userImage = dataSnapshot.child("userImage").value.toString()



            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}