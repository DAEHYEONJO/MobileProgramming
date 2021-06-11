package com.example.teamproject.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopWatchViewModel:ViewModel() {
    var isRunning = MutableLiveData<Boolean>()

    var hour = MutableLiveData<String>()
    var min = MutableLiveData<String>()
    var sec = MutableLiveData<String>()
    var msec = MutableLiveData<String>()

    init {
        hour.value = "00"
        min.value = "00"
        sec.value = "00"
        msec.value = "00"
    }
}