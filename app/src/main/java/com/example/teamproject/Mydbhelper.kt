package com.example.moblieprogramming6

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Mydbhelper{

    var db = FirebaseFirestore.getInstance()
    fun addprofile(profile : HashMap<String,Any>){ // 회원 가입때 회원정보를 firestore에 저장하는 함수입니다.
        db.collection("Profile").document(profile.get("id").toString()) //document의 이름은 id의 값이 될것입니다.
                .set(profile) // haspmap 데이터들을 id이름을 가진 document에 저장합니다.

    }

    fun addroutine(id: String, data: HashMap<String,Any>){ //입력형식은 추후 바꿀수 있습니다. 현재 방식은 날짜가 포함된 hasp map 형태입니다.
        db.collection("Profile").document("1234") // 아이디를 이름으로 한 document입니다.
                .collection("history").document(data.get("date").toString())
                // history 즉 운동 기록 콜렉션으로 접근하고 그곳에 날짜를 문서이름으로 한 document를 생성해 루틴정보를 저장합니다.
                .set(data)
    }

    fun updateroutine(id:String, data: HashMap<String,Any>){ //
        db.collection("Profile").document("1234") // 아이디를 이름으로 한 document입니다.
                .collection("history").document(data.get("date").toString())
                .update(data)
    }


    fun deleteallroutine(id:String, date: String){ // 해당 날짜 루틴 전체 삭제
        db.collection("Profile").document("1234")
                .collection("history").document(date).delete()
    }

    fun deletroutine(id:String, date:String, routine:String){ // 루틴 하나만 삭제
    val docref=db.collection("Profile").document("1234")
                .collection("history").document(date)
                val updates= hashMapOf<String,Any>(routine to FieldValue.delete())
        docref.update(updates)
    }






}