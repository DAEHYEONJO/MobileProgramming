package com.example.teamproject.video

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R

class MyVideoItemRecyclerViewAdapter(
    private val activity:Activity,private val values: ArrayList<VideoData>
) : RecyclerView.Adapter<MyVideoItemRecyclerViewAdapter.ViewHolder>() {

     fun clearAdapter(){
         if(values.isNotEmpty()){
             val size=values.size
             //Log.i("check",size.toString())
             values.clear()
             for(i in 0 until size){
                 notifyItemRemoved(i)
             }
         }
    }

    fun insertData(data: VideoData){
        values.add(data)
        notifyItemInserted(values.lastIndex)
    }

    interface OnItemClickListener{
        fun OnTitleClick(holder: MyVideoItemRecyclerViewAdapter.ViewHolder, view: View, data: VideoData, position: Int)
    }

    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text=item.title
        holder.video.loadData(item.url,"text/html","utf-8")
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val video: WebView = view.findViewById(R.id.video)
        init {
            video.settings.javaScriptEnabled=true
            video.settings.javaScriptCanOpenWindowsAutomatically=true
            video.settings.mediaPlaybackRequiresUserGesture=false
            video.settings.loadWithOverviewMode=true
            video.settings.builtInZoomControls=true
            video.webChromeClient= FullscreenableChromeClient(activity)
            view.setOnClickListener {
                itemClickListener?.OnTitleClick(this,it,values[absoluteAdapterPosition],absoluteAdapterPosition)
            }
        }
    }
}