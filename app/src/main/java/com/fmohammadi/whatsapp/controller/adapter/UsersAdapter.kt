package com.fmohammadi.whatsapp.controller.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class UsersAdapter(var mContext: Context, var mDatabaseQuery: DatabaseReference) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}