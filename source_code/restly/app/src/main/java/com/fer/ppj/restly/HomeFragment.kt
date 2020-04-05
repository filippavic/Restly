package com.fer.ppj.restly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.db.Session
import com.fer.ppj.restly.faceDetection.LeftRightExercise
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.sql.Timestamp


class HomeFragment : Fragment() {
    private var pauseOffset: Long = 0
    private var running: Boolean = false
    private var db = DbHandler(activity)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        db = DbHandler(activity)
        Log.d("DbHandler Home", db.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        initListeners(view)

        val handler = Handler()

        handler.postDelayed(object : Runnable {
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

    fun initListeners(view: View) {
        view.startChrono.setOnClickListener {
            running = true
            view.chrono.base = (SystemClock.elapsedRealtime() - pauseOffset);
            view.chrono.start()
            view.startChrono.visibility = View.GONE
            view.pauseChrono.visibility = View.VISIBLE
            view.endChrono.visibility = View.VISIBLE
        }
        view.pauseChrono.setOnClickListener {
            running = false
            view.chrono.stop()
            pauseOffset = SystemClock.elapsedRealtime() - view.chrono.getBase()
            Toast.makeText(context, "Time elapsed: " + pauseOffset / 1000 + "s", Toast.LENGTH_LONG)
                .show()
            view.pauseChrono.visibility = View.GONE
            view.endChrono.visibility = View.VISIBLE
            view.startChrono.visibility = View.VISIBLE
        }
        view.endChrono.setOnClickListener {
            running = false
            view.chrono.stop()
            pauseOffset = SystemClock.elapsedRealtime() - view.chrono.getBase()
            val session = Session(
                (pauseOffset / 1000).toInt(),
                (pauseOffset / 1000).toInt(),
                Timestamp(System.currentTimeMillis())
            )
            db.insertData(session)
            pauseOffset = 0;
            view.chrono.setBase(SystemClock.elapsedRealtime());
            view.pauseChrono.visibility = View.GONE
            view.endChrono.visibility = View.GONE
            view.startChrono.visibility = View.VISIBLE
        }
//        view.startExercise.setOnClickListener {
//            startActivity(Intent(activity, LeftRightExercise::class.java))
////            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//        }
    }
}