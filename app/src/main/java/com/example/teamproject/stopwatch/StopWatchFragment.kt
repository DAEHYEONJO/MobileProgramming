package com.example.teamproject.stopwatch

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentStopBinding
import com.example.teamproject.databinding.StopWatchAlertDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
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

    lateinit var exeNameAdapter : ArrayAdapter<String>
    lateinit var exeNameDbHelper : ExeNameDbHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("stopwatch","onCreateView")
        binding = FragmentStopBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    private fun initExeNameAdapter() {
        exeNameDbHelper = ExeNameDbHelper(requireContext())
        val exeNameList = exeNameDbHelper.getAll()
        exeNameAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,exeNameList)
        binding?.inputExeName?.setAdapter(exeNameAdapter)
        binding?.inputExeName?.isCursorVisible = false
        binding?.inputExeName?.setOnItemClickListener { parent, view, position, id ->
            val immHide = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            immHide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity!=null){
            stopWatchService = (activity as MainActivity).stopWatchService
        }else{
            Log.d("stopwatch","메인액티비티 null ")
        }
        Log.d("stopwatch","onViewCreated")
        initExeNameAdapter()

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
                        //(childFragmentManager as StopWatchFragment).binding?.micText?.text = getString(R.string.start)만
                        binding?.apply {
                            if (inputExeName.text.isEmpty()){
                                showWarningDiaglog(false)
                            }else{
                                showWarningDiaglog(true)
                            }
                        }
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
                    if (inputExeName.text.isEmpty()){
                        showWarningDiaglog(false)
                    }else{
                        showWarningDiaglog(true)
                    }
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
                    inputExeName.text.clear()
                }
            }
        }
    }

    private fun showWarningDiaglog(flag : Boolean) {

        var recordHour = stopWatchService.hour.toString()
        if (recordHour.length == 1) recordHour = "0$recordHour"
        var recordMin = stopWatchService.min.toString()
        if (recordMin.length == 1) recordMin = "0$recordMin"
        var recordSec = stopWatchService.sec.toString()
        if (recordSec.length == 1) recordSec = "0$recordSec"
        var recordMsec = stopWatchService.msec.toString()
        if (recordMsec.length == 1) recordMsec = "0$recordMsec"
        var recordStr = "$recordHour 시간 $recordMin 분 $recordSec 초 $recordMsec"

        val builder = AlertDialog.Builder(requireContext())
        val binding = StopWatchAlertDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
                .setCancelable(false)

        binding.recordText.text = recordStr
        binding.inputName.setAdapter(exeNameAdapter)
        binding.inputName.isCursorVisible = false
        if (!flag){
            binding.inputName.setBackgroundResource(R.drawable.watch_input_line_red)
            binding.inputName.setHintTextColor(resources.getColor(R.color.stop))
        }else{
            binding.inputName.setText(this.binding?.inputExeName?.text.toString())
        }
        binding.inputName.addTextChangedListener {
            if (it?.count()==0){
                binding.inputName.setBackgroundResource(R.drawable.watch_input_line_red)
                binding.inputName.setHintTextColor(resources.getColor(R.color.stop))
            }else{
                binding.inputName.setBackgroundResource(R.drawable.watch_input_exe_line)
                binding.inputName.setHintTextColor(resources.getColor(R.color.default_hint))
            }
        }
        binding.inputName.setOnItemClickListener { parent, view, position, id ->
            val immHide = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            immHide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

        val dialog = builder.show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.notRecord.setOnClickListener {
            dialog.dismiss()
        }
        binding.addRecord.setOnClickListener {
            if (binding.inputName.text.isEmpty()){
                binding.inputName.requestFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
            }else{
                val exeName = binding.inputName.text.toString()
                addExeRecord(exeName)
                dialog.dismiss()
            }
        }
    }

    private fun addExeRecord(name : String){
        val recordHour = stopWatchService.hour.toString()
        val recordMin = stopWatchService.min.toString()
        val recordSec = stopWatchService.sec.toString()
        val recordMsec = stopWatchService.msec.toString()
        val recordStr = "$recordHour:$recordMin:$recordSec:$recordMsec"

        Log.d("stopwatch","그만들어옴")
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val formatted = current.format(formatter)

        val curUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val exeRecordDb = FirebaseFirestore.getInstance().collection("Profile").document(curUserId).collection("ExeRecord")
        val newData = hashMapOf(formatted to recordStr)
        exeRecordDb.document(name).set(newData, SetOptions.merge())
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