package com.example.teamproject.login

data class signupInfo (val email:String, val pw:String,val nickname: String,val gender:String){
    constructor(): this("no email","no pw","no name","no gender")
}