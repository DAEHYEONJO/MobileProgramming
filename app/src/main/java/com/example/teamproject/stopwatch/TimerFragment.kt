package com.example.teamproject.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentTimerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class TimerFragment : Fragment() {

    private var binding : FragmentTimerBinding? = null
    lateinit var stopWatchService :StopWatchService
    private val stopWatchViewModel : StopWatchViewModel by activityViewModels<StopWatchViewModel>()
    lateinit var timer : Timer

    var pausePressed = false

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

        initBroadCast()

        initPicker()
        initTimerBtn()
        stopWatchViewModel.isTimerRunning.observe(viewLifecycleOwner, Observer {
            Log.d("timerfragment","타이머 돌아가고 있는지 : $it")
            if (it){
                //타이머 숫자 내려가고 있는 경우
                timer = timer(period = 10){
                    CoroutineScope(Dispatchers.Main).launch{
                        var hour = stopWatchService.timerHour
                        var min = stopWatchService.timerMin
                        var sec = stopWatchService.timerSec
                        binding?.apply {
                            if (hour -10<0){
                                timerHour.text = "0$hour"
                            }else{
                                timerHour.text = hour.toString()
                            }
                            if (min -10<0){
                                timerMin.text = "0$min"
                            }else{
                                timerMin.text = min.toString()
                            }
                            if (sec -10<0){
                                timerSec.text = "0$sec"
                            }else{
                                timerSec.text = sec.toString()
                            }
                        }
                    }
                }
                setVisibility(picker = View.GONE, timer = View.VISIBLE)
                setEnabled(reset = false,pause = true,start = false)

            }else{
                //타이머 안돌아가고 있는 경우
                binding?.apply {
                    if (stopWatchService.isTimerStarted&&!pausePressed){
                        //타이머 중지했었다가 돌아온경우
                        timerHour.text = stopWatchViewModel.timerHour.value.toString()
                        timerMin.text = stopWatchViewModel.timerMin.value.toString()
                        timerSec.text = stopWatchViewModel.timerSec.value.toString()
                        setVisibility(picker = View.GONE,timer = View.VISIBLE)
                        setEnabled(reset = true,pause = false,start = true)
                    }
                    if (!stopWatchService.isTimerStarted){
                        setVisibility(picker = View.VISIBLE,timer = View.GONE)
                        setEnabled(reset = false,pause = false,start = true)
                    }
                }
            }
        })
    }

    private fun initBroadCast() {
        requireActivity().registerReceiver(receiver, IntentFilter("com.example.TIMERFINISH"))
    }

    private fun setVisibility(picker : Int, timer : Int){
        binding?.apply {
            pickerLayout.visibility = picker
            timerLayout.visibility = timer
        }
    }

    private fun setEnabled(reset:Boolean,pause:Boolean,start:Boolean){
        binding?.apply {
            resetBtn.isEnabled = reset
            pauseBtn.isEnabled = pause
            startBtn.isEnabled = start
        }
    }

    private fun initTimerBtn() {
        binding?.apply {
            Log.d("timerfragment","initTimerBtn")
            startBtn.setOnClickListener {
                if (!stopWatchService.isTimerStarted) {
                    //타이머 시작 안한 경우
                    //타이머 시작 안한 경우
                    Log.d("timerfragment", "타이머 처음 시작하는 경우 ")

                    var totalTime = (hourPicker.value * 3600) + (minPicker.value * 60) + (secPicker.value)
                    Log.d("timerfragment", "totalTime : $totalTime stopWatchService.timerMaxTime : ${stopWatchService.timerMaxTime}")
                    if (totalTime != 0 && (stopWatchService.timerMaxTime == 0)) {
                        //처음 시작하는 경우
                        Log.d("timerfragment", "타이머 처음 시작하는 경우2 ")
                        if (hourPicker.value - 10 < 0) timerHour.text = "0${hourPicker.value}"
                        else timerHour.text = hourPicker.value.toString()
                        if (minPicker.value - 10 < 0) timerMin.text = "0${minPicker.value}"
                        else timerMin.text = minPicker.value.toString()
                        if (secPicker.value - 10 < 0) timerSec.text = "0${secPicker.value}"
                        else timerSec.text = secPicker.value.toString()
                        stopWatchService.timerMaxTime = totalTime
                        stopWatchService.startTimer()
                    }
                    if (totalTime == 0 && (stopWatchService.timerMaxTime == 0)) {
                        //처음 시작하는데 시간 입력 안한경우
                        Toast.makeText(activity, "시간을 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    stopWatchService.startTimer()
                }
                stopWatchViewModel.isTimerRunning.value= stopWatchService.isTimerRunning
            }
            pauseBtn.setOnClickListener {
                if (stopWatchService.isTimerRunning){
                    pausePressed = true
                    Log.d("timerfragment", "타이머 pause ")
                    stopWatchService.stopTimer()
                    setEnabled(reset = true,pause = false,start = true)
                    stopWatchViewModel.isTimerRunning.value= stopWatchService.isTimerRunning
                }
            }
            resetBtn.setOnClickListener {
                if(stopWatchService.isTimerStarted){
                    stopWatchService.resetTimer()
                }
                initPicker()
                setVisibility(picker = View.VISIBLE, timer = View.GONE)
                setEnabled(reset = false,pause = false,start = true)
                stopWatchViewModel.isTimerRunning.value= stopWatchService.isTimerRunning
            }
        }
    }

    private fun initPicker() {
        binding?.apply {
            if (!stopWatchService.isTimerRunning){
                pickerLayout.visibility = View.VISIBLE
                timerLayout.visibility = View.GONE
                hourPicker.minValue = 0
                hourPicker.maxValue = 23
                minPicker.minValue = 0
                minPicker.maxValue = 59
                secPicker.minValue = 0
                secPicker.maxValue = 59
            }else{
                pickerLayout.visibility = View.GONE
                timerLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("timerfragment","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("timerfragment","onPause")
        stopWatchViewModel.timerHour.value = binding?.timerHour?.text.toString()
        stopWatchViewModel.timerMin.value = binding?.timerMin?.text.toString()
        stopWatchViewModel.timerSec.value = binding?.timerSec?.text.toString()
    }

    private var receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("timerfragment","broadcast onReceive")
            val isFinish = intent?.getBooleanExtra("finish",false)
            if (isFinish!=null){
                if (isFinish&&(binding?.pickerLayout?.visibility==View.GONE)){
                    setEnabled(reset = true,pause = false,start = false)
                    stopWatchService.timerMaxTime = 0
                    //Toast.makeText(activity, "타이머 피니쉬", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(receiver)
    }
}