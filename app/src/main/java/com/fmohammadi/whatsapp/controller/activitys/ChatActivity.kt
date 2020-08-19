package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.model.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

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

                if (friendlyMessage!!.textMessage != null) {
                    messageViewHolder!!.bindViews(friendlyMessage)
                    var mCurrentUserId = mUser!!.uid
                    var isMe = friendlyMessage!!.idMessage!! == mCurrentUserId

                    if (isMe) {
                        messageViewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_me)
                        mFirebaseDatabase!!.child("Users")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    var imageUrl =
                                        dataSnapshot.child("userThumbImage").value.toString()
                                    var name = dataSnapshot.child("userName").value.toString()

                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }

                            })
                    } else {
                        messageViewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_me)
                        mFirebaseDatabase!!.child("Users")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    var imageUrl =
                                        dataSnapshot.child("userThumbImage").value.toString()
                                    var name = dataSnapshot.child("userName").value.toString()

                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }

                            })
                    }
                }

            }

        }

        recyclerViewChat.layoutManager = mLinearLayoutManager
        recyclerViewChat.adapter = mFirebaseAdapter

        sendMessage.setOnClickListener {

        }
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageName: TextView? = null
        var messageText: TextView? = null
        var messageTime: TextView? = null
        var messageBg: LinearLayout = itemView.findViewById(R.id.messageBg)

        fun bindViews(friendlyMessage: FriendlyMessage?) {
            messageName = itemView.findViewById(R.id.tvMessageName)
            messageText = itemView.findViewById(R.id.tvMessage)
            messageTime = itemView.findViewById(R.id.tvMessageTime)

            messageName!!.text = friendlyMessage!!.nameMessage
            messageText!!.text = friendlyMessage!!.textMessage
            messageTime!!.text = friendlyMessage!!.timeMessage.toString()
        }
    }
}