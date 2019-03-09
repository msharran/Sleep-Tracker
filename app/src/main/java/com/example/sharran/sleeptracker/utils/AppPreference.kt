package com.example.sharran.sleeptracker.utils

import android.content.Context
import com.example.sharran.sleeptracker.activity.SleepTrackerActivity
import com.example.sharran.sleeptracker.R

class AppPreference private constructor(){
    companion object {
        private val appPreference by lazy { AppPreference() }
        fun builder() = appPreference
    }

    fun putString(context : SleepTrackerActivity, value : String){
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(context.getString(R.string.SLEEPING_HOURS), value)
            val commit = commit()
            println("$value saved status $commit")
            commit
        }
    }

    fun getString(context: SleepTrackerActivity, defaultValue: String): String {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(context.getString(R.string.SLEEPING_HOURS), defaultValue) ?: defaultValue
    }
}