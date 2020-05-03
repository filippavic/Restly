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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.fer.ppj.restly.db.DbHandler
import com.fer.ppj.restly.db.Session
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_work.*
import kotlinx.android.synthetic.main.music_sheet_layout.*
import java.sql.Date


class WorkActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    lateinit var builderAlt: Notification.Builder
    private var mp: MediaPlayer? = null
    val channelId="com.fer.ppj.restly.notifications"
    val description="Obavijesti o pauzama i vježbama"

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            /*Trenutno samo gasi activity, moram skuziti kak se otvaraju vjezbe*/
            btn_stopWorking.performClick()
            /*startActivity(Intent(this@WorkActivity, LeftRightExercise::class.java))*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        var db = DbHandler(this)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var stopTime : Long = 0
        chrono.base = SystemClock.elapsedRealtime() + stopTime
        chrono.start()

        val bottomSheetBehavior = BottomSheetBehavior.from(musicBottomSheet)

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

        btn_musicMenu.setOnClickListener{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        //ovo crasha activity, don't know why
        /*btn_sound_ok.setOnClickListener{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }*/

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
            mp = MediaPlayer.create(this, R.raw.rain)
            mp!!.isLooping = true
            mp!!.setVolume(0.5f, 0.5f)
            mp!!.start()
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
            }
        }

        btn_stopWorking.setOnClickListener{
            stopTime = SystemClock.elapsedRealtime() - chrono.base
            chrono.stop()
            val session = Session(
                (stopTime / 1000).toInt(),
                (stopTime / 1000).toInt(),
                Date(System.currentTimeMillis())
            )
            db.insertData(session)
            unregisterReceiver(broadcastReceiver)
            finish()
        }

        chrono.setOnChronometerTickListener {

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val filter = IntentFilter("android.intent.CLOSE_ACTIVITY")
            registerReceiver(broadcastReceiver, filter)
            val intentAlt = Intent("android.intent.CLOSE_ACTIVITY")
            val pendingIntentAlt = PendingIntent.getBroadcast(this, 0, intentAlt, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.MAGENTA
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContentTitle("Vrijeme je za pauzu!")
                    .setContentText("Prošlo je 30 min rada - bilo bi dobro da se malo odmoriš.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.parseColor("#764BA2"))
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)

                builderAlt = Notification.Builder(this, channelId)
                    .setContentTitle("Vrijeme je za vježbe!")
                    .setContentText("Prošlo je sat vremena - bilo bi dobro da malo provježbaš.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.parseColor("#764BA2"))
                    .setContentIntent(pendingIntentAlt)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
            }
            else {
                builder = Notification.Builder(this)
                    .setContentTitle("Vrijeme je za pauzu!")
                    .setContentText("Probaj se opustiti sljedećih 5 min")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.parseColor("#764BA2"))
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)

                builderAlt = Notification.Builder(this)
                    .setContentTitle("Vrijeme je za vježbe!")
                    .setContentText("Prošlo je sat vremena - bilo bi dobro da malo provježbaš.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.parseColor("#764BA2"))
                    .setContentIntent(pendingIntentAlt)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
            }

            var currentTime : Int = ((SystemClock.elapsedRealtime() - chrono.base)/60000).toInt()
            var seconds : List<String> = chrono.getText().toString().split(":")

            /*Vrijeme je postavljeno na 1 min/2 min zbog testiranja*/

            if((currentTime != 0) && (seconds.last().equals("00")) && (currentTime % 2 == 0)){
                notificationManager.notify(0, builderAlt.build())
            }
            else if((currentTime != 0) && (seconds.last().equals("00")) && (currentTime % 1 == 0)){
                notificationManager.notify(1, builder.build())
            }
        }

    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Pritisni ZAUSTAVI za izlazak", Toast.LENGTH_LONG)
            .show()
    }

}
