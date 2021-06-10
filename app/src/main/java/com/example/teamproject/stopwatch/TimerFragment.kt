package com.example.teamproject.stopwatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentTimerBinding
import kotlin.math.min

class TimerFragment : Fragment() {

    private var binding : FragmentTimerBinding? = null
    lateinit var stopWatchService :StopWatchService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity!=null){
            stopWatchService = (activity as MainActivity).stopWatchService
        }

        initPicker()
        initTimer()
    }

    private fun initTimer() {
        binding?.apply {
            if (stopWatchService.isTimerRunning){
                this.resetBtn.isEnabled = false

            }
        }
    }

    private fun initPicker() {
        binding?.apply {
            hourPicker.minValue = 0
            hourPicker.maxValue = 23
            minPicker.minValue = 0
            minPicker.maxValue = 59
            secPicker.minValue = 0
            secPicker.maxValue = 59
        }
    }
}