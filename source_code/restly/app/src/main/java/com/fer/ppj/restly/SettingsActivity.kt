package com.fer.ppj.restly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setValues(storage)
        initListeners(storage)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        putValues(this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE))
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun initListeners(storage: SharedPreferences){
        iv_back.setOnClickListener {
            putValues(storage)

            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setValues(storage: SharedPreferences){
        val pauseDuration = storage.getInt("pauseDuration", 5)
        val shortPauseFreq = storage.getInt("shortPauseFreq", 30)
        val longPauseFreq = storage.getInt("longPauseFreq", 60)
        val dailyGoal = storage.getInt("dailyGoal", 45)

        et_pause_duration.setText(pauseDuration.toString())
        et_short_pause_freq.setText(shortPauseFreq.toString())
        et_long_pause_freq.setText(longPauseFreq.toString())
        et_daily_goal.setText(dailyGoal.toString())
    }

    private fun putValues(storage: SharedPreferences){
        val pauseDuration = et_pause_duration.text.toString().toInt()
        val shortPauseFreq = et_short_pause_freq.text.toString().toInt()
        val longPauseFreq = et_long_pause_freq.text.toString().toInt()
        val dailyGoal = et_daily_goal.text.toString().toInt()

        val editor: SharedPreferences.Editor = storage.edit()
        editor.putInt("pauseDuration", pauseDuration)
        editor.putInt("shortPauseFreq", shortPauseFreq)
        editor.putInt("longPauseFreq", longPauseFreq)
        editor.putInt("dailyGoal", dailyGoal)
        editor.apply()
    }


}
