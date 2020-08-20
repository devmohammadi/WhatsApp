package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.adapter.ChatAdapter
import com.fmohammadi.whatsapp.model.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    var mUserId: String? = null
    var mFirebaseDatabase: DatabaseReference? = null
    var mUser: FirebaseUser? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        mUser = FirebaseAuth.getInstance().currentUser

        mUserId = intent!!.extras!!.get("userId").toString().trim()
        supportActionBar!!.title = intent.extras!!.getString("userName")

        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mFirebaseDatabase = FirebaseDatabase.getInstance().reference

        mFirebaseAdapter = ChatAdapter(this, mUser!!, mFirebaseDatabase!!)

        recyclerViewChat.layoutManager = mLinearLayoutManager
        recyclerViewChat.adapter = mFirebaseAdapter


        sendMessage.setOnClickListener {
            if (intent.extras!!.get("userName").toString() != "") {
                var currentUserName = intent.extras!!.getString("userName")

                var mCurrentUserId = mUser!!.uid


                var friendlyMessage = FriendlyMessage()
                friendlyMessage.idMessage = mCurrentUserId
                friendlyMessage.nameMessageReceive = currentUserName!!.toString().trim()
                friendlyMessage.textMessage = editSendMessage.text.toString().trim()
                friendlyMessage.timeMessage = System.currentTimeMillis()


                mFirebaseDatabase!!.child("Users")
                    .child(mCurrentUserId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var name = dataSnapshot.child("userName").value.toString()
                            friendlyMessage.nameMessageSend = name
                            mFirebaseDatabase!!.child("messages").push().setValue(friendlyMessage)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }

                    })
                editSendMessage.setText("")
            }

        }
    }

}