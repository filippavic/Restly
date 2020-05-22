package com.fer.ppj.restly

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    private val welcomeSliderAdapter = WelcomeSliderAdapter(
        listOf(
            WelcomeSlide(
                "Bok! Dobrodošao/la u Restly, aplikaciju za zasluženi odmor.",
                R.drawable.welcome_start
            ),
            WelcomeSlide(
                "Restly je jednostavan za korištenje - kada kreneš raditi, upali Restly i pokreni timer.",
                R.drawable.welcome_work
            ),
            WelcomeSlide(
                "Svako malo, Restly će te obavijestiti da je vrijeme za pauzu - kraću ili dužu.",
                R.drawable.welcome_exc
            ),
            WelcomeSlide(
                "Vježbe su interaktivne - Restly koristi prednju kameru kako bi odredio radiš li vježbe ispravno.",
                R.drawable.welcome_face
            )
        )

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeSlider.adapter = welcomeSliderAdapter

        btn_continue.setOnClickListener {
            if (welcomeSlider.currentItem + 1 < welcomeSliderAdapter.itemCount) {
                welcomeSlider.currentItem += 1
            } else {
                startActivity(Intent(applicationContext, IntroActivity::class.java))
                finish()
            }
        }

        btn_skip.setOnClickListener {
            startActivity(Intent(applicationContext, IntroActivity::class.java))
            finish()
        }
    }
}