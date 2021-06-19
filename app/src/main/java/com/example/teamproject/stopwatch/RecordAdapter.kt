package com.example.teamproject.stopwatch

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R
import com.example.teamproject.databinding.RecordRowBinding

class RecordAdapter(var list : ArrayList<Record>):RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : RecordRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecordRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(position){
            0->{holder.binding.img.setImageResource(R.drawable.first)}
            1->{holder.binding.img.setImageResource(R.drawable.second)}
            2->{holder.binding.img.setImageResource(R.drawable.third)}
            else->{holder.binding.img.setImageResource(R.drawable.ic_baseline_mood_bad_24)}
        }
        holder.binding.date.text = list[position].date
        val total = list[position].time
        val hour = total/3600
        val min = (total/60)%60
        val sec = total%60
        Log.d("record","$hour $min $sec")
        var time = hour.toString()+"시간 "+min.toString()+"분 "+sec.toString()+"초"
        holder.binding.record.text = time
        val avg = list[itemCount-1].avg
        val ahour = avg/3600
        val amin = (avg/60)%60
        val asec = avg%60
        var atime = ahour.toString()+"시간 "+amin.toString()+"분 "+asec.toString()+"초"
        holder.binding.avg.text = atime


    }
}