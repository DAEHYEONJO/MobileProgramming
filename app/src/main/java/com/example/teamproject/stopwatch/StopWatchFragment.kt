package com.example.teamproject.stopwatch

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentStopBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.*
import kotlin.concurrent.timer

class StopWatchFragment : Fragment() {

    var binding : FragmentStopBinding? = null
    lateinit var stopWatchService :StopWatchService
    var timer : Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("stopwatch","onCreateView")

        binding = FragmentStopBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity!=null){
            stopWatchService = (activity as MainActivity).stopWatchService

        }else{
            Log.d("stopwatch","메인액티비티 null ")
        }
        Log.d("stopwatch","onViewCreated")


        timer = timer(period = 10) {
            settingTimes()
        }

        initBtn()
    }

    private fun initBtn() {
        binding?.apply {
            startBtn.setOnClickListener {
                if (!stopWatchService.isRunnig) {
                    stopWatchService.startStopWatch()
                }
            }
            pauseBtn.setOnClickListener {
                if (stopWatchService.isRunnig) {
                    stopWatchService.stopStopWatch()
                }
            }
            resetBtn.setOnClickListener {
                if (!stopWatchService.isRunnig) stopWatchService.resetStopWatch()
            }
        }
    }

    private fun settingTimes() {
        CoroutineScope(Dispatchers.Main).launch {
            var hour = stopWatchService.hour
            var min = stopWatchService.min
            var sec = stopWatchService.sec
            var msec = stopWatchService.msec
            binding?.apply {
                if (hour - 10 < 0) {
                    this.hour.text = "0$hour"
                } else {
                    this.hour.text = hour.toString()
                }

                if (min - 10 < 0) {
                    this.min.text = "0$min"
                } else {
                    this.min.text = min.toString()
                }

                if (sec - 10 < 0) {
                    this.sec.text = "0$sec"
                } else {
                    this.sec.text = sec.toString()
                }

                if (msec - 10 < 0) {
                    this.msec.text = "0$msec"
                } else {
                    this.msec.text = msec.toString()
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        Log.d("stopwatch","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("stopwatch","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("stopwatch","onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d("stopwatch","onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("stopwatch","onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("stopwatch","onDestroyView")
        timer?.cancel()
        timer?.purge()
        timer = null
        binding = null
    }

}