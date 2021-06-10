package com.example.teamproject.stopwatch

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentMainWatchBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainWatchFragment : Fragment() {

    var binding : FragmentMainWatchBinding? = null
    val tabTextArr = arrayOf("스톱워치", "타이머", "기록확인")

    var speechRecognizer: SpeechRecognizer? = null
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
        initViewPager()
        startStt()
    }


    private fun initViewPager() {
        binding?.viewPager?.adapter = WatchViewPagerAdapter(childFragmentManager,lifecycle)
        binding?.viewPager?.isSaveEnabled = false
        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){tab, position ->
            tab.text = tabTextArr[position]
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
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.forEach {
                Log.d("sttresult",it)
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