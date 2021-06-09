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
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentStopBinding
import java.util.*

class StopWatchFragment : Fragment() {

    var binding : FragmentStopBinding? = null
    var speechRecognizer: SpeechRecognizer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStopBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            testSttBtn.setOnClickListener {
                startStt()
            }
        }
    }

    private fun startStt() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity?.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(recognitionListener())
            startListening(speechRecognizerIntent)
        }
    }

    private fun recognitionListener() = object : RecognitionListener{
        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(activity, "음성 인식 시작", Toast.LENGTH_SHORT).show()
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
        }

        override fun onResults(results: Bundle?) {
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.forEach {
                binding?.sttResult?.text = it.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}