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
            logoutBtn.setOnClickListener {
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
        val adapter= ArrayAdapter<String>(context!!, R.layout.simple_spinner_dropdown_item,
            ArrayList<String>())
        adapter.add("팔굽혀펴기")
        adapter.add("철봉")
        adapter.add("코어")
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
                "팔굽혀펴기 Step 1",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "팔굽혀펴기 Step 2",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "팔굽혀펴기 Step 3",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"
            ))
            VideoAdapter.insertData(VideoData(
                "팔굽혀펴기 Step 4",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"
            ))
//            data.add(
//                VideoData(
//                    "팔굽혀펴기 Step 1",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "팔굽혀펴기 Step 2",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "팔굽혀펴기 Step 3",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "팔굽혀펴기 Step 4",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"
//                )
//            )
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
//            data.add(
//                VideoData(
//                    "왕초보 철봉 Step 1",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "왕초보 철봉 Step 2",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "왕초보 철봉 Step 3",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"
//                )
//            )
//            data.add(
//                VideoData(
//                    "왕초보 철봉 Step 4",
//                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"
//                )
//            )
        }else if(type==2){

        }else{
            data.add(
                VideoData(
                    "팔굽혀펴기 Step 1",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SRl21nXi-Qc\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
                )
            )
            data.add(
                VideoData(
                    "팔굽혀펴기 Step 2",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ecMvEalbis8\" frameborder=\"0\" allowfullscreen autobuffer></iframe>"
                )
            )
            data.add(
                VideoData(
                    "팔굽혀펴기 Step 3",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1tIIHPXvz0I\" frameborder=\"0\" allowfullscreen></iframe>"
                )
            )
            data.add(
                VideoData(
                    "팔굽혀펴기 Step 4",
                    "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g83sV5D2WJ4\" frameborder=\"0\" allowfullscreen></iframe>"
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}