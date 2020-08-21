package com.fmohammadi.whatsapp.controller.activitys

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.model.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.fmohammadi.whatsapp.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custom_bar.view.*

class ChatActivity : AppCompatActivity() {

    var receiverUserId: String? = null
    var receiverUserName: String? = null
    var mFirebaseDatabaseRef: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = null
    var mUserDataBase: DatabaseReference? = null
    var currentUserName: String? = null
    var imageProfileLink: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mFirebaseUser = FirebaseAuth.getInstance().currentUser


        mUserDataBase =
            FirebaseDatabase.getInstance().reference.child("Users").child(mFirebaseUser!!.uid)

        receiverUserId = intent.extras!!.getString("userId")
        receiverUserName = intent.extras!!.getString("userName")

        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowCustomEnabled(true)

        var inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customBarView = inflater.inflate(R.layout.custom_bar, null)

        imageProfileLink = intent!!.extras!!.getString("userImage")

        customBarView.customBarUserName.text = receiverUserName
        Picasso.with(this)
            .load(imageProfileLink)
            .placeholder(R.drawable.profile)
            .into(customBarView.customBarImageUser)

        customBarView.customBar.setOnClickListener {
            var profileIntent = Intent(this, ProfileActivity::class.java)

            profileIntent.putExtra("userName", receiverUserName)
            profileIntent.putExtra("userId", receiverUserId)
            profileIntent.putExtra("userStatus", intent!!.extras!!.getString("userStatus"))
            profileIntent.putExtra("userImage", imageProfileLink)

            startActivity(profileIntent)

        }

        supportActionBar!!.customView = customBarView

        mUserDataBase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentUserName = dataSnapshot.child("userName").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
            FriendlyMessage::class.java,
            R.layout.item_message,
            MessageViewHolder::class.java,
            mFirebaseDatabaseRef!!.child("messages")
        ) {
            override fun populateViewHolder(
                viewHolder: MessageViewHolder?,
                frindlyMessage: FriendlyMessage?,
                position: Int
            ) {
                if (frindlyMessage!!.textMessage != null &&
                    ((frindlyMessage.nameMessageSend == currentUserName && frindlyMessage.nameMessageReceive == receiverUserName) ||
                            frindlyMessage.nameMessageSend == receiverUserName && frindlyMessage.nameMessageReceive == currentUserName)
                ) {
                    viewHolder!!.bindViews(frindlyMessage)

                    viewHolder.messagesBox.visibility = View.VISIBLE


                    var currentUserId = mFirebaseUser!!.uid

                    var isMe = frindlyMessage.idMessage!! == currentUserId


                    if (isMe) {
                        viewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_me)

                        mFirebaseDatabaseRef!!.child("Users")
                            .child(currentUserId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(data: DataSnapshot) {
                                    var imageUrl = data.child("userThumbImage").value.toString()
                                    var displayName = data.child("userName").value.toString()
                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })


                    } else {
                        viewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_user)
                        viewHolder!!.messageNameSend!!.textAlignment =
                            (View.TEXT_ALIGNMENT_VIEW_START)
                        viewHolder!!.messageText!!.setMargins(rightMarginDp = 25)
                        viewHolder!!.messageText!!.setMargins(leftMarginDp = 0)

                        viewHolder!!.messageNameSend!!.setMargins(rightMarginDp = 25)
                        viewHolder!!.messageNameSend!!.setMargins(leftMarginDp = 0)

                        viewHolder!!.messageTime!!.setMargins(rightMarginDp = 25)
                        viewHolder!!.messageTime!!.setMargins(leftMarginDp = 0)

                        viewHolder!!.messageBox!!.gravity = (Gravity.LEFT)

                        mFirebaseDatabaseRef!!.child("Users")
                            .child(receiverUserId!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(data: DataSnapshot) {
                                    var imageUrl = data.child("userThumbImage").value.toString()
                                    var displayName = data.child("userName").value.toString()
                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })
                    }
                }
            }


            fun View.setMargins(
                leftMarginDp: Int? = null,
                topMarginDp: Int? = null,
                rightMarginDp: Int? = null,
                bottomMarginDp: Int? = null
            ) {
                if (layoutParams is ViewGroup.MarginLayoutParams) {
                    val params = layoutParams as ViewGroup.MarginLayoutParams
                    leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
                    topMarginDp?.run { params.topMargin = this.dpToPx(context) }
                    rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
                    bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
                    requestLayout()
                }
            }


            fun Int.dpToPx(mContext: Context): Int {
                val metrics = mContext.resources.displayMetrics
                return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    this.toFloat(),
                    metrics
                )
                    .toInt()
            }
        }

        recyclerViewChat.layoutManager = mLinearLayoutManager
        recyclerViewChat.adapter = mFirebaseAdapter

        sendMessage.setOnClickListener {
            if (intent.extras!!.get("userName").toString() != "") {

                var mCurrentUserId = mFirebaseUser!!.uid

                var friendlyMessage = FriendlyMessage(
                    mCurrentUserId,
                    currentUserName!!,
                    receiverUserName!!,
                    editSendMessage.text.toString().trim(),
                    System.currentTimeMillis()
                )

                mFirebaseDatabaseRef!!.child("messages").push().setValue(friendlyMessage)
                editSendMessage.setText("")

            }

        }
    }

}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var messageNameSend: TextView? = null
    var messageText: TextView? = null
    var messageTime: TextView? = null
    var messageBg: LinearLayout = itemView.findViewById(R.id.messageBg)
    var messageBox: RelativeLayout = itemView.findViewById(R.id.boxMessage)
    var messagesBox: ConstraintLayout = itemView.findViewById(R.id.boxMessages)

    fun bindViews(friendlyMessage: FriendlyMessage?) {
        messageNameSend = itemView.findViewById(R.id.tvMessageName)
        messageText = itemView.findViewById(R.id.tvMessage)
        messageTime = itemView.findViewById(R.id.tvMessageTime)

        messageNameSend!!.text = friendlyMessage!!.nameMessageSend
        messageText!!.text = friendlyMessage!!.textMessage
        messageTime!!.text = friendlyMessage.showTime(friendlyMessage.timeMessage!!)

    }
}
