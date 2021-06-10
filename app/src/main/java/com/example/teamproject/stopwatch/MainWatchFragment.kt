package com.example.teamproject.stopwatch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentMainWatchBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainWatchFragment : Fragment() {

    var binding : FragmentMainWatchBinding? = null
    val tabTextArr = arrayOf("스톱워치", "타이머", "기록확인")

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
    }

    private fun initViewPager() {
        binding?.viewPager?.adapter = WatchViewPagerAdapter(childFragmentManager,lifecycle)
        binding?.viewPager?.isSaveEnabled = false
        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){tab, position ->
            tab.text = tabTextArr[position]
        }.attach()
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