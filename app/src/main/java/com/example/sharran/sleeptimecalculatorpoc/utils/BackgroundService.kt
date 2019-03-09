package com.example.sharran.sleeptimecalculatorpoc.utils

import android.support.v4.app.NotificationCompat
import android.app.PendingIntent
import android.app.Service
import com.example.sharran.sleeptimecalculatorpoc.activity.MainActivity
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.sharran.sleeptimecalculatorpoc.R


class BackgroundService : Service() {


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        println("Service Started ::::::::::::::::")
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        startForeground(
            NOTIF_ID, NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_sleeping)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }

    companion object {
        private val NOTIF_ID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) 100 else 1
        private val NOTIF_CHANNEL_ID = "Channel_Id"
    }
}