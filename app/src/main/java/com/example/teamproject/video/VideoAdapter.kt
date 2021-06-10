package com.example.teamproject.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.databinding.VideoRowBinding



class VideoAdapter(val items :ArrayList<VideoData>): RecyclerView.Adapter<VideoAdapter.ViewHolder>()  {
    var showVideo =Array(itemCount,{0})
    var checkObserver=Array(itemCount,{0})

    interface OnItemClickListener{
        fun OnTitleClick(holder:ViewHolder, view: View, data: VideoData, position: Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: VideoRowBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.video.settings.javaScriptEnabled=true
            binding.video.settings.javaScriptCanOpenWindowsAutomatically=true
            binding.video.settings.mediaPlaybackRequiresUserGesture=false
            binding.video.settings.loadWithOverviewMode=true
            binding.video.settings.builtInZoomControls=true
            binding.video.webChromeClient= WebChromeClient()
            //binding.webView.visibility= View.GONE

            binding.root.setOnClickListener {
                itemClickListener?.OnTitleClick(this,it,items[absoluteAdapterPosition],absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= VideoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textView.text=items[position].title

            video.loadData(items[position].url,"text/html","utf-8")
//            if(showVideo[position]==1)
//                webView.visibility=View.VISIBLE
//            else
//                webView.visibility = View.GONE
        }
    }
}