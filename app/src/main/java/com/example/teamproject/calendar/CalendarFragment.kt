package com.example.teamproject.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.teamproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class CalendarFragment : Fragment() {
    private var curr_date:String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView =inflater.inflate(R.layout.fragment_calendar, container, false)
        var fab = rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener{
            var intent = Intent(activity, AddRoutineActivity::class.java)
            intent.putExtra("date", curr_date)
            startActivity(intent)
        }

        var calendarView = rootView.findViewById<CalendarView>(R.id.calendarView)
        curr_date = LocalDate.now().toString()

        calendarView.setOnDateChangeListener { view, year, month, day ->
            curr_date = LocalDate.of(year, month+1, day).toString()
        }
        return rootView
    }
}