package com.example.teamproject.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.Mydbhelper
import com.example.teamproject.Myroutines
import com.example.teamproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class CalendarFragment : Fragment() {
    var data: ArrayList<Myroutines> = ArrayList()
    lateinit var curr_date: String
    lateinit var recyclerView: RecyclerView
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var mydbhelper: Mydbhelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)
        data.add(Myroutines("test", "2"))
        init(rootView)
        fabInit(rootView)
        return rootView
    }

    private fun fabInit(rootView: View) {
        var fab = rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            var intent = Intent(activity, AddRoutineActivity::class.java)
            intent.putExtra("date", curr_date)
            startActivity(intent)
        }
    }

    private fun init(rootView: View) {
        mydbhelper = Mydbhelper()
        var calendarView = rootView.findViewById<CalendarView>(R.id.calendarView)
        curr_date = LocalDate.now().toString()


        initRecyclerView(rootView)
        calendarView.setOnDateChangeListener { view, year, month, day ->
            curr_date = LocalDate.of(year, month + 1, day).toString()
            mydbhelper.readlist("1234", curr_date, object : Mydbhelper.MyCallbakclist {
                override fun onCallbacklist(value: ArrayList<Myroutines>) {
                    super.onCallbacklist(value)
                    data.clear()
                    for(v in value){
                        if(v.name!="date")
                            data.add(Myroutines(v.name, v.count))
                    }
                }
            })
            recyclerView.swapAdapter(CalendarAdapter(data), true)
        }
    }

    private fun initRecyclerView(rootView: View) {
        recyclerView = rootView.findViewById<RecyclerView>(R.id.calendar_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this.activity,
            LinearLayoutManager.VERTICAL, false
        )
        calendarAdapter = CalendarAdapter(data)
        recyclerView.adapter = calendarAdapter
    }
}