package com.example.teamproject

import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.teamproject.databinding.ActivityMainBinding
import com.example.teamproject.databinding.DbexampleBinding
import com.google.firebase.firestore.FirebaseFirestore

class Example :AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    lateinit var mydbhelper: Mydbhelper
    lateinit var binding: DbexampleBinding
    lateinit var List : ArrayList<Myroutines>
    lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DbexampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                "date" to "2021-05-29",
                "팔굽혀펴기" to 20,
                "플랭크" to 30
        )


        val routine2 = hashMapOf(
                "date" to "2021-05-30",
                "팔굽혀펴기" to 20,
                "플랭크" to 30
        )

        val routine3 = hashMapOf(
                "date" to "2021-05-29",
                "풀업" to 10
        )

        mydbhelper = Mydbhelper()


        mydbhelper.readlist("1234", "2021-05-29", object : Mydbhelper.MyCallbakclist {
            override fun onCallbacklist(value: ArrayList<Myroutines>) {
                super.onCallbacklist(value)
                List = value
                /// 다음과 같이 리싸이클러뷰와 사용하거 싶다면 list 형태로 반환될 oncallbacklist를 반환하여서
                binding.recyclerView.adapter = Myadapter(List) // 해당 루틴정보를 list에 담아서 리싸이클러뷰에 달아줍니다.
                Log.d("asdf",List.toString())
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        mydbhelper.read("1234", object : Mydbhelper.MyCallback {
            override fun onCallback(value: String) { // mydbhelper의 oncallbakc을 오버라읻 하여서 다음과 같이
                //원하는 형태로 값을 받아오면 됩니다. 현재는 하나의 문자열만 받아오는 형태입니다.
                super.onCallback(value)
                text = value
                binding.name.text = text
                Log.d("asdf", text.toString())
            }

        }) // 다음과 원하는 속성에 대해서 데이터를 읽어올수 있습니다.



        mydbhelper.addprofile(exam) // 회원가입 정보를 넣어줍니다.
        mydbhelper.addroutine("1234", routine) //  루틴정보를 넣어줍니다.

        mydbhelper.updateroutine("1234", routine3) // 5-29일에 같은 날짜에 루틴을 업데이트 해줍니다.

        mydbhelper.addroutine("1234", routine2) // 새로운 날짜에 루틴을 추가해줍니다.

        mydbhelper.deletroutine("1234", "2021-05-29", "풀업") // 해당날짜의 루틴 하나만 삭제합니다.

        //mydbhelper.deleteallroutine("1234","2021-05-30") // 해당 날짜의 루틴 전부 삭제합니다.


    }
}
// firebase가  asynchronous 한 동작을 하여서 직관적으로 데이터를 가져오기 어렵고 다음과 같은 방법으로 인터페이스와
//익명객체를 활용해야만 클래스를 통해서 원하는 데이터를 가져올수 있었습니다. 지금은 문자열과 임의의 데이터클래스를 담은 리스트만
// 만들어놨습니다.
