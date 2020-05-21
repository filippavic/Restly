package com.fer.ppj.restly

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.faceDetection.LeftRightExercise
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.time.LocalDate
import java.util.*


class HomeFragment : Fragment() {
    private var pauseOffset: Long = 0
    private var running: Boolean = false
    private var streak: Int = 1
    private var maxStreak: Int = 1
    private var dailyGoal = 0
    private var exerciseTimeDay = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUsername()
        setDailyGoal()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        initListeners(view)

        displayData(view)

        Handler().postDelayed({
            dailyGoalAchievement()
        }, 50)

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
        this.activity?.finish()
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setUsername() {
        val storage = this.activity?.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val username = storage?.getString("username", "korisnik")

        headerUserText.text = username + "!"
    }

    private fun setDailyGoal() {
        val storage = this.activity?.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        dailyGoal = storage!!.getInt("dailyGoal", 1)

        goalCounterText.text = dailyGoal.toString() + " min"

        view?.goalImage?.setColorFilter(
            ContextCompat.getColor(
                this.activity!!.applicationContext,
                R.color.altText
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initListeners(view: View) {
        /*view.btn_startChrono.setOnClickListener {
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
        }*/

        view.iv_settings.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
            this.activity?.finish()
        }

        view.btn_startChrono.setOnClickListener {
            activity?.startService(Intent(activity, BackgroundService::class.java))
            startActivity(Intent(activity, WorkActivity::class.java))
            this.activity?.finish()
        }
    }

    // RawQuery mi nije delal kak treba pa sam pozbrojil vremena rucno, ne znak kolko hita performanse kad ima puno podataka
    // Budem se s tim malo pozabavil kad budem imal više vremena
    // Query: SELECT SUM(exercise_time), SUM(total_time) FROM restly WHERE date = '1970-01-01' GROUP BY exercise_time, total_time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayData(view: View) {
        val db = DbHandler(activity)

        var exerciseTimeWeek = 0
        var totalTimeWeek = 0
        exerciseTimeDay = 0
        var totalTimeDay = 0
        view.todayExerciseCounterText.text = ""
        view.todayWorkCounterText.text = ""
        view.weekExerciseCounterText.text = ""
        view.weekWorkCounterText.text = ""
        val data = db.readData()
        for (i in 0 until (data.size)) {
            if (i < data.size - 1) {
                val calNow: Calendar = GregorianCalendar()
                calNow.time = data[i].date
                val calNext: Calendar = GregorianCalendar()
                calNext.time = data[i + 1].date
//                Log.d("time", calNow.get(Calendar.DAY_OF_MONTH).toString())
                if (calNow.get(Calendar.DAY_OF_MONTH) + 1 == calNext.get(Calendar.DAY_OF_MONTH)) {
                    streak++
                    if (streak > maxStreak) {
                        maxStreak = streak
                    }
                } else if (calNow.get(Calendar.DAY_OF_MONTH) + 1 != calNext.get(Calendar.DAY_OF_MONTH)) {
                    streak = 1
                }
            }
            if (data[i].date.time > System.currentTimeMillis() - 604800000 && data[i].date.time < System.currentTimeMillis()) {
                exerciseTimeWeek += data[i].exercise_time
                totalTimeWeek += data[i].total_time
            }
            if (data[i].date.toString() == LocalDate.now().toString()) {
                exerciseTimeDay += data[i].exercise_time
                totalTimeDay += data[i].total_time
            }
        }
        if (data.size == 0) {
            streak = 0
            maxStreak = 0
        }
        view.todayExerciseCounterText.text = "" + exerciseTimeDay + " s"
        view.todayWorkCounterText.text = "" + totalTimeDay + " s"
        view.weekExerciseCounterText.text = "" + exerciseTimeWeek + " s"
        view.weekWorkCounterText.text = "" + totalTimeWeek + " s"
        if (streak % 10 == 1 && streak != 11) {
            view.streakCounterText.text = "" + streak + " dan"
        } else if (streak % 10 != 1) {
            view.streakCounterText.text = "" + streak + " dana"
        }
        if (maxStreak % 10 == 1 && streak != 11) {
            view.longestCounterText.text = "" + maxStreak + " dan"
        } else if (maxStreak % 10 != 1) {
            view.longestCounterText.text = "" + maxStreak + " dana"
        }

        if (exerciseTimeDay / 60 < dailyGoal) {
            view.goalImage.setColorFilter(
                ContextCompat.getColor(
                    this.activity!!.applicationContext,
                    R.color.altText
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun dailyGoalAchievement(){
        if (exerciseTimeDay / 60 > dailyGoal) {
            view?.goalImage?.colorFilter = null
        }
    }

}