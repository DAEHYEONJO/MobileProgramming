package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class CalendarActivity : AppCompatActivity() {
    lateinit var curr_date:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        init()
    }
    fun init(){
        var fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener{
            intent = Intent(this, AddRoutineActivity::class.java)
            intent.putExtra("date", curr_date)
            startActivity(intent)
        }

        var calendarView = findViewById<CalendarView>(R.id.calendarView)
        curr_date = LocalDate.now().toString()

        calendarView.setOnDateChangeListener { view, year, month, day ->
            curr_date = LocalDate.of(year, month+1, day).toString()
        }
    }
}