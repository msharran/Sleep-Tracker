package com.example.sharran.sleeptracker.activity

import com.example.sharran.sleeptracker.utils.BackgroundService
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.sleeptracker.R
import android.content.Intent
import android.app.ActivityManager
import android.content.Context
import android.util.Log


class SleepTrackerActivity : AppCompatActivity() {

    private lateinit var backgroundServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_tracker)

        backgroundServiceIntent = Intent(this, BackgroundService::class.java)
        if (!isMyServiceRunning(BackgroundService::class.java)) {
            startService(backgroundServiceIntent)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }
}
