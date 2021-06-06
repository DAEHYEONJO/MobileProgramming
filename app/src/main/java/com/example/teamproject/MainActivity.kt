package com.example.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teamproject.databinding.ActivityMainBinding
import com.example.teamproject.databinding.DbexampleBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            val intent = Intent(this, Example::class.java)
            startActivity(intent) /// db 예시 화면으로 갑니다.
        }
    }
}