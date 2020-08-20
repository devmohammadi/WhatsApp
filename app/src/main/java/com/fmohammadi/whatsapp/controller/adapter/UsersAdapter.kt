package com.fmohammadi.whatsapp.controller.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.activitys.ChatActivity
import com.fmohammadi.whatsapp.model.Users
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.popup_show_image_profile.view.*
import kotlinx.android.synthetic.main.users_row.view.*

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


        var alertDialogBuilder: AlertDialog.Builder? = null
        var alertDialog: AlertDialog? = null

        viewHolder!!.bindViews(mContext, user!!)

        viewHolder.itemView.linearUserImage.setOnClickListener {
            openProfile(alertDialogBuilder, alertDialog, viewHolder)

        }

        viewHolder.itemView.linearUserName.setOnLongClickListener {
            var userName = viewHolder!!.user_Name
            var userAbout = viewHolder!!.user_About
            var userPic = viewHolder!!.user_LinkProfile
            LongClick(
                alertDialogBuilder,
                alertDialog,
                viewHolder,
                userName,
                userId,
                userAbout,
                userPic
            )
            return@setOnLongClickListener true

        }
        viewHolder.itemView.name_users.setOnLongClickListener {
            var userName = viewHolder!!.user_Name
            var userAbout = viewHolder!!.user_About
            var userPic = viewHolder!!.user_LinkProfile
            LongClick(
                alertDialogBuilder,
                alertDialog,
                viewHolder,
                userName,
                userId,
                userAbout,
                userPic
            )
            return@setOnLongClickListener true

        }

        viewHolder.itemView.linearUserName.setOnClickListener {
            var userName = viewHolder!!.user_Name
            var userAbout = viewHolder!!.user_About
            var userPic = viewHolder!!.user_LinkProfile
            sendMessage(userName, userId, userAbout, userPic)
        }
    }

    private fun LongClick(
        alertDialogBuilder: AlertDialog.Builder?,
        alertDialog: AlertDialog?,
        viewHolder: ViewHolder,
        userName: String?,
        userId: String?,
        userAbout: String?,
        userPic: String?
    ) {
        var alertDialogBuilder1 = alertDialogBuilder
        var options = arrayOf("Open Profile", "Send Message")
        alertDialogBuilder1 = AlertDialog.Builder(mContext)
        alertDialogBuilder1!!.setTitle("Select Options")
        alertDialogBuilder1!!.setItems(
            options,
            DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                if (i == 0) {
                    openProfile(alertDialogBuilder1, alertDialog, viewHolder)
                } else {
                    sendMessage(userName, userId, userAbout, userPic)
                }

            })
        alertDialogBuilder1!!.show()
    }

    private fun openProfile(
        alertDialogBuilder: AlertDialog.Builder?,
        alertDialog: AlertDialog?,
        viewHolder: ViewHolder
    ) {
        var alertDialogBuilder1 = alertDialogBuilder
        var alertDialog1 = alertDialog
        var viewImage =
            LayoutInflater.from(mContext).inflate(R.layout.popup_show_image_profile, null)
        alertDialogBuilder1 = AlertDialog.Builder(mContext).setView(viewImage)
        alertDialog1 = alertDialogBuilder1!!.create()
        alertDialog1!!.show()
        Picasso.with(mContext)
            .load(viewHolder.user_LinkProfile)
            .placeholder(R.drawable.profile)
            .into(viewImage.popImage)
    }

    private fun sendMessage(
        userName: String?,
        userId: String?,
        userAbout: String?,
        userPic: String?
    ) {
        var chatIntent = Intent(mContext, ChatActivity::class.java)

        chatIntent.putExtra("userName", userName)
        chatIntent.putExtra("userId", userId)
        chatIntent.putExtra("userStatus", userAbout)
        chatIntent.putExtra("userImage", userPic)

        mContext.startActivity(chatIntent)
    }
}