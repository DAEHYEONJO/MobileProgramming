package com.example.teamproject.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.teamproject.R
import com.example.teamproject.login.LoginActivity

class AlarmReceiver:BroadcastReceiver() {
    lateinit var pendingIntent: PendingIntent
    //lateinit var builder:NotificationCompat.Builder
    override fun onReceive(context: Context?, intent: Intent?) {
        var channelID =""
        var code= 0
        var name=""
        val notificationManager=context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val i = Intent(context, LoginActivity::class.java) //알람 누르면 login 화면으로 이동
        i.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        val type=intent?.getIntExtra("type",-1)
        if(type==0){//운동 스케쥴이 있는 날 받는 알람
            val requestCode=intent?.getIntExtra("code",-1)
            pendingIntent=PendingIntent.getActivity(context,requestCode,i,PendingIntent.FLAG_UPDATE_CURRENT)
            val builder=NotificationCompat.Builder(context,"specificday")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_baseline_timer_24)
                    .setContentTitle("title")
                    .setContentText("오늘 운동 일정이 있어요!")
                    .setAutoCancel(true)

            channelID ="specificday"
            code= requestCode
            name="specificdayAlarm"
            builder.setChannelId(channelID)
            val notificationChannel= NotificationChannel(channelID,name,NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel)

            notificationManager.notify(code, builder.build())
            return
        }
        else if(type ==1){//매일 받는 알람
            val text=intent?.getStringExtra("content")
            pendingIntent=PendingIntent.getActivity(context,100,i,PendingIntent.FLAG_UPDATE_CURRENT)
            val builder=NotificationCompat.Builder(context,"everyday")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_baseline_timer_24)
                    .setContentTitle("title")
                    .setContentText(text)
                    .setAutoCancel(true)
            channelID ="everyday"
            code= 100
            name="everydayAlarm"
            builder.setChannelId("everyday")
            val notificationChannel=NotificationChannel("everyday","everydayAlarm",NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel)

            notificationManager.notify(100, builder.build())
        }



    }

}