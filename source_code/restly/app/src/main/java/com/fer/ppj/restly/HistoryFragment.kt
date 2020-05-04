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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        displayData(view)

        return view
    }

    private fun displayData(view: View) {
        val db = DbHandler(activity)

        view.tv_historyData.text = ""
        val data = db.readData()
        for (i in 0 until (data.size)) {
            view.tv_historyData.append(
                "ID: " + data[i].id.toString() + " Vrijeme vježbe: " + data[i].exercise_time.toString() + "s Ukupno vrijeme: " + data[i].total_time.toString() + "s Datum i vrijeme: " + data[i].date.toString() + "\n"
            )
        }
    }
}