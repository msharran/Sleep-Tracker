package com.example.sharran.sleeptracker.utils

import android.content.Context

class DataBase {
    private val context = AppContext.initialize.sleepTrackerActivity!!

    fun putString(key: String, value: String){
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(key, value)
            val commit = commit()
            println(" saved status $commit :::: slept for $value")
            commit
        }
    }

    fun getString(key: String, defaultValue: String): String {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun clearString(key :String) = this.putString(key,"0")

    fun putBoolean(key: String, value: Boolean){
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(key, value)
            val commit = commit()
            println(" saved status $commit :::: is data saved $value")
            commit
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, defaultValue)
    }

    fun clearBoolean(key: String) = this.putBoolean(key,false)

    companion object {
        val SLEEPING_TIME = "sleeping_hours"
    }
}