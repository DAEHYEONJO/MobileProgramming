package com.example.teamproject

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Mydbhelper{

    var db = FirebaseFirestore.getInstance()
    fun addprofile(profile : HashMap<String,String>){ // 회원 가입때 회원정보를 firestore에 저장하는 함수입니다.
        db.collection("Profile").document(profile.get("id").toString()) //document의 이름은 id의 값이 될것입니다.
                .set(profile) // haspmap 데이터들을 id이름을 가진 document에 저장합니다.

    }

    fun addroutine(id: String, data: HashMap<String,Any>){ //입력형식은 추후 바꿀수 있습니다. 현재 방식은 날짜가 포함된 hasp map 형태입니다.
        db.collection("Profile").document(id) // 아이디를 이름으로 한 document입니다.
                .collection("history").document(data.get("date").toString())
                // history 즉 운동 기록 콜렉션으로 접근하고 그곳에 날짜를 문서이름으로 한 document를 생성해 루틴정보를 저장합니다.
                .set(data)
    }

    fun updateroutine(id:String, data: HashMap<String,Any>){ //
        db.collection("Profile").document(id) // 아이디를 이름으로 한 document입니다.
                .collection("history").document(data.get("date").toString())
                .update(data)
    }


    fun deleteallroutine(id:String, date: String){ // 해당 날짜 루틴 전체 삭제
        db.collection("Profile").document(id)
                .collection("history").document(date).delete()
    }

    fun deletroutine(id:String, date:String, routine:String){ // 루틴 하나만 삭제
        val docref=db.collection("Profile").document(id)
                .collection("history").document(date)
        val updates= hashMapOf<String,Any>(routine to FieldValue.delete())
        docref.update(updates)
    }


    interface MyCallback {
        fun onCallback(value: String){
        }
    }

    interface MyCallbakclist {
        fun onCallbacklist(value : ArrayList<Myroutines>){
        }
    }


    fun read(id: String, myCallback: MyCallback) { // 단순하게 하나의 요소만 읽을 경우 입니다.
        db.collection("Profile").document(id) /// document 아이디는 회원가입에서 생성된 아이디가 될것입니다.
                .get()
                .addOnSuccessListener {
                    if(it != null){
                        myCallback.onCallback(it.get("name").toString())
                    }

                }
                .addOnFailureListener { exception ->
                }

    }

    fun readname(id: String, myCallback: MyCallback) { // 단순하게 하나의 요소만 읽을 경우 입니다.
        db.collection("Profile").document(id) /// document 아이디는 회원가입에서 생성된 아이디가 될것입니다.
            .get()
            .addOnSuccessListener {
                if(it != null){
                    myCallback.onCallback(it.get("name").toString())
                }

            }
            .addOnFailureListener { exception ->
            }
    }

    fun readgender(id: String, myCallback: MyCallback) { // 단순하게 하나의 요소만 읽을 경우 입니다.
        db.collection("Profile").document(id) /// document 아이디는 회원가입에서 생성된 아이디가 될것입니다.
            .get()
            .addOnSuccessListener {
                if(it != null){
                    myCallback.onCallback(it.get("gender").toString())
                }

            }
            .addOnFailureListener { exception ->
            }
    }




    fun getRoutineList(id:String, date :String, myCallbakclist: MyCallbakclist) { // 리싸이클러뷰와 연동할 경우 입니다. 해당 날짜에 대한 루틴정보를 읽어옵니다.
        val docref = db.collection("Profile").document(id)
            .collection("history").whereEqualTo("date", date)
        docref.get()
            .addOnSuccessListener {documents->
                if(documents != null) {
                    val list = ArrayList<Myroutines>()
                    for(document in documents){
                        val map = document.data
                        val keys = map?.keys?.iterator()
                        while (keys!!.hasNext()) {
                            val key = keys?.next()
                            list.add(Myroutines(key.toString(), map.get(key).toString()))
                        }
                    }
                    myCallbakclist.onCallbacklist(list)
                }

            }
            .addOnFailureListener {  }
    }

    fun readlist(id:String, date :String , myCallbakclist: MyCallbakclist) { // 리싸이클러뷰와 연동할 경우 입니다. 해당 날짜에 대한 루틴정보를 읽어옵니다.

        val docref = db.collection("Profile").document(id)
                .collection("history").document(date)
        docref.get()
                .addOnSuccessListener {document->
                    if(document != null) {
                        val list = ArrayList<Myroutines>()
                        val map = document.data
                        val keys = map?.keys?.iterator()
                        while (keys!!.hasNext()) {
                            val key = keys?.next()
                            list.add(Myroutines(key.toString(), map.get(key).toString()))
                        }
                        myCallbakclist.onCallbacklist(list)
                    }

                }
                .addOnFailureListener {  }
    }
}





