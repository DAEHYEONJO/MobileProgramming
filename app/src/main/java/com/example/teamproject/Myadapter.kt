package com.example.teamproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R

class Myadapter(val routines: ArrayList<Myroutines>) :RecyclerView.Adapter<Myadapter.ViewHolder>() {

    var itemClickListener: AdapterView.OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val routine: TextView = itemView.findViewById(R.id.textView)
        val count : TextView = itemView.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.routine.text = routines[position].name
        holder.count.text = routines[position].count
    }

    override fun getItemCount(): Int {
        return routines.size
    }

}