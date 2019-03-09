package com.example.sharran.sleeptimecalculatorpoc.utils

import android.content.Intent
import android.support.v4.content.ContextCompat.startForegroundService
import android.os.Build
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log


class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, BackgroundService::class.java))
        } else {
            context.startService(Intent(context, BackgroundService::class.java))
        }
    }
}

