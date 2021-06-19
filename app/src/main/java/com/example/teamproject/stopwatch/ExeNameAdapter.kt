package com.example.teamproject.stopwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.databinding.ExeNameRowBinding

class ExeNameAdapter(var list : ArrayList<String>): RecyclerView.Adapter<ExeNameAdapter.ViewHolder>() {


    interface OnItemClickListener{
        fun onClick(name:String)
    }

    var listener : OnItemClickListener? = null

    inner class ViewHolder(val binding : ExeNameRowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                listener?.onClick(list[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExeNameRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = list[position]
    }
}