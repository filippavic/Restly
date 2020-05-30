package com.fer.ppj.restly

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {
    private val SPLASH_DISPLAY_LENGTH = 500

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val storage = getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val introDone = storage.getBoolean("introDone", false)

        Handler().postDelayed({
            if (introDone) {
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                finish()
            }else{
                val mainIntent = Intent(applicationContext, WelcomeActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                finish()
            }
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
