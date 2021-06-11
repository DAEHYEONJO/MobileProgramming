package com.example.teamproject.stopwatch

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class StopWatchService : Service() {

    var notificationManager : NotificationManager? = null
    var notificationId = 100

    private val binder = Mybinder()
    var hour = 0
    var min = 0
    var sec = 0
    var msec = 0
    var time = 0
    var isRunning : Boolean by Delegates.observable(false){ property, oldValue, newValue ->
        Log.d("stopwatchservice","chagne isRunning old : $oldValue -> new : $newValue")
    }
    lateinit var timerTask : Timer

    var isTimerRunning = false
    var isTimerStarted : Boolean by Delegates.observable(false){property, oldValue, newValue ->
        Log.d("timerfragment","chagne isTimerStarted old : $oldValue -> new : $newValue")
        if (!newValue && oldValue){
            Log.d("timerfragment","타이머만료+브로드캐스트+알람")
            val broadcastIntent = Intent("com.example.TIMERFINISH")
            broadcastIntent.putExtra("finish",true)
            sendBroadcast(broadcastIntent)
            makeNotification()
        }
    }
    var timerMaxTime = 0
    lateinit var timerTimer: Timer
    var timerHour = 0
    var timerMin = 0
    var timerSec = 0

    override fun onBind(intent: Intent): IBinder {
        Log.i("stopwatchservice","onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("stopwatchservice","onCreate")
        timerTask = Timer()
        timerTimer = Timer()
    }

    inner class Mybinder: Binder(){
        fun getService():StopWatchService = this@StopWatchService
    }

    fun makeNotification(){
        val channel = NotificationChannel("channelTimer","timer",NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            enableVibration(true)
            enableLights(true)
            lightColor = Color.RED
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this,"channelTimer")
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setColor(Color.BLACK)
            .setContentTitle("시계")
            .setContentText("타이머 만료")
            .setAutoCancel(true)

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("timerExpire","te")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivities(this,1, arrayOf(intent),PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()
        notificationManager!!.notify(notificationId,notification)
    }

    fun startStopWatch(){
        isRunning = true
        timerTask = timer(period = 10){
            time ++
            hour = time/360000
            min = time/6000
            min %= 60
            sec = time/100
            sec %= 60
            msec = time%100
        }
    }

    fun stopStopWatch(){
        if (isRunning){
            Log.d("stopwatchservice","stopStopWatch")
            timerTask.cancel()
            timerTask.purge()
            isRunning = false
        }
    }

    fun resetStopWatch(){
        hour = 0
        min = 0
        sec = 0
        msec = 0
        time = 0
    }


    fun startTimer(){
        Log.d("timerfragment","타이머 시작!!!")
        if (timerMaxTime!=0 && !isTimerRunning){
            isTimerRunning = true
            isTimerStarted = true
            timerTimer = timer(period = 1000){
                Log.d("timerfragment",timerMaxTime.toString())
                if (timerMaxTime<=0) {
                    Log.d("timerfragment","타이머 끝")
                    isTimerStarted = false
                    isTimerRunning = false
                    timerMaxTime = 0

                    this.cancel()
                }
                timerHour = timerMaxTime/3600
                timerMin = (timerMaxTime%3600)/60
                timerSec = (timerMaxTime%3600)%60
                timerMaxTime--
            }
        }
    }

    fun stopTimer(){
        if (isTimerRunning){
            timerTimer.cancel()
            timerTimer.purge()
            isTimerRunning = false
        }
    }

    fun resetTimer(){
        stopTimer()
        isTimerStarted = false
        timerMaxTime = 0
        timerHour = 0
        timerSec = 0
        timerMin = 0
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("stopwatchservice","onStartCommand")

        return START_STICKY //서비스가 시스템에 의해 중지되면 재시작 한다
        //create 다음 실행ㅇ되는 함수
    }


    override fun onDestroy() {
        Log.i("stopwatchservice","onDestroy")
        super.onDestroy()

    }


}