package com.example.teamproject.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.teamproject.MainActivity
import com.example.teamproject.Mydbhelper
import com.example.teamproject.alarm.AlarmService
import com.example.teamproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    lateinit var mydbhelper: Mydbhelper // db에도 정보를 넣기 위해서 선언해주었습니다.
    lateinit var binding: ActivityLoginBinding
    lateinit var auth:FirebaseAuth
    lateinit var signupInfo: signupInfo
    private var gender=""
    private var checkEmail=0
    private var checkEmailLogin=0
    lateinit var alarmService: AlarmService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initEditText()
        initRegister()

    }

    private fun init(){
        auth= FirebaseAuth.getInstance()
        binding.apply {


            if(auth.currentUser!=null){ //로그인 된 상태면 바로 메인으로 이동
                val intent=Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            LoginBtn.setOnClickListener {//로그인 버튼
                val intent= Intent(this@LoginActivity,
                    MainActivity::class.java)
                //startActivity(intent)
                //finish()
                login()
            }

            signupBtn.setOnClickListener { //회원가입 하기
                loginLayout.visibility=View.GONE
                signupPopup.visibility=View.VISIBLE
            }
            cancleBtn.setOnClickListener { //회원가입 취소
                loginLayout.visibility=View.VISIBLE
                signupPopup.visibility=View.GONE
                initSignup()
            }
            okBtn.setOnClickListener{ // 회원가입 신청
                register()
            }
        }
    }
    private fun login(){
        binding.apply {
            var email=emailText.text.toString()
            var pw=pwText.text.toString()
            if(checkEmailLogin==0){
                return
            }
            if(pw.isEmpty()){
                loginEmailInputLayout.setError("비밀번호가 필요합니다.")
                loginEmailInputLayout.requestFocus()
                return
            }else{
                loginEmailInputLayout.error=null
            }

            progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(email,pw).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent= Intent(this@LoginActivity,
                            MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity,"로그인 실패",Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility=View.GONE
            }
        }

    }

    private fun initEditText(){
        binding.apply {
            emailText.addTextChangedListener {
                if(it.toString().contains('@')&&it.toString().contains('.')){
                    loginEmailInputLayout.error=null
                    checkEmailLogin=1
                }else{
                    loginEmailInputLayout.error="이메일 형식이 올바르지 않습니다."
                    checkEmailLogin=0
                }
            }

        }
    }

    private fun initRegister(){
        binding.apply {
            checkMale.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    gender="남"
                }else{
                    gender=""
                }
                checkFemale.isChecked=false
            }
            checkFemale.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    gender="여"
                }else{
                    gender=""
                }
                checkMale.isChecked=false
            }
            emailSignupText.addTextChangedListener {
                if(it.toString().contains('@')&&it.toString().contains('.')){
                    signupInputLayout.error=null
                    checkEmail=1
                }else{
                    signupInputLayout.error="이메일 형식이 올바르지 않습니다."
                    checkEmail=0
                }
            }
        }

    }

    private fun register(){
        binding.apply {
            var email=emailSignupText.text.toString().trim()
            var pw=pwSignupText.text.toString()
            var nickname=nameSignupText.text.toString().trim()

            if(checkEmail==0){
                return
            }
            if(email.isEmpty()){
                signupInputLayout.setError("이메일이 필요합니다.")
                signupInputLayout.requestFocus()
                return
            }
            if(pw.isEmpty()){
                signupInputLayout2.setError("비밀번호가 필요합니다.")
                signupInputLayout2.requestFocus()
                return
            }else{
                signupInputLayout2.error=null
            }
            if(pw.length<6){
                signupInputLayout2.setError("비밀번호는 6자 이상이어야 합니다.")
                signupInputLayout2.requestFocus()
                return
            }else{
                signupInputLayout2.error=null
            }
            if(nickname.length<2){
                signupInputLayout3.setError("닉네임은 2자 이상이어야 합니다.")
                signupInputLayout3.requestFocus()
                return
            }else{
                signupInputLayout3.error=null
            }
            if(nickname.length>10){
                signupInputLayout3.setError("닉네임은 10자 이하여야 합니다.")
                signupInputLayout3.requestFocus()
                return
            }else{
                signupInputLayout3.error=null
            }
            if(gender==""){
                checkFemale.setError("성별을 선택하세요.")
                checkFemale.requestFocus()
                return
            }else{
                checkFemale.error=null
            }
            progressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(email,pw)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            signupInfo= signupInfo(email,pw,nickname,gender)
                            try{
                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .setValue(signupInfo).addOnCompleteListener {
                                            if(it.isSuccessful){
                                                Toast.makeText(this@LoginActivity,"등록 성공",Toast.LENGTH_SHORT).show()

                                                // 회원가입 성공후 db에 따로 넣어주는 작업입니다.
                                                mydbhelper = Mydbhelper()
                                                val exam = hashMapOf(
                                                        "name" to signupInfo.nickname,
                                                        "id" to FirebaseAuth.getInstance().currentUser!!.uid,
                                                        "gender" to signupInfo.gender
                                                )
                                                mydbhelper.addprofile(exam)
                                                // 파이어스토어상에서는 정상적으로 연동되는거 확인했습니다.

                                                initSignup()
                                            }
                                        }
                            }catch (e:Exception){
                                Toast.makeText(this@LoginActivity,"등록 실패",Toast.LENGTH_SHORT).show()
                            }

                        }else{
                            Toast.makeText(this@LoginActivity,"이미 등록된 계정입니다.",Toast.LENGTH_SHORT).show()

                        }
                    }
            progressBar.visibility=View.GONE
        }
    }
    private fun initSignup(){
        binding.apply {
            loginLayout.visibility=View.VISIBLE
            emailSignupText.text?.clear()
            pwSignupText.text?.clear()
            nameSignupText.text?.clear()
            checkMale.isChecked=false
            checkFemale.isChecked=false
            signupInputLayout.error=null
            signupInputLayout2.error=null
            signupInputLayout3.error=null
            checkFemale.error=null
            signupPopup.visibility=View.GONE
        }
    }

}