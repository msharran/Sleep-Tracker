package com.example.sharran.sleeptracker.utils

import android.annotation.SuppressLint
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils{
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getCurrentTime(format :String) : String{
            val c = Calendar.getInstance()
            val simpleDateFormat =  SimpleDateFormat(format)
            return simpleDateFormat.format(c.time)
        }

        fun fetchTimeInHHmmSS(bigDecimal: BigDecimal): String {
            val longVal = bigDecimal.toLong()
            val hours = longVal.toInt() / 3600
            var remainder = longVal.toInt() - hours * 3600
            val mins = remainder / 60
            remainder -= mins * 60
            val secs = remainder

            return "${hours}H:${mins}M:${secs}S"
        }
    }
}