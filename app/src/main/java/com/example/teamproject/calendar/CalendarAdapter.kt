package com.example.teamproject.calendar

import android.util.Log
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_routine_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return files?.size!!
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val current = files?.get(position)
        if (validationCheck(current)) {
            holder.calendar_routine_name.text = current?.name
            holder.calendar_routine_count.text = current?.count
            holder.calendar_close_button.setOnClickListener {
                mydbhelper.deleteroutine(
                    holder.curr_id,
                    holder.curr_date,
                    holder.calendar_routine_name.text.toString()
                )
                items.removeAt(position)
                notifyDataSetChanged()
            }
        } else {
            hideItem(holder, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // row가 들어옴
        var calendar_routine_name: TextView = itemView.findViewById(R.id.calendar_routine_name)
        var calendar_routine_count: TextView = itemView.findViewById(R.id.calendar_routine_count)
        var calendar_close_button: ImageButton = itemView.findViewById(R.id.calendar_close_button)
        var curr_id: String = items[0].count
        var curr_date: String = items[1].count

        init {
            Log.d("testtest", "curr_date: $curr_id")
            Log.d("testtest", "curr_date: $curr_date")

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