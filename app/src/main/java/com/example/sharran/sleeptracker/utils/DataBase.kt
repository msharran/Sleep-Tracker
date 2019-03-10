package com.example.sharran.sleeptracker.utils

import android.content.Context

class DataBase {
    private val context = AppContext.initialize.sleepTrackerActivity!!

    fun putData(key: String,value: String){
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(key, value)
            val commit = commit()
            println(" saved status $commit :::: slept for $value")
            commit
        }
    }

    fun getData(key: String,defaultValue: String): String {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun clearData(key :String){
        this.putData(key,"0")
    }

    companion object {
        val SLEEPING_TIME = "sleeping_hours"
    }
}