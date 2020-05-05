package com.fer.ppj.restly

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.db.Session
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.fer.ppj.restly.faceDetection.LeftRightExercise
import kotlinx.android.synthetic.main.activity_work.*
import kotlinx.android.synthetic.main.music_sheet_layout.*
import java.sql.Date


class WorkActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    lateinit var builderAlt: Notification.Builder
    val channelId = "com.fer.ppj.restly.notifications"
    val description = "Obavijesti o pauzama i vježbama"
    private var action = ""

    private var mp: MediaPlayer? = null

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
        setContentView(R.layout.activity_work)
        var db = DbHandler(this)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var stopTime: Long = 0
        chrono.base = SystemClock.elapsedRealtime() + stopTime
        chrono.start()

        val bottomSheetBehavior = BottomSheetBehavior.from(musicBottomSheet)

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

        btn_musicMenu.setOnClickListener{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        btn_sound_ok.setOnClickListener{
            //ovo crasha activity, don't know why
            //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            Toast.makeText(applicationContext, "Povuci izbornik prema dolje", Toast.LENGTH_LONG)
                .show()
        }

        btn_sea_sound.setOnClickListener {
            if (mp != null) {
                mp!!.stop()
                mp!!.release()
                mp = null
            }
            btn_sea_sound.setBackgroundResource(R.drawable.sea_sound_card)
            btn_rain_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_no_sound.setBackgroundResource(R.drawable.not_active_card)
            mp = MediaPlayer.create(this, R.raw.sea)
            mp!!.isLooping = true
            mp!!.setVolume(0.5f, 0.5f)
            mp!!.start()
        }

        btn_rain_sound.setOnClickListener {
            if (mp != null) {
                mp!!.stop()
                mp!!.release()
                mp = null
            }
            btn_sea_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_rain_sound.setBackgroundResource(R.drawable.rain_sound_card)
            btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
            btn_no_sound.setBackgroundResource(R.drawable.not_active_card)
            mp = MediaPlayer.create(this, R.raw.rain)
            mp!!.isLooping = true
            mp!!.setVolume(0.5f, 0.5f)
            mp!!.start()
        }

        btn_forest_sound.setOnClickListener {
            Toast.makeText(applicationContext, "Dolazi uskoro!", Toast.LENGTH_LONG)
                .show()
        }

        btn_alpha_sound.setOnClickListener {
            Toast.makeText(applicationContext, "Dolazi uskoro!", Toast.LENGTH_LONG)
                .show()
        }

        btn_no_sound.setOnClickListener{
            if (mp != null) {
                mp!!.stop()
                mp!!.release()
                mp = null
                btn_sea_sound.setBackgroundResource(R.drawable.not_active_card)
                btn_rain_sound.setBackgroundResource(R.drawable.not_active_card)
                btn_forest_sound.setBackgroundResource(R.drawable.not_active_card)
                btn_alpha_sound.setBackgroundResource(R.drawable.not_active_card)
                btn_no_sound.setBackgroundResource(R.drawable.health_nutrition_card)
            }
        }

        btn_stopWorking.setOnClickListener {
            stopTime = SystemClock.elapsedRealtime() - chrono.base
            chrono.stop()
            val session = Session(
                (stopTime / 1000).toInt(),
                (stopTime / 1000).toInt(),
                Date(System.currentTimeMillis())
            )
            db.insertData(session)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        val shortPauseFreq = storage.getInt("shortPauseFreq", 30)
        val longPauseFreq = storage.getInt("longPauseFreq", 60)

//      Ovaj dio koda sam maknul iz setOnChronometerTickListener jer se ne treba izvrsavati na svaki tick, slobodno vrni ak ti treba
        val intent = Intent(applicationContext, LeftRightExercise::class.java)
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val filter = IntentFilter("android.intent.CLOSE_ACTIVITY")
//        Ovo ne treba jer sam maknul broadcast reciever, odkomentiraj ak treba
//        registerReceiver(broadcastReceiver, filter)
        val intentAlt = Intent("android.intent.CLOSE_ACTIVITY")
        val pendingIntentAlt = PendingIntent.getBroadcast(this, 0, intentAlt, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Vrijeme je za pauzu!")
                .setContentText("Prošlo je " + shortPauseFreq + " minuta rada - bilo bi dobro da se malo odmoriš.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#764BA2"))
                .setContentIntent(pendingIntentAlt)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)

            builderAlt = Notification.Builder(this, channelId)
                .setContentTitle("Vrijeme je za vježbe!")
                .setContentText("Prošlo je " + longPauseFreq + " minuta - bilo bi dobro da malo provježbaš.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#764BA2"))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        } else {
            builder = Notification.Builder(this)
                .setContentTitle("Vrijeme je za pauzu!")
                .setContentText("Prošlo je " + shortPauseFreq + " minuta rada - bilo bi dobro da se malo odmoriš.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#764BA2"))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)

            builderAlt = Notification.Builder(this)
                .setContentTitle("Vrijeme je za vježbe!")
                .setContentText("Prošlo je " + longPauseFreq + " minuta - bilo bi dobro da malo provježbaš.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#764BA2"))
                .setContentIntent(pendingIntentAlt)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        }
//        do tud

        var prevRestType = "long"
        var totalTime = shortPauseFreq
        var seconds = 0.toLong()
        var minutes = 0.toLong()

        chrono.setOnChronometerTickListener {
//            Za testiranje radimo s sekundama, u stvarnosti trebamo raditi s minutama
//            minutes = (SystemClock.elapsedRealtime() - chrono.base)/(1000*60)
            seconds = (SystemClock.elapsedRealtime() - chrono.base) / 1000

            if (seconds.toInt() == totalTime) {
                if (prevRestType == "long") {
//                    Ak je prosla pauza bila duga, sljedeca je isto duga
//                    Pokreni short rest, dodaj vrijeme od long rest
                    action = "rest"
                    notificationManager.notify(0, builder.build())
                    totalTime += longPauseFreq
                    prevRestType = "short"
                } else {
                    action = "exercise"
                    notificationManager.notify(0, builderAlt.build())
                    totalTime += shortPauseFreq
                    prevRestType = "long"
                }
            }

//            var currentTime: Int = ((SystemClock.elapsedRealtime() - chrono.base) / 60000).toInt()
//            var seconds: List<String> = chrono.getText().toString().split(":")
//
//
//            /*Vrijeme je postavljeno na 1 min/2 min zbog testiranja*/
//            if ((currentTime == 0) && (seconds.last().equals("00")) && (currentTime % 2 == 0)) {
//                notificationManager.notify(0, builderAlt.build())
//            } else if ((currentTime == 0) && (seconds.last().equals("00")) && (currentTime % 1 == 0)) {
//                notificationManager.notify(1, builder.build())
//            }
        }

    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Pritisni ZAUSTAVI za izlazak", Toast.LENGTH_LONG)
            .show()
    }

}
