package com.example.sharran.sleeptimecalculatorpoc.activity

import com.example.sharran.sleeptimecalculatorpoc.utils.BackgroundService
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.sleeptimecalculatorpoc.R
import android.content.Intent
import android.os.Build


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, BackgroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        }else this.startService(intent)
    }
}
