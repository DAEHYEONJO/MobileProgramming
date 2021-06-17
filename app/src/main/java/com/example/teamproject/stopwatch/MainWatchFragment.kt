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
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentMainWatchBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainWatchFragment : Fragment() {

    var binding : FragmentMainWatchBinding? = null
    val tabTextArr = arrayOf("스톱워치", "타이머", "기록확인")
    val tabImgArr = arrayOf(R.drawable.ic_baseline_timer_24,R.drawable.ic_baseline_watch_later_24,R.drawable.ic_baseline_format_list_numbered_24)

    private val stopWatchViewModel : StopWatchViewModel by activityViewModels<StopWatchViewModel>()
    lateinit var watchViewPagerAdapter: WatchViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("mainwatchfragment","onCreateView")

        initExeList()

        binding = FragmentMainWatchBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    private fun initExeList() {
        val db = FirebaseFirestore.getInstance()
        val exeCollection = db.collection("ExeList")
        val exeDb = exeCollection.document("hi4")
        exeCollection.get().addOnSuccessListener {
            for (doc in it){
                Log.d("dsfdsa","exe name : ${doc.id}")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("mainwatchfragment","onViewCreated")
        initViewPager()

    }


    private fun initViewPager() {
        Log.d("viewmodel","저장된.selectedViewPagerPosition : ${stopWatchViewModel.selectedViewPagerPosition.value}")
        watchViewPagerAdapter = WatchViewPagerAdapter(childFragmentManager,lifecycle)
        binding?.viewPager?.adapter = watchViewPagerAdapter
        binding?.viewPager?.isSaveEnabled = false
        binding?.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("viewmodel ", "onPageSelected change position : $position")
                stopWatchViewModel.selectedViewPagerPosition.value = position
            }
        })
        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){tab, position ->
            tab.text = tabTextArr[position]
            tab.setIcon(tabImgArr[position])
        }.attach()
        binding?.viewPager?.currentItem = stopWatchViewModel.selectedViewPagerPosition.value!!
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

        binding = null
    }
}