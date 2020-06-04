package com.fer.ppj.restly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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

        var positionDuration = 0
        var positionShort = 0
        var positionLong = 0

        when (pauseDuration){
            5 -> positionDuration = 0
            10 -> positionDuration = 1
            15 -> positionDuration = 2
            else -> positionDuration = 0
        }

        when (shortPauseFreq){
            30 -> positionShort = 0
            45 -> positionShort = 1
            60 -> positionShort = 2
            else -> positionShort = 0
        }

        when (longPauseFreq){
            45 -> positionLong = 0
            60 -> positionLong = 1
            75 -> positionLong = 2
            else -> positionLong = 0
        }


        sp_pause_duration.setSelection(positionDuration);
        sp_short_pause_freq.setSelection(positionShort)
        sp_long_pause_freq.setSelection(positionLong)
        et_daily_goal.setText(dailyGoal.toString())
    }

    private fun putValues(storage: SharedPreferences){
        val pauseDuration = sp_pause_duration.selectedItem.toString().toInt()
        val shortPauseFreq = sp_short_pause_freq.selectedItem.toString().toInt()
        val longPauseFreq = sp_long_pause_freq.selectedItem.toString().toInt()
        val dailyGoal = et_daily_goal.text.toString().toInt()

        val editor: SharedPreferences.Editor = storage.edit()
        editor.putInt("pauseDuration", pauseDuration)
        editor.putInt("shortPauseFreq", shortPauseFreq)
        editor.putInt("longPauseFreq", longPauseFreq)
        editor.putInt("dailyGoal", dailyGoal)
        editor.apply()
    }


}
