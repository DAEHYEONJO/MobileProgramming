package com.example.teamproject.stopwatch

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.teamproject.R
import com.example.teamproject.databinding.ExeNameRecyclerViewBinding
import com.example.teamproject.databinding.FragmentRecordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecordFragment : Fragment() {

    var binding : FragmentRecordBinding? = null
    var exeNameAapter : ExeNameAdapter? = null
    val stopWatchViewModel : StopWatchViewModel by activityViewModels<StopWatchViewModel>()
    val curUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val db = FirebaseFirestore.getInstance()
    val exeRecordDb = db.collection("Profile").document(curUserId).collection("ExeRecord")
    lateinit var recordAdapter: RecordAdapter
    private var progressDialog : AppCompatDialog? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNameDialog()
    }

    private fun initNameDialog() {
        stopWatchViewModel.recordNotifyText.observe(viewLifecycleOwner, Observer {

            if (stopWatchViewModel.recordCurName.value!=null)
                binding!!.exeName.text = stopWatchViewModel.recordCurName.value
            if (it == "기록 있음"){
                Log.d("record","기록 있음 : "+stopWatchViewModel.recordNotifyText.value.toString())
                binding!!.recordRecyclerView.visibility = View.VISIBLE
                binding!!.notifyText.visibility = View.GONE
                //initRecordRecyclerView(binding!!.exeName.text as String)

            }else{
                Log.d("record","기록 없음 : "+stopWatchViewModel.recordNotifyText.value.toString())
                binding!!.recordRecyclerView.visibility = View.GONE
                binding!!.notifyText.visibility = View.VISIBLE
                binding!!.notifyText.text = stopWatchViewModel.recordNotifyText.value.toString()
            }
        })



        binding!!.exeName.setOnClickListener {
            val binding = ExeNameRecyclerViewBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(binding.root)
                    .setCancelable(false)
            val dialog = builder.show()

            val exeList = stopWatchViewModel.exeNameList.value
            binding.recyclerView.apply {
                exeNameAapter = ExeNameAdapter(exeList!!)
                exeNameAapter?.listener = object : ExeNameAdapter.OnItemClickListener{
                    override fun onClick(name: String) {
                        this@RecordFragment.binding!!.exeName.text = name
                        stopWatchViewModel.recordCurName.value = name
                        dialog.dismiss()
                        initRecordRecyclerView(name)
                    }
                }
                adapter = exeNameAapter
                layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                addItemDecoration(DividerItemDecoration(requireContext(),1))
            }

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


    }

    private fun initRecordRecyclerView(s: String) {
        Log.d("record","initRecordRecyclerView")
        val recordList = ArrayList<Record>()
        var flag = false

        exeRecordDb.document(s).get(). addOnSuccessListener {
            if (it.data?.keys!=null){
                stopWatchViewModel.recordNotifyText.value = "기록 있음"
                for (key in it.data?.keys!!.iterator()){
                    //Log.d("record","key : $key value : ${it[key]}")
                    val str = it[key].toString().split(":")
                    var result = 0
                    result +=str[0].toInt()*3600
                    result+=str[1].toInt()*60
                    result+=str[2].toInt()

                    recordList.add(Record(key, result))
                }
            }else{
                //binding!!.notifyText.visibility = View.VISIBLE
                //binding!!.notifyText.text = "아직 기록이 없습니다 !"
                stopWatchViewModel.recordNotifyText.value = "아직 기록이 없습니다 !"
                flag = true
                progressOff()
                return@addOnSuccessListener
            }
        }.addOnCompleteListener {
            if (flag) {
                progressOff()
                return@addOnCompleteListener
            }
            recordList.sortBy {
                it.time
                //Log.d("record","sorting : ${it.date} ${it.time}")
            }
            var sum = 0
            var avg = 0
            recordList.forEach {
                //Log.d("record","result : ${it.date} ${it.time}")
                sum+=it.time
                avg = sum/(recordList.size)
            }
            recordList[recordList.size-1].avg = avg
            recordAdapter = RecordAdapter(recordList)
            binding!!.recordRecyclerView.apply {
                adapter = recordAdapter
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            }
            progressOff()
        }

    }

    private fun progressOn(){
        if (exeNameAapter!=null){
            progressDialog = AppCompatDialog(requireContext())
            progressDialog?.setCancelable(false)
            progressDialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))//베경색 투명하게
            progressDialog?.setContentView(R.layout.loding_dialog_layout)//다이어로그 커스텀 레이아웃
            progressDialog?.show()//다이어로그 띄우기
            val img = progressDialog?.findViewById<ImageView>(R.id.loading_img)//다이어로그에 보여질 이미지
            val frameAnimation = img?.background as AnimationDrawable//애니메이션 객체 생성
            scope.launch {
                frameAnimation.start()//애니메이션 쓰레드로 실행해주기
            }
        }
    }

    private fun progressOff(){
        if(progressDialog!=null){
            Log.d("tq","progressOff")
            if (progressDialog!!.isShowing)
                progressDialog!!.dismiss()
        }
    }

    override fun onResume() {
        Log.d("record","onResume")
        super.onResume()
        if (binding!!.exeName.text!="운동이름"){
            initRecordRecyclerView(binding!!.exeName.text.toString())
        }
    }

    override fun onPause() {
        Log.d("record","onPause")
        super.onPause()
        Log.d("record","pause: "+stopWatchViewModel.recordNotifyText.value.toString())
        Log.d("record","pause: "+binding?.notifyText?.text.toString())
        stopWatchViewModel.recordCurName.value = binding?.exeName?.text.toString()
    }

    override fun onStart() {
        super.onStart()
        Log.d("record","onStart")
        //progressOn()
    }


    override fun onDestroy() {
        Log.d("record","onDestroy")
        super.onDestroy()
        binding = null
    }

}