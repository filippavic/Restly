package com.fer.ppj.restly

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.db.DbHandler
import kotlinx.android.synthetic.main.fragment_history.view.*


class HistoryFragment : Fragment() {
    private var db = DbHandler(activity)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        db = DbHandler(activity)
        Log.d("DbHandler History", db.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        Handler().postDelayed({
            displayData(view)
        }, 100)

        return view
    }

    private fun displayData(view: View) {
        view.tv_historyData.text = ""
        val data = db.readData()
        for (i in 0 until (data.size)) {
            view.tv_historyData.append(
                "ID: " + data[i].id.toString() + " Vrijeme vježbe: " + data[i].exercise_time.toString() + "s Ukupno vrijeme: " + data[i].total_time.toString() + "s Datum i vrijeme: " + data[i].date.toString() + "\n"
            )
        }
    }
}