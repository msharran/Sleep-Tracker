package com.example.sharran.sleeptracker.utils

import com.example.sharran.sleeptracker.activity.SleepTrackerActivity

class AppContext private constructor(){
    var sleepTrackerActivity :SleepTrackerActivity? = null

    companion object {
        val initialize by lazy { AppContext() }
    }

    val database by lazy { DataBase() }
}