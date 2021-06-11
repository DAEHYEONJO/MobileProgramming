package com.example.teamproject.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.Myroutines
import com.example.teamproject.R

class CalendarAdapter(val items: ArrayList<Myroutines>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>(){
    private var files: ArrayList<Myroutines>? = items

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_routine_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return files?.size!!
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val current = files?.get(position)
        if(current!=null){
            holder.calendar_routine_name.text = current.name
            holder.calendar_routine_count.text = current.count
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // row가 들어옴
        var calendar_routine_name: TextView = itemView.findViewById(R.id.calendar_routine_name)
        var calendar_routine_count: TextView = itemView.findViewById(R.id.calendar_routine_count)

        init {

        }
    }
}