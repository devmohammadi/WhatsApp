package com.fmohammadi.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DashboaerdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboaerd)

        if(intent!!.extras != null){
            Toast.makeText(this,intent!!.extras!!.getString("name"),Toast.LENGTH_LONG).show()
        }
    }
}