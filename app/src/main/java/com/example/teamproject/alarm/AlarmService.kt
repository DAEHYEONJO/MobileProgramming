package com.example.teamproject.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build

class AlarmService (private val context: Context){
    private val alarmManager:AlarmManager ?=
            context.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager?

    fun setExactAlarm(timeInMillis:Long){

    }

    fun setRepetitiveAlarm(timeInMillis:Long){

    }

    private fun setAlarm(timeInMillis:Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                timeInMillis,pendingIntent)
            }else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        timeInMillis,pendingIntent)
            }
        }
    }

    //private fun getIntent()=
}