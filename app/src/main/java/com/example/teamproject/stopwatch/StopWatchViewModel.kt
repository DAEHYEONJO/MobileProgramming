package com.example.teamproject.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopWatchViewModel:ViewModel() {
    var isRunning = MutableLiveData<Boolean>()

    var hour = MutableLiveData<String>()
    var min = MutableLiveData<String>()
    var sec = MutableLiveData<String>()
    var msec = MutableLiveData<String>()

    var isTimerRunning = MutableLiveData<Boolean>()
    var isTimerStarted = MutableLiveData<Boolean>()
    var timerHour = MutableLiveData<String>()
    var timerMin = MutableLiveData<String>()
    var timerSec = MutableLiveData<String>()

    var selectedViewPagerPosition = MutableLiveData<Int>()


    init {
        hour.value = "00"
        min.value = "00"
        sec.value = "00"
        msec.value = "00"

        timerHour.value = "00"
        timerMin.value = "00"
        timerSec.value = "00"

        selectedViewPagerPosition.value = 0
    }
}