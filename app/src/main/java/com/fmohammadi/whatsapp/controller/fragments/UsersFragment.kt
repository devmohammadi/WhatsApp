package com.fmohammadi.whatsapp.controller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.adapter.UsersAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_users.*


/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {

    var mUsersDatabase: DatabaseReference? = null
    var layoutManager: LinearLayoutManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        userRecyclerView.setHasFixedSize(true)

        userRecyclerView.layoutManager = layoutManager

        userRecyclerView.adapter = UsersAdapter(context!!, mUsersDatabase!!)

    }
}