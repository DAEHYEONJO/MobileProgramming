package com.example.teamproject.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.Mydbhelper
import com.example.teamproject.Myroutines
import com.example.teamproject.R


class CalendarAdapter(val items: ArrayList<Myroutines>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var files: ArrayList<Myroutines>? = items
    private var mydbhelper = Mydbhelper()
    lateinit var view:View
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_routine_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return files?.size!!
    }

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null


    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val current = files?.get(position)
        if (items.size > 1) {
            holder.curr_date = items[1].count
        }
        if (validationCheck(current)) {

            holder.calendar_routine_name.text = current?.name
            holder.calendar_routine_count.text = current?.count
            holder.calendar_close_button.setOnClickListener {
                itemClickListener?.OnItemClick(holder, view, position)
            }
        } else {
            hideItem(holder, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var calendar_routine_name: TextView = itemView.findViewById(R.id.calendar_routine_name)
        var calendar_routine_count: TextView = itemView.findViewById(R.id.calendar_routine_count)
        var calendar_close_button: ImageButton = itemView.findViewById(R.id.calendar_close_button)
        var curr_id: String = ""
        var curr_date: String = ""

        init {
            curr_id = items[0].count
        }
    }

    private fun hideItem(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.calendar_routine_name.visibility = View.GONE
        holder.calendar_routine_count.visibility = View.GONE
        holder.calendar_close_button.visibility = View.GONE
    }

    private fun validationCheck(item: Myroutines?): Boolean {
        return item != null &&
                item.name != "user_id" &&
                item.name != "date"
    }
}