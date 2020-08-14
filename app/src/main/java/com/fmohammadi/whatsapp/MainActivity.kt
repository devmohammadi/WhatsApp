package com.fmohammadi.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->
            user = firebaseAuth!!.currentUser

            if (user != null) {
                var dashboardIntent = Intent(this, DashboaerdActivity::class.java)
                var userName = user!!.email!!.split('@')[0]
                dashboardIntent.putExtra("name", userName)
                startActivity(dashboardIntent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        btn_CreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
        bttn_Login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if(mAuth != null){
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}