package com.example.teamproject.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlarmService (private val context: Context){

    private fun everydayAlarm(content:String){
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,18)
        calendar.set(Calendar.MINUTE,30)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("type",0)
        intent.putExtra("content",content) // 매일 보내질 알람의 내용 설정!
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,100,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)
    }

    private fun setAlarm(year:Int,month:Int,day:Int){
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month-1) //1월이 0임
        calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.HOUR_OF_DAY,20)
        calendar.set(Calendar.MINUTE,30)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        val requestCode= (year.toString()+month.toString()+day.toString()).toInt()
        intent.putExtra("type",1)
        intent.putExtra("code",requestCode)
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

    private fun cancelAlarm(year:Int,month:Int,day:Int){
        val requestCode= (year.toString()+month.toString()+day.toString()).toInt()
        val alarmManager=context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent= Intent(context.applicationContext,AlarmReceiver::class.java)
        val pendingIntent= PendingIntent.getBroadcast(context.applicationContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

//    private val alarmManager: AlarmManager?=
//            context.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager?
//
//    fun setExactAlarm(timeInMillis:Long){
//        setAlarm(timeInMillis,getPendingIntent(getIntent().apply {
//            action=Constants.ACTION_SET_EXACT_ALARM
//            putExtra(Constants.EXTRA_EXACT_ALARM_TIME,timeInMillis)
//        }))
//    }
//
//    fun setRepetitiveAlarm(timeInMillis:Long){
//        setAlarm(timeInMillis,getPendingIntent(getIntent().apply {
//            action=Constants.ACTION_SET_REPETITIVE_ALARM
//            putExtra(Constants.EXTRA_EXACT_ALARM_TIME,timeInMillis)
//        }))
//    }
//
//    private fun setAlarm(timeInMillis:Long, pendingIntent: PendingIntent){
//        alarmManager?.let {
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                timeInMillis,pendingIntent)
//            }else{
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                        timeInMillis,pendingIntent)
//            }
//        }
//    }
//
//    private fun getIntent() : Intent =Intent(context,AlarmReceiver::class.java)
//
//    private fun getPendingIntent(intent: Intent):PendingIntent=
//            PendingIntent.getBroadcast(
//                    context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT
//            )
//    fun setEveryDayAlarm(){
//        val calendar=Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY,18)
//        calendar.set(Calendar.MINUTE,30)
//        val notificationChannel=NotificationChannel("everyday","everyday",NotificationManager.IMPORTANCE_DEFAULT)
//        notificationChannel.enableVibration(true)
//        notificationChannel.enableLights(true)
//        notificationChannel.lightColor= Color.BLUE
//        notificationChannel.lockscreenVisibility= Notification.VISIBILITY_PRIVATE
//
//        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(notificationChannel)
//
//        val intent= Intent(context,LoginActivity::class.java)
//        val pendingIntent=PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//        val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
//    }
}