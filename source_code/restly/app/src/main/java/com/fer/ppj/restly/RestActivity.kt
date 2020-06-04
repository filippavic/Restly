package com.fer.ppj.restly

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rest.*


class RestActivity : AppCompatActivity() {

//    Mislim da ovo ne treba, ak sam u krivu slobodno odkomentiraj
//    Fixal sam pozivanje activityja, tam dolje si u intent slal context od MainActivity pa se je zato on palil
//    val broadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(contxt: Context?, intent: Intent?) {
//            /*Trenutno samo gasi activity, moram skuziti kak se otvaraju vjezbe*/
//            btn_stopWorking.performClick()
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest)

        val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val pauseDuration = storage.getInt("pauseDuration", 5)

        val animationDrawable =
            workContainerRest.background as AnimationDrawable
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        object : CountDownTimer(pauseDuration*1000.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                var minutesText = minutes.toString()
                if (minutes < 10){
                    minutesText = "0$minutes"
                }
                val seconds = millisUntilFinished % 60000 / 1000
                var secondsText = seconds.toString()
                if (seconds < 10){
                    secondsText = "0$seconds"
                }
                countDown.text = ("$minutesText:$secondsText")
            }

            override fun onFinish() {
                finish()
            }
        }.start()


//        val bottomSheetBehavior = BottomSheetBehavior.from(musicBottomSheet)

//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

//        btn_musicMenu.setOnClickListener{
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
//        }

//        btn_sound_ok.setOnClickListener{
//            //ovo crasha activity, don't know why
//            //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
//            Toast.makeText(applicationContext, "Povuci izbornik prema dolje", Toast.LENGTH_LONG)
//                .show()
//        }

//        btn_sea_sound.setOnClickListener {
//            if (mp != null) {
//                mp!!.stop()
//                mp!!.release()
//                mp = null
//            }
//            btn_sea_sound.setBackgroundResource(R.drawable.sea_sound_card)
//            btn_rain_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_no_sound.setBackgroundResource(R.drawable.not_active_card)
//            mp = MediaPlayer.create(this, R.raw.sea)
//            mp!!.isLooping = true
//            mp!!.setVolume(0.5f, 0.5f)
//            mp!!.start()
//        }
//
//        btn_rain_sound.setOnClickListener {
//            if (mp != null) {
//                mp!!.stop()
//                mp!!.release()
//                mp = null
//            }
//            btn_sea_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_rain_sound.setBackgroundResource(R.drawable.rain_sound_card)
//            btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
//            btn_no_sound.setBackgroundResource(R.drawable.not_active_card)
//            mp = MediaPlayer.create(this, R.raw.rain)
//            mp!!.isLooping = true
//            mp!!.setVolume(0.5f, 0.5f)
//            mp!!.start()
//        }
//
//        btn_forest_sound.setOnClickListener {
//            Toast.makeText(applicationContext, "Dolazi uskoro!", Toast.LENGTH_LONG)
//                .show()
//        }
//
//        btn_alpha_sound.setOnClickListener {
//            Toast.makeText(applicationContext, "Dolazi uskoro!", Toast.LENGTH_LONG)
//                .show()
//        }
//
//        btn_no_sound.setOnClickListener{
//            if (mp != null) {
//                mp!!.stop()
//                mp!!.release()
//                mp = null
//                btn_sea_sound.setBackgroundResource(R.drawable.not_active_card)
//                btn_rain_sound.setBackgroundResource(R.drawable.not_active_card)
//                btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
//                btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
//                btn_no_sound.setBackgroundResource(R.drawable.health_nutrition_card)
//            }
//        }

//        btn_stopWorking.setOnClickListener {
//            stopTime = SystemClock.elapsedRealtime() - chrono.base
//            chrono.stop()
//            val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
//            val exerciseTime = storage.getLong("exerciseTime", 0)
////            Log.d("exercise work", exerciseTime.toString())
//            val session = Session(
//                (exerciseTime / 1000).toInt(),
//                (stopTime / 1000).toInt(),
//                Date(System.currentTimeMillis())
//            )
//            db.insertData(session)
//
//            if (mp != null) {
//                mp!!.stop()
//                mp!!.release()
//                mp = null
//            }
//
//            val editor: SharedPreferences.Editor = storage.edit()
//            editor.putLong("exerciseTime", 0L)
//            editor.apply()
//
//            stopService(Intent(this, BackgroundService::class.java))
//
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }

    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Pričekaj do kraja pauze", Toast.LENGTH_LONG)
            .show()
    }

}
