package com.fer.ppj.restly

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.db.Session
import com.fer.ppj.restly.faceDetection.LeftRightExercise
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.sql.Date
import java.time.LocalDate


class HomeFragment : Fragment() {
    private var pauseOffset: Long = 0
    private var running: Boolean = false
    private var db = DbHandler(activity)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        db = DbHandler(activity)
        Log.d("DbHandler Home", db.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        initListeners(view)

        Handler().postDelayed({
            displayData(view)
        }, 100)

        val handler = Handler()

        handler.postDelayed(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
//                Log.d("Time elapsed", (((SystemClock.elapsedRealtime() - chrono.getBase())/1000).toInt()).toString())
                if (((SystemClock.elapsedRealtime() - view.chrono.getBase()) / 1000).toInt() == 10 && running) {
                    startNextActivity()
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)

        return view
    }

    fun startNextActivity() {
        startActivity(Intent(activity, LeftRightExercise::class.java))
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initListeners(view: View) {
        view.btn_startChrono.setOnClickListener {
            running = true
            view.chrono.base = (SystemClock.elapsedRealtime() - pauseOffset);
            view.chrono.start()
            view.btn_startChrono.visibility = View.GONE
            view.btn_pauseChrono.visibility = View.VISIBLE
            view.btn_endChrono.visibility = View.VISIBLE
        }
        view.btn_pauseChrono.setOnClickListener {
            running = false
            view.chrono.stop()
            pauseOffset = SystemClock.elapsedRealtime() - view.chrono.getBase()
            Toast.makeText(context, "Time elapsed: " + pauseOffset / 1000 + "s", Toast.LENGTH_LONG)
                .show()
            view.btn_pauseChrono.visibility = View.GONE
            view.btn_endChrono.visibility = View.VISIBLE
            view.btn_startChrono.visibility = View.VISIBLE
        }
        view.btn_endChrono.setOnClickListener {
            running = false
            view.chrono.stop()
            pauseOffset = SystemClock.elapsedRealtime() - view.chrono.getBase()
            val session = Session(
                (pauseOffset / 1000).toInt(),
                (pauseOffset / 1000).toInt(),
                Date(System.currentTimeMillis())
            )
            db.insertData(session)
            pauseOffset = 0;
            view.chrono.setBase(SystemClock.elapsedRealtime());
            view.btn_pauseChrono.visibility = View.GONE
            view.btn_endChrono.visibility = View.GONE
            view.btn_startChrono.visibility = View.VISIBLE
            displayData(view)
        }

    }

    // RawQuery mi nije delal kak treba pa sam pozbrojil vremena rucno, ne znak kolko hita performanse kad ima puno podataka
    // Budem se s tim malo pozabavil kad budem imal više vremena
    // Query: SELECT SUM(exercise_time), SUM(total_time) FROM restly WHERE date = '1970-01-01' GROUP BY exercise_time, total_time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayData(view: View) {
        var exerciseTimeWeek = 0
        var totalTimeWeek = 0
        var exerciseTimeDay = 0
        var totalTimeDay = 0
        view.tv_today.text = ""
        val data = db.readData()
        for (i in 0 until (data.size)) {
            if (data[i].date.time > System.currentTimeMillis() - 604800000 && data[i].date.time < System.currentTimeMillis()){
                exerciseTimeWeek += data[i].exercise_time
                totalTimeWeek += data[i].total_time
            }
            if (data[i].date.toString() == LocalDate.now().toString()) {
                exerciseTimeDay += data[i].exercise_time
                totalTimeDay += data[i].total_time
            }
        }
        view.tv_today.text = "Vrijeme vježbanja danas: " + exerciseTimeDay + "s, Ukupno vrijeme: " + totalTimeDay +"s"
        view.tv_week.text = "Vrijeme vježbanja u posljednjih 7 dana: " + exerciseTimeWeek + "s, Ukupno vrijeme: " + totalTimeWeek +"s"
    }

}