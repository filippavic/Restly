package com.fer.ppj.restly

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fer.ppj.restly.faceDetection.LeftRightExercise
import com.fer.ppj.restly.healthFragmentActivities.NutritionActivity
import com.fer.ppj.restly.healthFragmentActivities.WorkoutActivity
import com.fer.ppj.restly.healthFragmentActivities.WorkplaceActivity
import kotlinx.android.synthetic.main.fragment_health.*
import kotlinx.android.synthetic.main.fragment_health.view.*


class HealthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_health, container, false)

        initListeners(view)

        return view
    }

    private fun initListeners(view: View) {
        view.btn_exercise.setOnClickListener {
            startActivity(Intent(activity, LeftRightExercise::class.java))
        }
        view.btn_workplace.setOnClickListener {
            startActivity(Intent(activity, WorkplaceActivity::class.java))
        }
        view.btn_nutrition.setOnClickListener {
            startActivity(Intent(activity, NutritionActivity::class.java))
        }
        view.btn_workout.setOnClickListener {
            startActivity(Intent(activity, WorkoutActivity::class.java))
        }

    }
}