package com.example.sharran.sleeptimecalculatorpoc.utils

import android.support.v4.app.NotificationCompat
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.sharran.sleeptimecalculatorpoc.R
import android.app.*
import android.content.Context
import android.graphics.Color
import android.support.annotation.RequiresApi
import android.util.Log
import java.util.*
import android.content.IntentFilter
import com.example.sharran.sleeptimecalculatorpoc.utils.Receiver.Companion.SCREEN_OFF
import com.example.sharran.sleeptimecalculatorpoc.utils.Receiver.Companion.SCREEN_ON


class BackgroundService : Service() {

    companion object {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
    }

    var counter = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        val receiver = Receiver()
        registerReceiver(receiver, filter)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(1, Notification())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setSmallIcon(R.drawable.ic_sleeping)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        startTimer()
        val screenOn = intent.getStringExtra("screen_state")
        if (screenOn == SCREEN_ON) {
            println("Screen unlocked :::::::::::::: $screenOn")
            stoptimertask()
        } else if (screenOn == SCREEN_OFF){
            println("Screen locked :::::::::::::: $screenOn")
            startTimer()
        }
        return Service.START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
//        stoptimertask()

        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Receiver::class.java)
        this.sendBroadcast(broadcastIntent)
    }


    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    fun startTimer() {
        if (timer!=null)
            return
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("Count", "=========  " + counter++)
            }
        }
        timer!!.schedule(timerTask, 1000, 1000) //
    }

    fun stoptimertask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
            counter = 0
        }
    }
}