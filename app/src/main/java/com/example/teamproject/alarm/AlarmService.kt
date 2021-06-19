package com.example.teamproject.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlarmService (private val context: Context){

     fun everydayAlarm(){
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,9)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("type",1)
        intent.putExtra("content","content") // 매일 보내질 알람의 내용 설정!
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,100,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)
    }

     fun setAlarm(year:Int,month:Int,day:Int){
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month-1) //1월이 0임
        calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.HOUR_OF_DAY,9)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        val requestCode= (year.toString()+month.toString()+day.toString()).toInt()
        intent.putExtra("type",0)
        intent.putExtra("code",requestCode)
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

     fun cancelAlarm(year:Int,month:Int,day:Int){
        val requestCode= (year.toString()+month.toString()+day.toString()).toInt()
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

}