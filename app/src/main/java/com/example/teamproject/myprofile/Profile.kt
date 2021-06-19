package com.example.teamproject.myprofile

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.teamproject.Mydbhelper
import com.example.teamproject.R
import com.example.teamproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class Profile : Fragment() {
    var saylist : ArrayList<String> = ArrayList()
    var peoplelist:ArrayList<String> = ArrayList()
    lateinit var binding: FragmentProfileBinding
    lateinit var mydbhelper : Mydbhelper
    var count : Int = 0
    var start_date :LocalDate = LocalDate.now()

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
        initData()
        init()
        countdate(count,start_date)

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


    fun countdate(num :Int, date:LocalDate)  {
        binding.progressBar2.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
        CoroutineScope(Dispatchers.IO).async {
            FirebaseFirestore.getInstance().collection("Profile")
                    .document(FirebaseAuth.getInstance().currentUser!!.uid).collection("history").document(date.toString()).get()
                    .addOnSuccessListener {Document ->
                        if(Document.exists()){
                            start_date = minus(date)
                            count += 1
                            countdate(count,start_date)
                            }
                    }
                    }.await()
            binding.routinedays.text = count.toString() + "일"
            binding.progressBar2.visibility =View.GONE
            }
        }

    fun minus(date: LocalDate):LocalDate{
        return date.minusDays(1)
    }


    fun readFileScan(scan: Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            var list = word.split("–")
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