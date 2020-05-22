package com.fer.ppj.restly.faceDetection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class AllExercises: AppCompatActivity() {
    var start = 0L
    var first = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start = System.currentTimeMillis()
        Log.d("allexercies start", start.toString())

        val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = storage.edit()
        editor.putBoolean("showNotification", false)
        editor.apply()

        startActivity(Intent(this, LeftRightExercise::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        val storage = getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val elapsedTime = storage.getLong("exerciseTime", 0)
//        Log.d("exercises current", System.currentTimeMillis().toString())
//        Log.d("exercises elapsedtime", elapsedTime.toString())
        val time = elapsedTime + (System.currentTimeMillis() - start)
//        Log.d("exercises", time.toString())
        val editor: SharedPreferences.Editor = storage.edit()
        editor.putLong("exerciseTime", time)
        editor.putBoolean("showNotification", true)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        if (first > 0){
            finish()
        }
        first++
    }
}