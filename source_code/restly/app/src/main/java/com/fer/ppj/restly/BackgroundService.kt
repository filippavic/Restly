package com.fer.ppj.restly

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fer.ppj.restly.faceDetection.AllExercises

class BackgroundService : Service() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.fer.greenhouse"
    private val description = ""
    var shortPauseFreq = 0
    var longPauseFreq = 0
    var pauseDuration = 0
    val handler = Handler()
    private var action = ""
    private var start = 0L
    private val context = this

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val storage = this.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
        shortPauseFreq = storage.getInt("shortPauseFreq", 30)
        longPauseFreq = storage.getInt("longPauseFreq", 60)
        pauseDuration = storage.getInt("pauseDuration", 5)

        start = System.currentTimeMillis()
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {

        var prevRestType = "long"
        var totalTime = shortPauseFreq
        var pauseTime = 0
        var seconds = 0.toLong()
        var minutes = 0.toLong()

        startForeground()
        handler.postDelayed(object : Runnable {
            override fun run() {

//                minutes = (System.currentTimeMillis() - start)/(1000*60)
                seconds = (System.currentTimeMillis() - start) / 1000

//                Log.d("seconds", seconds.toString())
                if (seconds.toInt() == totalTime) {

                    val storage = context.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
                    val showNotification = storage.getBoolean("showNotification", true)
                    Log.d("showNotification", showNotification.toString())

                    if (prevRestType == "long") {
//                    Ak je prosla pauza bila duga, sljedeca je isto duga
//                    Pokreni short rest, dodaj vrijeme od long rest
                        action = "rest"
                        if (showNotification) {
                            notificationRest()
                        }
                        pauseTime = totalTime + pauseDuration
                        totalTime += longPauseFreq
                        prevRestType = "short"
                    } else {
                        action = "exercise"
                        if (showNotification) {
                            notificationExercise()
                        }
                        totalTime += shortPauseFreq
                        prevRestType = "long"
                    }
                }

                if (seconds.toInt() == pauseTime && prevRestType == "short"){
                    val storage = context.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
                    val showNotification = storage.getBoolean("showNotification", true)
                    if(showNotification){
                        notificationPauseEnd()
                    }
                }

                handler.postDelayed(this, 1000L)
            }
        }, 0)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun notificationRest() {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, WorkActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId, description, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.name = "alertsChannel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Vrijeme je za pauzu!")
                .setContentText("Prošlo je $shortPauseFreq minuta rada - bilo bi dobro da se malo odmoriš.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        } else {

            builder = Notification.Builder(this)
                .setContentTitle("Vrijeme je za pauzu!")
                .setContentText("Prošlo je $shortPauseFreq minuta rada - bilo bi dobro da se malo odmoriš.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        }
        notificationManager.notify(1234, builder.build())

    }

    private fun notificationExercise() {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, AllExercises::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, intent, 0
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId, description, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.name = "alertsChannel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Vrijeme je za vježbe!")
                .setContentText("Prošlo je $longPauseFreq minuta - bilo bi dobro da malo provježbaš.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        } else {

            builder = Notification.Builder(this)
                .setContentTitle("Vrijeme je za vježbe!")
                .setContentText("Prošlo je $longPauseFreq minuta - bilo bi dobro da malo provježbaš.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        }
        notificationManager.notify(4321, builder.build())

    }

    private fun notificationPauseEnd() {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, WorkActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId, description, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.name = "alertsChannel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Pauza je gotova!")
                .setContentText("Prošlo je $pauseDuration minuta pauze - možete nastaviti s radom.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        } else {

            builder = Notification.Builder(this)
                .setContentTitle("Pauza je gotova!")
                .setContentText("Prošlo je $pauseDuration minuta pauze - možete nastaviti s radom.")
                .setColor(Color.parseColor("#5C5FBC"))
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
        }
        notificationManager.notify(1234, builder.build())

    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("bckServ", "backgroundService")
            } else {
                ""
            }
        val notificationIntent = Intent(this, WorkActivity::class.java)
        notificationIntent.flags =
            (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        notificationIntent.action = (Intent.ACTION_MAIN)
//        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                channelId
            )
                .setOngoing(true)
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentTitle("Servis radi u pozadini")
                .setContentText("Kliknite da bi ste se vratili u aplikaciju")
                .setContentIntent(pendingIntent)
                .build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    companion object {
        private const val NOTIF_ID = 1
    }
}