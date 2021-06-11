package com.example.teamproject.myprofile

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.teamproject.Mydbhelper
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class Profile : Fragment() {
    var saylist : ArrayList<String> = ArrayList()
    var peoplelist:ArrayList<String> = ArrayList()
    lateinit var binding: FragmentProfileBinding
    lateinit var mydbhelper : Mydbhelper
    lateinit var start_date: LocalDate
    var count : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mydbhelper = Mydbhelper()
        start_date = LocalDate.now()
        initData()
        init()
        countdate()



    }

    fun init(){
        binding.apply {
            mydbhelper.readname(
                FirebaseAuth.getInstance().currentUser!!.uid,
                object : Mydbhelper.MyCallback {

                    override fun onCallback(value: String) {
                        super.onCallback(value)
                        nameText.text = value + "님 어서오세요!"
                    }
                })

            mydbhelper.readgender(
                FirebaseAuth.getInstance().currentUser!!.uid,
                object : Mydbhelper.MyCallback {
                    override fun onCallback(value: String) {
                        super.onCallback(value)
                        gender.text = value
                    }
                })
            var num = Random().nextInt(saylist.size)
            sayingtext.text = saylist[num]
            whosaying.text = peoplelist[num]
        }

    }


    fun countdate()  { // 몇일 연속으로 루틴 달성 했는가 보여주는 구문입니다.
        val account = Account(count.toString(), start_date.toString())
        CoroutineScope(Dispatchers.IO).launch {
        FirebaseFirestore.getInstance().collection("Profile")
            .document(FirebaseAuth.getInstance().currentUser!!.uid).collection("history").get()
            .addOnSuccessListener {
                /*
                    for (document in it){
                        Log.d("db", document["date"].toString())
                        Log.d("hi",start_date.toString())
                        count += 1
                        start_date.minusDays(1)
                        if(document["date"].toString() == start_date.toString()) {
                            count += 1
                            Log.d("db", document["date"].toString())
                        }
                    }*/
                    binding.routinedays.text = it.size().toString() + " 번"
                    binding.routinedays.setTextSize(45.0F)
            }
        }
    }

    fun readFileScan(scan: Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            var list = word.split("–")
            Log.d("asdf",list.toString())
            saylist.add(list[0])
            peoplelist.add(list[1])
        }
        scan.close()
    }

    private fun initData() {

        val scan = Scanner(resources.openRawResource(R.raw.sayings))
        readFileScan(scan)
    }



}