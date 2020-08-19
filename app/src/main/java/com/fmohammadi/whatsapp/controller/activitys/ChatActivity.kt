package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.model.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {

    var mUserId: String? = null
    var mFirebaseDatabase: DatabaseReference? = null
    var mUser: FirebaseUser? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var data = intent!!.extras!!
        mUser = FirebaseAuth.getInstance().currentUser
        mUserId = data.getString("userId")
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        mFirebaseDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
            FriendlyMessage::class.java,
            R.layout.item_message,
            MessageViewHolder::class.java,
            mFirebaseDatabase!!.child("message")
        ) {
            override fun populateViewHolder(
                messageViewHolder: MessageViewHolder?,
                friendlyMessage: FriendlyMessage?,
                position: Int
            ) {

            }

        }


        if (data != null) {
            //set title by userName
        }
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}