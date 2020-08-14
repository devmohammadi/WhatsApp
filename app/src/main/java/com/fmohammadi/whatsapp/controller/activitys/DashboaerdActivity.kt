package com.fmohammadi.whatsapp.controller.activitys

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.adapter.SectionPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboaerd.*

class DashboaerdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboaerd)

        var sectionAdapter: SectionPagerAdapter? = null

        supportActionBar!!.title = "Dashboard"

        sectionAdapter = SectionPagerAdapter(supportFragmentManager)
        dashboardViewPager.adapter = sectionAdapter
        mainTab.setupWithViewPager(dashboardViewPager)
        mainTab.setTabTextColors(Color.WHITE, Color.GREEN)

        if (intent!!.extras != null) {
            Toast.makeText(this, intent!!.extras!!.getString("name"), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.logOut){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        if(item.itemId == R.id.settings){
            startActivity(Intent(this,SettingsActivity::class.java))
        }
        return true
    }
}