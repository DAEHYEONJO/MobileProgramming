package com.example.teamproject.login

data class signupInfo (val email:String,val nickname: String,val gender:String){
    constructor(): this("no email","no name","no gender")
}