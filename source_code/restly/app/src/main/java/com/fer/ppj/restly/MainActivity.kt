package com.fer.ppj.restly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var pauseOffset : Long = 0
    private var running: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListeners()
        chrono.base = SystemClock.elapsedRealtime()


        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
//                Log.d("Time elapsed", (((SystemClock.elapsedRealtime() - chrono.getBase())/1000).toInt()).toString())
                if(((SystemClock.elapsedRealtime() - chrono.getBase())/1000).toInt() == 10 && running){
                    startNextActivity()
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    fun startNextActivity(){
        startActivity(Intent(this, LeftRightExercise::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    fun initListeners() {
        startChrono.setOnClickListener {
            running = true
            chrono.base = (SystemClock.elapsedRealtime() - pauseOffset);
            chrono.start()
            startChrono.visibility = View.GONE
            stopChrono.visibility = View.VISIBLE
            resetChrono.visibility = View.GONE
        }
        stopChrono.setOnClickListener {
            chrono.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chrono.getBase()
            Toast.makeText(this, "Time elapsed: " + pauseOffset/1000 + "s", Toast.LENGTH_LONG).show()
            stopChrono.visibility = View.GONE
            resetChrono.visibility = View.VISIBLE
            startChrono.visibility = View.VISIBLE
        }
        resetChrono.setOnClickListener {
            chrono.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
        startExercise.setOnClickListener {
            startActivity(Intent(this, LeftRightExercise::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}