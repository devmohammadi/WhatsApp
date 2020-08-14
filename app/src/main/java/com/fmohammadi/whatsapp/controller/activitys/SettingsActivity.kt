package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fmohammadi.whatsapp.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.title = "Settings"
    }
}