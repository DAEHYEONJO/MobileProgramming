package com.example.teamproject.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class rebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarm=AlarmService(context)
            alarm.everydayAlarm()
        }
    }
}