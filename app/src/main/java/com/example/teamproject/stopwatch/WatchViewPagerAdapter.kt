package com.example.teamproject.stopwatch

import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class WatchViewPagerAdapter(childFragmentManager: FragmentManager,lifecycle:Lifecycle): FragmentStateAdapter(childFragmentManager,lifecycle) {
    override fun getItemCount(): Int = 3


    lateinit var stopWatchFragment: StopWatchFragment
    lateinit var timerFragment : TimerFragment
    lateinit var recordFragment: RecordFragment
    init {
        stopWatchFragment = StopWatchFragment()
        timerFragment = TimerFragment()
        recordFragment = RecordFragment()
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                stopWatchFragment
            }
            1->{
                timerFragment
            }
            2->{
                recordFragment
            }
            else->{
                stopWatchFragment
            }
        }
    }
}