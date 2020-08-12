package com.fmohammadi.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("message").push()

        var newPerson = Person("fatemeh", "mohammadi", 21, "esfahan")
        myRef.setValue(newPerson)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("error firebase", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.value as HashMap<String, Any>
                Log.d("gotValue name", data.get("name").toString())
                Log.d("gotValue family", data.get("family").toString())
                Log.d("gotValue age", data.get("age").toString())
                Log.d("gotValue address", data.get("address").toString())
            }

        })
    }
}

data class Person(var name: String, var family: String, var age: Int, var address: String)