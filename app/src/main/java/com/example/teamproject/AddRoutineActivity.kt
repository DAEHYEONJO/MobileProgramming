package com.example.teamproject

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AddRoutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)
        init()
    }
    fun max(a:Int, b:Int):Int {
        var ret = if(a>b) {
            a
        }else{
            b
        }
        return ret
    }
    private fun init(){
        var editText = findViewById<EditText>(R.id.repeat)
        editText.setText("10")
        var plus_btn = findViewById<ImageButton>(R.id.plus)
        var minus_btn = findViewById<ImageButton>(R.id.minus)
        plus_btn.setOnClickListener {
            val new_repeat = editText.text.toString().toInt()+1
            editText.setText(new_repeat.toString())
        }
        minus_btn.setOnClickListener {
            val new_repeat = max(editText.text.toString().toInt()-1, 0)
            editText.setText(new_repeat.toString())
        }
    }
}