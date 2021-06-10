package com.example.teamproject.video

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject.databinding.ActivityShowVideoBinding

open class ShowVideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowVideoBinding
    lateinit var adapter: VideoAdapter
    var data:ArrayList<VideoData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
    }
    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager=
                LinearLayoutManager(this@ShowVideoActivity, LinearLayoutManager.VERTICAL,false)
            adapter= VideoAdapter(data)
            adapter.itemClickListener=object :VideoAdapter.OnItemClickListener{
                override fun OnTitleClick(
                    holder: VideoAdapter.ViewHolder,
                    view: View,
                    data: VideoData,
                    position: Int
                ) {
                    Toast.makeText(this@ShowVideoActivity,"asd", Toast.LENGTH_SHORT).show()
//                    if(holder.binding.webView.visibility==View.GONE){
//                        holder.binding.webView.visibility=View.VISIBLE
//                        adapter.showVideo[position] = 1
//                    }else{
//                        holder.binding.webView.visibility = View.GONE
//                        adapter.showVideo[position] = 0
//
//                    }
//                    holder.binding.webView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                        override fun onReady(youTubePlayer: YouTubePlayer) {
//                            youTubePlayer.loadVideo(data.url, 0f)
//                            youTubePlayer.pause()
//
//                        }
//                    })
                    holder.binding.video.webChromeClient=FullscreenableChromeClient(this@ShowVideoActivity)
                    //holder.binding.video.visibility=View.VISIBLE
//                    if(adapter.checkObserver[position]==0) {
//                        lifecycle.addObserver(holder.binding.webView)
//                        adapter.checkObserver[position]=1
//                        holder.binding.webView.addFullScreenListener(object  :
//                            YouTubePlayerFullScreenListener {
//                            override fun onYouTubePlayerEnterFullScreen() {
//                                val decorView: View = this@ShowVideoActivity.getWindow().getDecorView()
//                                hideSystemUi()
////                                for (i in 0 until binding.root.childCount) {
////                                    binding.root.getChildAt(i).visibility = View.GONE
////                                    view.invalidate()
////                                }
//                            }
//
//                            override fun onYouTubePlayerExitFullScreen() {
//                                showSystemUI()
//                            }
//
//                        })
//                    }
                }
            }
            recyclerView.adapter=adapter
        }
    }

    private fun initData(){
        data.add(VideoData("왕초보 철봉 Step 1","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"))
        data.add(VideoData("왕초보 철봉 Step 2","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"))
        data.add(VideoData("왕초보 철봉 Step 3","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"))
        data.add(VideoData("왕초보 철봉 Step 4","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"))
//        data.add(VideoData("왕초보 철봉 Step 1","SRl21nXi-Qc"))
//        data.add(VideoData("왕초보 철봉 Step 2","ecMvEalbis8"))
//        data.add(VideoData("왕초보 철봉 Step 3","1tIIHPXvz0I"))
//        data.add(VideoData("왕초보 철봉 Step 4","g83sV5D2WJ4"))
    }

    private fun hideSystemUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // show app content in fullscreen, i. e. behind the bars when they are shown (alternative to
            // deprecated View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.setDecorFitsSystemWindows(false)
            // finally, show the system bars
            window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            // Shows the system bars by removing all the flags
            // except for the ones that make the content appear under the system bars.
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

}