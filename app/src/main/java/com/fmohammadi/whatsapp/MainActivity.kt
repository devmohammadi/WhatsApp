package com.fmohammadi.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var myAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("message").push()

        //  var newPerson = Person("fatemeh", "mohammadi", 21, "esfahan")
        //  myRef.setValue(newPerson)

//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("error firebase", error.message)
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                var data = snapshot.value as HashMap<String, Any>
//                Log.d("gotValue name", data.get("name").toString())
//                Log.d("gotValue family", data.get("family").toString())
//                Log.d("gotValue age", data.get("age").toString())
//                Log.d("gotValue address", data.get("address").toString())
//            }
//
//        })

        myAuth = FirebaseAuth.getInstance()

        myAuth!!.signInWithEmailAndPassword("f.mohammadi@gmail.com", "123456")
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful)
                    Log.d("isSuccessful", "isSuccessful")
                else
                    Log.d("UnSuccessful", "UnSuccessful")
            }
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = myAuth!!.currentUser
        if (firebaseUser != null)
            Log.d("Logged In", "User Is Logged In")
        else
            Log.d("Logged Out", "User Is Logged Out")
    }
}

data class Person(var name: String, var family: String, var age: Int, var address: String)