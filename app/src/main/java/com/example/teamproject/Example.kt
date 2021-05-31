package com.example.moblieprogramming6

import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moblieprogramming6.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class Example :AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    lateinit var mydbhelper: Mydbhelper
    lateinit var binding: ActivityMainBinding
    var List = ArrayList<Myroutines>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readlist("1234", "2021-05-29")
        init()
    }

    fun init() {

        val exam = hashMapOf(
                "name" to "김준우",
                "id" to 1234, // 해당 아이디는 회원가입을 통해서 받아오는 아이디 입니다. 예시를 보여야 하므로 임의로 정해주었습니다.
                "state" to "df",
                "country" to "asdf"
        )

        val routine = hashMapOf( // 다음과 같이 루틴 형식을 해쉬맵으로 생성합니다.
                "date" to  "2021-05-29",
                "팔굽혀펴기" to 20,
                "플랭크" to 30
        )


        val routine2 = hashMapOf(
                "date" to  "2021-05-30",
                "팔굽혀펴기" to 20,
                "플랭크" to 30
        )

        val routine3 = hashMapOf(
                "date" to  "2021-05-29",
                "풀업" to 10
         )


        read("1234") // 다음과 원하는 속성에 대해서 데이터를 읽어올수 있습니다.
            //데이터를 value값으로 반환해오는 것은 불가능하여 다음과 같이 mydbhelpler 클래스가 아닌
            // 액티비티 혹은 프래그먼트가 동작하는 클래스 에서 아래 함수를 선언한 후 textview등 위젯의 속성을 바꾸어야 합니다
            //이는 다른분들 코드를 보고 각각 프래그먼트 속성에 맞게 데이터를 받아 올 수 있도록 제가 작성 하도록 하겠습니다.


        mydbhelper = Mydbhelper()
        mydbhelper.addprofile(exam) // 회원가입 정보를 넣어줍니다.
        mydbhelper.addroutine("1234",routine) //  루틴정보를 넣어줍니다.

        mydbhelper.updateroutine("1234",routine3) // 5-29일에 같은 날짜에 루틴을 업데이트 해줍니다.

        mydbhelper.addroutine("1234",routine2) // 새로운 날짜에 루틴을 추가해줍니다.

        mydbhelper.deletroutine("1234","2021-05-29","풀업") // 해당날짜의 루틴 하나만 삭제합니다.

        //mydbhelper.deleteallroutine("1234","2021-05-30") // 해당 날짜의 루틴 전부 삭제합니다.


    }




    fun read(id: String) { // 단순하게 하나의 요소만 읽을 경우 입니다.
        db.collection("Profile").document(id) /// document 아이디는 회원가입에서 생성된 아이디가 될것입니다.
                .get()
                .addOnSuccessListener {
                    if(it != null){
                        binding.name.setText(it.get("name").toString()) // 해당 아이디에서 애트리뷰트 네임을 가져옵니다.
                    }
                }
                .addOnFailureListener { exception ->
                }
    }


    fun readlist(id:String, date :String) { // 리싸이클러뷰와 연동할 경우 입니다. 해당 날짜에 대한 루틴정보를 읽어옵니다.

            val docref = db.collection("Profile").document(id)
                .collection("history").document(date)
        docref.get()
                .addOnSuccessListener {document->
                    if(document != null) {
                        val map = document.data
                        val keys = map?.keys?.iterator()
                        while (keys!!.hasNext()) {
                            val key = keys?.next()
                            List.add(Myroutines(key.toString(), map.get(key).toString()))
                        }
                    }
                    else{
                    }
                    binding.recyclerView.adapter = Myadapter(List) // 해당 루틴정보를 list에 담아서 리싸이클러뷰에 달아줍니다.
                    binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                }
        }
    }
