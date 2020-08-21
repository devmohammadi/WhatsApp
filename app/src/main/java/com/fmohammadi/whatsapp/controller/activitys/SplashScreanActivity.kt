package com.fmohammadi.whatsapp.controller.activitys

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.fmohammadi.whatsapp.R
import kotlinx.android.synthetic.main.activity_splash_screan.*

class SplashScreanActivity : AppCompatActivity() {
    private var fadeObjectAnimatorImage: ObjectAnimator? = null
    private var fadeObjectAnimatorText: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screan)

        fadeObjectAnimatorImage = ObjectAnimator.ofFloat(imageSplash, "alpha", 0f, 1f)
        fadeObjectAnimatorImage?.apply {
            duration = 2000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 0
            start()
        }
        fadeObjectAnimatorText = ObjectAnimator.ofFloat(textSplash, "alpha", 0f, 1f)
        fadeObjectAnimatorText?.apply {
            duration = 2000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 0
            start()
        }

        Handler().postDelayed({
            kotlin.run {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 5000)
    }


}