package com.fer.ppj.restly

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.db.Session
import java.sql.Date
import kotlinx.android.synthetic.main.activity_work.*


class WorkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        var db = DbHandler(this)
        var stopTime : Long = 0
        chrono.base = SystemClock.elapsedRealtime() + stopTime
        chrono.start()

        btn_stopWorking.setOnClickListener{
            stopTime = SystemClock.elapsedRealtime() - chrono.base
            chrono.stop()
            val session = Session(
                (stopTime / 1000).toInt(),
                (stopTime / 1000).toInt(),
                Date(System.currentTimeMillis())
            )
            db.insertData(session)
            finish()
        }

        chrono.setOnChronometerTickListener {
            var currentTime : Int = ((SystemClock.elapsedRealtime() - chrono.base)/60000).toInt()
            var seconds : List<String> = chrono.getText().toString().split(":")

            /*Vrijeme je postavljeno na 1 min/2 min zbog testiranja*/

            if((currentTime != 0) && (seconds.last().equals("00")) && (currentTime % 2 == 0)){
                Toast.makeText(applicationContext, "Vrijeme je za vježbe!", Toast.LENGTH_LONG)
                    .show()
            }
            else if((currentTime != 0) && (seconds.last().equals("00")) && (currentTime % 1 == 0)){
                Toast.makeText(applicationContext, "Vrijeme je za kratku pauzu!", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Prvo treba zaustaviti brojač", Toast.LENGTH_LONG)
            .show()
    }

}
