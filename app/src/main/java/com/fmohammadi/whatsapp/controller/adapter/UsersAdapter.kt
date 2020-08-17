package com.fmohammadi.whatsapp.controller.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.model.Users
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(var mContext: Context, mDatabaseQuery: DatabaseReference) :
    FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(
        Users::class.java,
        R.layout.users_row,
        ViewHolder::class.java,
        mDatabaseQuery
    ) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user_Name: String? = null
        var user_About: String? = null
        var user_LinkProfile: String? = null

        fun bindViews(mContext: Context, user: Users?) {
            var userName = itemView.findViewById<TextView>(R.id.name_users)
            var userAbout = itemView.findViewById<TextView>(R.id.about_users)
            var userProfileImage = itemView.findViewById<CircleImageView>(R.id.image_profile_users)

            user_Name = user!!.userName
            user_About = user!!.userStatus
            user_LinkProfile = user!!.userImage

            userName.text = user.userName
            userAbout.text = user.userStatus
            Picasso.with(mContext)
                .load(user_LinkProfile)
                .placeholder(R.drawable.profile)
                .into(userProfileImage)
        }

    }

    override fun populateViewHolder(viewHolder: ViewHolder?, user: Users?, position: Int) {
        var userId = getRef(position).key
        viewHolder!!.bindViews(mContext, user!!)

        viewHolder.itemView.setOnClickListener {
            Toast.makeText(mContext, "user id : $userId", Toast.LENGTH_LONG).show()
        }
    }
}