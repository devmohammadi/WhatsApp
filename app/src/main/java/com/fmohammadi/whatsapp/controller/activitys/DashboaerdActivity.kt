package com.fmohammadi.whatsapp.controller.activitys

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.adapter.SectionPagerAdapter
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
}