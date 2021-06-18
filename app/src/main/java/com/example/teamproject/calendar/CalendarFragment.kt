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
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class CalendarFragment : Fragment() {
    var data: ArrayList<Myroutines> = ArrayList()
    private lateinit var curr_date: String
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var mydbhelper: Mydbhelper
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_calendar, container, false)
        init(rootView)
        fabInit(rootView)
        return rootView
    }

    private fun fabInit(rootView: View) {
        val fab = rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(activity, AddRoutineActivity::class.java)
            intent.putExtra("date", curr_date)
            startActivity(intent)
        }
    }

    private fun init(rootView: View) {
        mydbhelper = Mydbhelper()
        curr_date = LocalDate.now().toString()

        val user_id = FirebaseAuth.getInstance().currentUser!!.uid
        val calendarView = rootView.findViewById<CalendarView>(R.id.calendarView)

        initRecyclerView(rootView)

        calendarView.setOnDateChangeListener { _, year, month, day ->
            curr_date = LocalDate.of(year, month + 1, day).toString()
            mydbhelper.getRoutineList(user_id, curr_date, object : Mydbhelper.MyCallbakclist {
                override fun onCallbacklist(value: ArrayList<Myroutines>) {
                    super.onCallbacklist(value)
                    data.clear()

                    data.add(Myroutines("user_id", user_id)) // remove할 때 아이디 정보가 필요함.

                    for (v in value) {
                        data.add(Myroutines(v.name, v.count))
                    }

                    recyclerView.adapter?.notifyDataSetChanged()
                }
            })

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