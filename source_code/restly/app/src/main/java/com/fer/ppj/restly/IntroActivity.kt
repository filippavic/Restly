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

        sp_pause_duration.setSelection(0);
        sp_short_pause_freq.setSelection(1);
        sp_long_pause_freq.setSelection(1);

        btn_finish_intro.setOnClickListener {
            val username = et_name.text.toString()
            val pauseDuration = sp_pause_duration.selectedItem.toString().toInt()
            val shortPauseFreq = sp_short_pause_freq.selectedItem.toString().toInt()
            val longPauseFreq = sp_long_pause_freq.selectedItem.toString().toInt()
            val dailyGoal = et_daily_goal.text.toString()

            if (username.isEmpty()){
                et_name.error = "Unesi svoje ime"
                et_name.requestFocus()
//            } else if (pauseDuration.isEmpty()) {
//                et_pause_duration.error = "Unesi željeno trajanje pauze"
//                et_pause_duration.requestFocus()
//            } else if (shortPauseFreq.isEmpty()) {
//                et_short_pause_freq.error = "Unesi učestalost kratke pauze"
//                et_short_pause_freq.requestFocus()
//            } else if (longPauseFreq.isEmpty()) {
//                et_long_pause_freq.error = "Unesi učestalost duge pauze"
//                et_long_pause_freq.requestFocus()
            } else if (dailyGoal.isEmpty()) {
                et_daily_goal.error = "Unesi dnevni cilj"
                et_daily_goal.requestFocus()
            } else{
                val editor: Editor = storage.edit()
                editor.putBoolean("introDone", true)
                editor.putString("username", username)
                editor.putInt("pauseDuration", pauseDuration)
                editor.putInt("shortPauseFreq", shortPauseFreq)
                editor.putInt("longPauseFreq", longPauseFreq)
                editor.putInt("dailyGoal", dailyGoal.toInt())
                editor.apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
