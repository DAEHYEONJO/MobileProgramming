package com.example.teamproject.stopwatch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentStopBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class StopWatchFragment : Fragment() {

    var binding : FragmentStopBinding? = null
    lateinit var stopWatchService :StopWatchService
    var timer : Timer? = null

    private val stopWatchViewModel : StopWatchViewModel by activityViewModels<StopWatchViewModel>()

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




        stopWatchViewModel.isRunning.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("stopwatch","프래그먼트 뷰모델 isRunnig $it")
            if (it){
                binding?.micText?.text = getString(R.string.stop)
                timer = timer(period = 10) {
                    settingTimes()
                }
            }else{
                binding?.micText?.text = getString(R.string.start)
                binding?.hour?.text = stopWatchViewModel.hour.value
                binding?.min?.text = stopWatchViewModel.min.value
                binding?.sec?.text = stopWatchViewModel.sec.value
                binding?.msec?.text = stopWatchViewModel.msec.value
            }
        })

        initBtn()
    }

    private fun initBtn() {

        binding?.apply {
            if (stopWatchService.isRunning){
                micText.text = getString(R.string.stop)
                startBtn.isEnabled = false
                resetBtn.isEnabled = false
                pauseBtn.isEnabled = true
            }else{
                micText.text = getString(R.string.start)
                startBtn.isEnabled = true
                pauseBtn.isEnabled = false
                if (stopWatchService.time!=0){
                    resetBtn.isEnabled = true
                }else{
                    resetBtn.isEnabled = false
                }

            }
            startBtn.setOnClickListener {
                if (!stopWatchService.isRunning) {
                    micText.text = getString(R.string.stop)
                    stopWatchService.startStopWatch()
                    it.isEnabled = false
                    pauseBtn.isEnabled = true
                    resetBtn.isEnabled = false
                    stopWatchViewModel.isRunning.value = stopWatchService.isRunning
                }
            }
            pauseBtn.setOnClickListener {
                micText.text = getString(R.string.start)
                if (stopWatchService.isRunning) {
                    stopWatchService.stopStopWatch()
                    it.isEnabled = false
                    startBtn.isEnabled = true
                    resetBtn.isEnabled = true
                    stopWatchViewModel.isRunning.value = stopWatchService.isRunning
                }
            }
            resetBtn.setOnClickListener {
                if (!stopWatchService.isRunning) {
                    micText.text = getString(R.string.start)
                    stopWatchService.resetStopWatch()
                    it.isEnabled = false
                    startBtn.isEnabled = true
                    pauseBtn.isEnabled = false
                    stopWatchViewModel.isRunning.value = stopWatchService.isRunning
                }
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
        stopWatchViewModel.hour.value = binding?.hour?.text.toString()
        stopWatchViewModel.min.value = binding?.min?.text.toString()
        stopWatchViewModel.sec.value = binding?.sec?.text.toString()
        stopWatchViewModel.msec.value = binding?.msec?.text.toString()
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