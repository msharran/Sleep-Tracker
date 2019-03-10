package com.example.sharran.sleeptracker.utils

import android.support.v4.app.NotificationCompat
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.sharran.sleeptracker.R
import android.app.*
import android.content.Context
import android.graphics.Color
import android.support.annotation.RequiresApi
import android.util.Log
import java.util.*
import android.content.IntentFilter
import com.example.sharran.sleeptracker.utils.DataBase.Companion.SLEEPING_TIME
import com.example.sharran.sleeptracker.utils.TimeUtils.Companion.getCurrentTime
import com.example.sharran.sleeptracker.utils.Receiver.Companion.SCREEN_OFF
import com.example.sharran.sleeptracker.utils.Receiver.Companion.SCREEN_ON
import com.example.sharran.sleeptracker.utils.TimeUtils.Companion.fetchTimeInHHmmSS


class BackgroundService : Service() {

    companion object {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        var isTimeSavedLocally  = false
    }

    private var sleepingTime:String = "0"
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var counter = 1
    private val appContext = AppContext.initialize
    private val database = appContext.database

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
            startForegroundAboveOreo()
        else
            startForeground(1, buildNotification("App is running in background"))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundAboveOreo() {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val notificationManager = getNotificationManager()
        notificationManager.createNotificationChannel(notificationChannel)

        val notification = buildNotification("App is running in background")
        startForeground(1, notification)
    }

    private fun getNotificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun buildNotification(title :String): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_sleeping)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
        }
        return notificationBuilder.build()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val screenOn = intent.getStringExtra("screen_state")
        when (screenOn) {
            SCREEN_ON -> {
                println("Screen unlocked :::::::::::::: $screenOn")
                stopTimer()
                saveAndShowSleepingTime()
            }
            SCREEN_OFF -> {
                println("Screen locked :::::::::::::: $screenOn")
                startTimer()
            }
        }
        return Service.START_STICKY
    }

    private fun saveAndShowSleepingTime() {
        val currentTime = getCurrentTime(format = "HH").toInt()
        when {
            currentTime in 9..11 && !isTimeSavedLocally -> {
                saveSleepingTimeLocallyAndNotify()
                isTimeSavedLocally = true
            }
            currentTime in 9..11 && isTimeSavedLocally -> {
                isTimeSavedLocally = false
                saveSleepingHoursToDB()
            }
            else -> {
                isTimeSavedLocally = false
                saveSleepingHoursToDB()
            }
        }
    }


    private fun saveSleepingHoursToDB() {
        if (counter > database.getData(SLEEPING_TIME, "0").toInt()) {
            database.putData(key = SLEEPING_TIME, value = (counter).toString())
        }
    }

    private fun saveSleepingTimeLocallyAndNotify() {
        saveSleepingHoursToDB()
        sleepingTime = fetchTimeInHHmmSS(database.getData(key = SLEEPING_TIME, defaultValue = "0").toBigDecimal())
        notify("Sleep duration $sleepingTime")
        database.clearData(SLEEPING_TIME)
        println("Sleep duration $sleepingTime")
    }

    private fun notify(text :String) {
        val notificationManager = getNotificationManager()
        val notification = buildNotification(text)
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()

        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Receiver::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    private fun startTimer() {
        if (timer!=null)
            return
        counter = 1
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("Count", "=========  " + counter++)
            }
        }
        timer!!.schedule(timerTask, 1000, 1000) //
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }
}
