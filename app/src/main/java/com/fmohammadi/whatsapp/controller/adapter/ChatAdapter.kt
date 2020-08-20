package com.fmohammadi.whatsapp.controller.adapter

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.model.FriendlyMessage
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatAdapter(
    var mContext: Context,
    var mUser: FirebaseUser,
    mDatabaseQuery: DatabaseReference
) :
    FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
        FriendlyMessage::class.java,
        R.layout.item_message,
        MessageViewHolder::class.java,
        mDatabaseQuery.child("messages")
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

            var mDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().reference

            if (isMe) {
                messageViewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_me)
                mDatabaseReference!!.child("Users")
                    .child(mCurrentUserId)
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
                messageViewHolder!!.messageBg.setBackgroundResource(R.drawable.chat_user)
                messageViewHolder!!.messageNameSend!!.textAlignment = (View.TEXT_ALIGNMENT_VIEW_START)
                messageViewHolder!!.messageText!!.setMargins(rightMarginDp = 25 )
                messageViewHolder!!.messageText!!.setMargins(leftMarginDp = 0)

                messageViewHolder!!.messageNameSend!!.setMargins(rightMarginDp = 25 )
                messageViewHolder!!.messageNameSend!!.setMargins(leftMarginDp = 0)

                messageViewHolder!!.messageTime!!.setMargins(rightMarginDp = 25 )
                messageViewHolder!!.messageTime!!.setMargins(leftMarginDp = 0)

                mDatabaseReference!!.child("Users")
                    .child(mCurrentUserId)
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

    fun Int.dpToPx(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }
}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var messageNameSend: TextView? = null
    var messageText: TextView? = null
    var messageTime: TextView? = null
    var messageBg: LinearLayout = itemView.findViewById(R.id.messageBg)

    fun bindViews(friendlyMessage: FriendlyMessage?) {
        messageNameSend = itemView.findViewById(R.id.tvMessageName)
        messageText = itemView.findViewById(R.id.tvMessage)
        messageTime = itemView.findViewById(R.id.tvMessageTime)

        messageNameSend!!.text = friendlyMessage!!.nameMessageSend
        messageText!!.text = friendlyMessage!!.textMessage
        messageTime!!.text = friendlyMessage.showTime(friendlyMessage.timeMessage!!)

    }
}