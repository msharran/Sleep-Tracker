package com.example.sharran.sleeptimecalculatorpoc.utils

import android.content.Intent
import android.os.Build
import android.content.BroadcastReceiver
import android.content.Context


class Receiver : BroadcastReceiver() {
    companion object {
        const val SCREEN_ON = "screen on"
        const val SCREEN_OFF = "screen off"

        var wasScreenOn : String = ""
    }
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = getIntentBasedOnScreenOperations(intent, context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }

    }

    private fun getIntentBasedOnScreenOperations(intent: Intent, context: Context): Intent {
        val serviceIntent = Intent(context, BackgroundService::class.java)
        when {
            intent.action == Intent.ACTION_SCREEN_ON -> {
                wasScreenOn = SCREEN_ON
                serviceIntent.putExtra("screen_state", wasScreenOn)
            }
            intent.action == Intent.ACTION_SCREEN_OFF -> {
                wasScreenOn = SCREEN_OFF
                serviceIntent.putExtra("screen_state", wasScreenOn)
            }
        }


        return serviceIntent
    }
}

