package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fmohammadi.whatsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    var mCurrentUser: FirebaseUser? = null
    var mDateBase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
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
        mDateBase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userName = dataSnapshot.child("userName").value.toString()
                var userEmail = dataSnapshot.child("userEmail").value.toString()
                var userStatus = dataSnapshot.child("userStatus").value.toString()
                var userImage = dataSnapshot.child("userImage").value.toString()

                supportActionBar!!.title = userName
                profile_default_name.text = userName
                profile_default_about.text = userStatus
                profile_default_email.text = userEmail

                Picasso.with(this@ProfileActivity)
                    .load(userImage)
                    .placeholder(R.drawable.profile)
                    .into(profile_image)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}