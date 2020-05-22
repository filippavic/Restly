package com.fer.ppj.restly

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.db.DbHandler
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.sql.Date


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
        initListeners(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DbHandler(activity)
        val count = db.getDataCount()
        if (count > 7){
            btn_more.visibility = View.VISIBLE
        }
    }

    private fun historyCard(
        dataId: Int,
        exerciseTime: Int,
        totalTime: Int,
        date: Date,
        view: View
    ) {
        val layoutInflater =
            activity?.baseContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView = layoutInflater.inflate(R.layout.histroy_card, null)
        addView.id = dataId
        val tvExercise = addView.findViewById(R.id.tv_exercise) as TextView
        tvExercise.text = (kotlin.math.ceil(exerciseTime.toDouble() / 60).toInt().toString() + " min")
        val tvTotal = addView.findViewById(R.id.tv_total) as TextView
        tvTotal.text = (kotlin.math.ceil(totalTime.toDouble() / 60).toInt().toString() + " min")
        val tvDate = addView.findViewById(R.id.tv_date) as TextView
        tvDate.text = date.toString()
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(20, 0, 20, 25)
        layoutParams.height = 100
        addView.layoutParams = layoutParams

        view.history_container.addView(addView)
    }

    private fun displayData(view: View) {
        val db = DbHandler(activity)

//        view.tv_historyData.text = ""
        val data = db.readDataTop10()
        for (i in 0 until (data.size)) {
//            view.tv_historyData.append(
//                "ID: " + data[i].id.toString() + " Vrijeme vježbe: " + data[i].exercise_time.toString() + "s Ukupno vrijeme: " + data[i].total_time.toString() + "s Datum i vrijeme: " + data[i].date.toString() + "\n"
//            )
            historyCard(data[i].id, data[i].exercise_time, data[i].total_time, data[i].date, view)
        }
    }

    private fun initListeners(view: View){
        view.btn_more.setOnClickListener {
            val db = DbHandler(activity)

            val data = db.readDataAfter10()
            for (i in 0 until (data.size)) {
                historyCard(data[i].id, data[i].exercise_time, data[i].total_time, data[i].date, view)
            }

            view.btn_more.visibility = View.GONE
        }
    }
}