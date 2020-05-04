package com.fer.ppj.restly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_intro.*


class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val storage = getSharedPreferences("STORAGE", Context.MODE_PRIVATE)

        btn_finish_intro.setOnClickListener {
            val username = et_name.text.toString()
            val pauseDuration = et_pause_duration.text.toString()
            val shortPauseFreq = et_short_pause_freq.text.toString()
            val longPauseFreq = et_long_pause_freq.text.toString()
            val dailyGoal = et_daily_goal.text.toString()

            if (username.isEmpty()){
                et_name.error = "Unesi svoje ime"
                et_name.requestFocus()
            } else if (pauseDuration.isEmpty()) {
                et_pause_duration.error = "Unesi željeno trajanje pauze"
                et_pause_duration.requestFocus()
            } else if (shortPauseFreq.isEmpty()) {
                et_short_pause_freq.error = "Unesi učestalost kratke pauze"
                et_short_pause_freq.requestFocus()
            } else if (longPauseFreq.isEmpty()) {
                et_long_pause_freq.error = "Unesi učestalost duge pauze"
                et_long_pause_freq.requestFocus()
            } else if (dailyGoal.isEmpty()) {
                et_daily_goal.error = "Unesi dnevni cilj"
                et_daily_goal.requestFocus()
            } else{
                val editor: Editor = storage.edit()
                editor.putBoolean("introDone", true)
                editor.putString("username", username)
                editor.putInt("pauseDuration", pauseDuration.toInt())
                editor.putInt("shortPauseFreq", shortPauseFreq.toInt())
                editor.putInt("longPauseFreq", longPauseFreq.toInt())
                editor.putInt("dailyGoal", dailyGoal.toInt())
                editor.apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
