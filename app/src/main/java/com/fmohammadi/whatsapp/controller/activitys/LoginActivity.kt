package com.fmohammadi.whatsapp.controller.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.fmohammadi.whatsapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        btn_login.setOnClickListener {
            var email = tvEmail_Login.text.toString().trim()
            var password = tvPassword_Login.text.toString().trim()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                LoginAccount(email, password)
            } else {
                Toast.makeText(this, "Please Enter Your Information", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun LoginAccount(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var dashboardIntent = Intent(this, DashboaerdActivity::class.java)
                    var userName = email.split('@')[0]
                    dashboardIntent.putExtra("userName", userName)
                    startActivity(dashboardIntent)
                    finish()

                } else {
                    Toast.makeText(this, "User Not Found Login Failed", Toast.LENGTH_LONG).show()
                }
            }
    }
}