package com.example.teamproject.stopwatch

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class StopWatchService : Service() {

    private val binder = Mybinder()
    var hour = 0
    var min = 0
    var sec = 0
    var msec = 0
    var time = 0
    var isRunnig = false
    lateinit var timerTask : Timer


    override fun onBind(intent: Intent): IBinder {
        Log.i("stopwatchservice","onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("stopwatchservice","onCreate")
    }

    inner class Mybinder: Binder(){
        fun getService():StopWatchService = this@StopWatchService
    }

    fun startStopWatch(){
        isRunnig = true
        timerTask = timer(period = 10){
            time ++
            hour = time/360000
            min = time/6000
            sec = time/100
            sec %= 60
            msec = time%100
        }
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