package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText




class AddRoutineActivity : AppCompatActivity() {
    lateinit var mydbhelper: Mydbhelper
    lateinit var selected_date:String
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
        selected_date = intent.getStringExtra("date").toString()

        mydbhelper = Mydbhelper()
        val routine_count = findViewById<EditText>(R.id.routine_count)
        val plus_btn = findViewById<ImageButton>(R.id.plus)
        val minus_btn = findViewById<ImageButton>(R.id.minus)
        val applyBtn = findViewById<Button>(R.id.apply_btn)
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)

        val intent = Intent(this, CalendarActivity::class.java)

        routine_count.setText("10")
        plus_btn.setOnClickListener {
            val new_repeat = routine_count.text.toString().toInt()+1
            routine_count.setText(new_repeat.toString())
        }
        minus_btn.setOnClickListener {
            val new_repeat = max(routine_count.text.toString().toInt()-1, 0)
            routine_count.setText(new_repeat.toString())
        }
        applyBtn.setOnClickListener {
            val routine_name = findViewById<TextInputEditText>(R.id.routine_name)

            val data = hashMapOf(
                routine_name.text.toString() to routine_count.text.toString().toInt(),
                "date" to selected_date
            )
            mydbhelper.addroutine("temp", data)

            startActivity(intent)
        }
        cancelBtn.setOnClickListener {
            startActivity(intent)
        }
    }
}