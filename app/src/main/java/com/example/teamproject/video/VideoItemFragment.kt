package com.example.teamproject.video

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject.databinding.FragmentVideoItemListBinding
import com.example.teamproject.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class VideoItemFragment : Fragment() {
    var binding: FragmentVideoItemListBinding?=null
    lateinit var VideoAdapter: MyVideoItemRecyclerViewAdapter
    var data:ArrayList<VideoData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentVideoItemListBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(-1)
        initSpinner()
        binding?.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            VideoAdapter= MyVideoItemRecyclerViewAdapter(activity as Activity,data)
            VideoAdapter.itemClickListener=object :MyVideoItemRecyclerViewAdapter.OnItemClickListener{
                override fun OnTitleClick(
                    holder: MyVideoItemRecyclerViewAdapter.ViewHolder,
                    view: View,
                    data: VideoData,
                    position: Int
                ) {
                    holder.video.webChromeClient=FullscreenableChromeClient(activity as Activity)
                }

            }
            logoutBtn.setOnClickListener {  //로그하웃 하기
                FirebaseAuth.getInstance().signOut()
                val i = Intent(activity as Activity, LoginActivity::class.java)
                i.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                activity?.finish()
            }

            recyclerView.adapter=VideoAdapter
        }
    }
    private fun initSpinner(){
        val adapter= ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item,
            ArrayList<String>())
        adapter.add("팔굽혀펴기")
        adapter.add("철봉")
        adapter.add("코어")
        adapter.add("하체")
        adapter.add("버티기 운동")
        //adapter.add("")
        binding?.apply {
            spinner.adapter=adapter
            //spinner.setSelection(0)
            spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position){
                        0->{//팔굽
                            initData(0)
                        }
                        1->{//철봉
                            initData(1)
                        }
                        2->{//코어
                            initData(2)
                        }
                        3->{//하체
                            initData(3)
                        }
                        4->{//버티기
                            initData(4)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun initData(type:Int){
        if(type==0){
            VideoAdapter.clearAdapter()
            VideoAdapter.insertData(VideoData(
                "푸쉬업 0개 탈출",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/8oYB62z3sVs\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "푸쉬업 추천 루틴",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/c_5ENJWekbQ\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
        }
        else if(type==1) {
            VideoAdapter.clearAdapter()
            VideoAdapter.insertData(VideoData(
                "왕초보 철봉 Step 1",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "왕초보 철봉 Step 2",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "왕초보 철봉 Step 3",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "왕초보 철봉 Step 4",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"
            ))
        }else if(type==2){
            VideoAdapter.clearAdapter()
            VideoAdapter.insertData(VideoData(
                    "올바른 플랭크",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Zq8nRY9P_cM\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                    "복부 추천 루틴",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jj6ze_eqmYI\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
        }
        else if(type==3){
            VideoAdapter.clearAdapter()
            VideoAdapter.insertData(VideoData(
                    "왕초보 하체운동",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/GASCMxaAHKw\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                    "하체운동 추천 루틴",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/KXYi6bI-UPE\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
        }
        else if(type==4){
            VideoAdapter.clearAdapter()
            VideoAdapter.insertData(VideoData(
                    "5가지 버티기 운동",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/bwYrU734-8c\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}