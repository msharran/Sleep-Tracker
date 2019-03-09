package com.example.sharran.sleeptimecalculatorpoc.utils

import android.content.Context
import com.example.sharran.sleeptimecalculatorpoc.activity.MainActivity
import com.example.sharran.sleeptimecalculatorpoc.R

class AppPreference private constructor(){
    companion object {
        private val appPreference by lazy { AppPreference() }
        fun init() = appPreference
    }

    fun putString(context : MainActivity, value : String){
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(context.getString(R.string.SLEEPING_HOURS), value)
            val commit = commit()
            println("$value saved status $commit")
            commit
        }
    }

    fun getString(context: MainActivity, defaultValue: String): String {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(context.getString(R.string.SLEEPING_HOURS), defaultValue) ?: defaultValue
    }
}