package com.fmohammadi.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()
        btn_Create.setOnClickListener {
            var name = tvName_Create.text.toString().trim()
            var email = tvEmail_Create.text.toString().trim()
            var password = tvPassword_Create.text.toString().trim()
            var rePassword = tvRePassword_Create.text.toString().trim()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password) && !TextUtils.isEmpty(rePassword)
            ) {
                if (rePassword == password) {
                    CreateAccount(email, password, name)
                } else {
                    Toast.makeText(
                        this,
                        "password and confirm password does not match !!!",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(this, "please enter your information", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun CreateAccount(email: String, password: String, name: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var currentUser: FirebaseUser = mAuth!!.currentUser!!
                    var userId = currentUser!!.uid

                    mDatabase = FirebaseDatabase.getInstance().reference
                        .child("Users").child(userId)

                    var userObject = HashMap<String, String>()

                    userObject.put("name", name)
                    userObject.put("email", email)
                    userObject.put("status", "Im Programmer...")
                    userObject.put("image", "default")
                    userObject.put("thumb_image", "default")

                    mDatabase!!.setValue(userObject).addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful) {
                            var dashboardIntent = Intent(this, DashboaerdActivity::class.java)
                            dashboardIntent.putExtra("name", name)
                            startActivity(dashboardIntent)
                            finish()
                        } else {
                            Toast.makeText(this, "User Not Created", Toast.LENGTH_LONG).show()
                        }
                    }


                } else {
                    Log.d("error", task.exception!!.message!!)
                }
            }
    }
}