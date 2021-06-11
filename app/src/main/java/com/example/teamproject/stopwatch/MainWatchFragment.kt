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
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentMainWatchBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainWatchFragment : Fragment() {

    var binding : FragmentMainWatchBinding? = null
    val tabTextArr = arrayOf("스톱워치", "타이머", "기록확인")
    val tabImgArr = arrayOf(R.drawable.ic_baseline_timer_24,R.drawable.ic_baseline_watch_later_24,R.drawable.ic_baseline_format_list_numbered_24)

    var speechRecognizer: SpeechRecognizer? = null
    var resultText = ""
    lateinit var stopWatchService : StopWatchService
    private val speechRecognizerIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity?.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA)
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("mainwatchfragment","onCreateView")
        binding = FragmentMainWatchBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("mainwatchfragment","onViewCreated")

        if(activity!=null){
            stopWatchService = (activity as MainActivity).stopWatchService

        }else{
            Log.d("stopwatch","메인액티비티 null ")
        }
        startStt()
        initViewPager()

    }


    private fun initViewPager() {
        binding?.viewPager?.adapter = WatchViewPagerAdapter(childFragmentManager,lifecycle)
        binding?.viewPager?.isSaveEnabled = false
        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){tab, position ->
            tab.text = tabTextArr[position]
            tab.setIcon(tabImgArr[position])
            /*if (position!=0){
                speechRecognizer?.stopListening()
            }else{
                speechRecognizer?.startListening(speechRecognizerIntent)
            }*/
        }.attach()
    }

    private fun startStt() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(recognitionListener())
            startListening(speechRecognizerIntent)
        }
    }

    private fun recognitionListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
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
                        //(childFragmentManager as StopWatchFragment).binding?.micText?.text = getString(R.string.stop)
                    }

                }
                "그만"->{
                    if(stopWatchService.isRunning){
                        stopWatchService.stopStopWatch()
                        //(childFragmentManager as StopWatchFragment).binding?.micText?.text = getString(R.string.start)
                    }
                }
            }
            speechRecognizer?.startListening(speechRecognizerIntent)
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d("mainwatchfragment","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("mainwatchfragment","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("mainwatchfragment","onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d("mainwatchfragment","onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mainwatchfragment","onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("mainwatchfragment","onDestroyView")
        speechRecognizer?.stopListening()
        speechRecognizer = null
        binding = null
    }
}