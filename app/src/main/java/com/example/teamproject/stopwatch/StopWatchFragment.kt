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
    var timer : Timer? = null

    private val stopWatchViewModel : StopWatchViewModel by activityViewModels<StopWatchViewModel>()

    var speechRecognizer: SpeechRecognizer? = null
    var resultText = ""
    lateinit var stopWatchService : StopWatchService
    private val speechRecognizerIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity?.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA)
        }
    }
    private var isListening = false

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
            if (it){
                Log.d("stopwatch","프래그먼트 뷰모델 isRunnig $it")
                binding?.micText?.text = getString(R.string.stop)
                timer = timer(period = 10) {
                    settingTimes()
                }
            }else{
                Log.d("stopwatch","프래그먼트 뷰모델 isRunnig $it")
                Log.d("stopwatch","프래그먼트 뷰모델 hour ${stopWatchViewModel.hour.value}")
                Log.d("stopwatch","프래그먼트 뷰모델 min ${stopWatchViewModel.min.value}")
                Log.d("stopwatch","프래그먼트 뷰모델 sec ${stopWatchViewModel.sec.value}")
                Log.d("stopwatch","프래그먼트 뷰모델 msec ${stopWatchViewModel.msec.value}")
                binding?.micText?.text = getString(R.string.start)
                binding?.hour?.text = stopWatchViewModel.hour.value
                binding?.min?.text = stopWatchViewModel.min.value
                binding?.sec?.text = stopWatchViewModel.sec.value
                binding?.msec?.text = stopWatchViewModel.msec.value
            }
        })

        initBtn()
    }

    private fun startStt() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(recognitionListener())
            startListening(speechRecognizerIntent)
        }
    }


    private fun recognitionListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            isListening = true
            Log.d("stopwatch","음성인식 시작 !!!!!!!!음성인식 시작 !!!!!!!!음성인식 시작 !!!!!!!!음성인식 시작 !!!!!!!!")
        }

        override fun onRmsChanged(rmsdB: Float) {
        }

        override fun onBufferReceived(buffer: ByteArray?) {
        }

        override fun onPartialResults(partialResults: Bundle?) {
        }

        override fun onEvent(eventType: Int, params:Bundle?) {
        }

        override fun onBeginningOfSpeech() {
        }

        override fun onEndOfSpeech() {
        }

        override fun onError(error: Int) {
            when(error){
                SpeechRecognizer.ERROR_AUDIO->{Log.e("stt","ERROR_AUDIO")}
                SpeechRecognizer.ERROR_CLIENT->{Log.e("stt","ERROR_CLIENT")}
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS->{Log.e("stt","ERROR_INSUFFICIENT_PERMISSIONS")}
                SpeechRecognizer.ERROR_NETWORK->{Log.e("stt","ERROR_NETWORK")}
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT->{Log.e("stt","ERROR_NETWORK_TIMEOUT")}
                SpeechRecognizer.ERROR_NO_MATCH->{Log.e("stt","ERROR_NO_MATCH")}
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY->{Log.e("stt","ERROR_RECOGNIZER_BUSY")}
                SpeechRecognizer.ERROR_SERVER->{Log.e("stt","ERROR_SERVER")}
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT->{Log.e("stt","ERROR_SPEECH_TIMEOUT")}
            }
            speechRecognizer?.startListening(speechRecognizerIntent)
        }

        override fun onResults(results: Bundle?) {
            resultText = ""
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.forEach {
                Log.d("sttresult",it)
                resultText+=it
            }
            when(resultText){
                "시작"->{
                    if(!stopWatchService.isRunning){
                        stopWatchService.startStopWatch()
                        //StopWatchFragment().binding?.micText?.text = getString(R.string.stop)
                        //(childFragmentManager as StopWatchFragment).binding?.micText?.text = getString(R.string.stop)
                    }

                }
                "그만"->{
                    if(stopWatchService.isRunning){
                        stopWatchService.stopStopWatch()
                        //StopWatchFragment().binding?.micText?.text = getString(R.string.start)
                        //(childFragmentManager as StopWatchFragment).binding?.micText?.text = getString(R.string.start)
                    }
                }
            }
            stopWatchViewModel.isRunning.value = stopWatchService.isRunning
            speechRecognizer?.startListening(speechRecognizerIntent)
        }
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
        startStt()
    }

    override fun onPause() {
        super.onPause()
        Log.d("stopwatch","onPause")
        stopWatchViewModel.hour.value = binding?.hour?.text.toString()
        stopWatchViewModel.min.value = binding?.min?.text.toString()
        stopWatchViewModel.sec.value = binding?.sec?.text.toString()
        stopWatchViewModel.msec.value = binding?.msec?.text.toString()
        if (speechRecognizer!=null && isListening){
            speechRecognizer!!.stopListening()
            speechRecognizer = null
            isListening = false
        }
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
        speechRecognizer?.stopListening()
        speechRecognizer = null
        timer?.cancel()
        timer?.purge()
        timer = null
        binding = null
    }

}