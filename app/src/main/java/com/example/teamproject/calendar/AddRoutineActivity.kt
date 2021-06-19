package com.example.teamproject.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.MainActivity
import com.example.teamproject.Mydbhelper
import com.example.teamproject.R
import com.example.teamproject.alarm.AlarmService
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class AddRoutineActivity : AppCompatActivity() {
    lateinit var mydbhelper: Mydbhelper
    lateinit var selected_date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)

        init()
    }

    private fun init() {
        selected_date = intent.getStringExtra("date").toString()
        val alarm=AlarmService(this@AddRoutineActivity)

        mydbhelper = Mydbhelper()
        val routine_count = findViewById<EditText>(R.id.routine_count)
        val plus_btn = findViewById<ImageButton>(R.id.plus)
        val minus_btn = findViewById<ImageButton>(R.id.minus)
        val applyBtn = findViewById<Button>(R.id.apply_btn)
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        routine_count.setText("10")

        plus_btn.setOnClickListener {
            val new_repeat = routine_count.text.toString().toInt() + 1
            routine_count.setText(new_repeat.toString())
        }
        minus_btn.setOnClickListener {
            val new_repeat = max(routine_count.text.toString().toInt() - 1, 0)
            routine_count.setText(new_repeat.toString())
        }
        applyBtn.setOnClickListener {
            var user_id = FirebaseAuth.getInstance().currentUser!!.uid
            val routine_name = findViewById<TextInputEditText>(R.id.routine_name).text.toString()
            val routine_count = routine_count.text.toString().toInt()
            mydbhelper.existRoutineList(
                user_id,
                selected_date,
                object : Mydbhelper.MyCallBackExist {
                    override fun onCallBackExist(value: Boolean) {
                        super.onCallBackExist(value)
                        val data = hashMapOf(
                            routine_name to routine_count,
                            "date" to selected_date
                        )
                        if (value) {
                            mydbhelper.existRoutine(user_id,
                                selected_date, routine_name,
                                object : Mydbhelper.MyCallBackExist {
                                    override fun onCallBackExist(value: Boolean) {
                                        super.onCallBackExist(value)
                                        if (value) {
                                            makeToast(R.string.add_routine_duplicate.toString())
                                        } else {
                                            mydbhelper.updateroutine(user_id, data)
                                            makeToast(R.string.add_routine_success.toString())
                                            alarm.setAlarm(
                                                selected_date.substring(0, 4).toInt(),
                                                selected_date.substring(5, 7).toInt(),
                                                selected_date.substring(8, 10).toInt()
                                            )
                                            startActivity(intent)
                                        }
                                    }
                                })
                        } else {
                            mydbhelper.addroutine(user_id, data)
                            alarm.setAlarm(
                                selected_date.substring(0, 4).toInt(),
                                selected_date.substring(5, 7).toInt(),
                                selected_date.substring(8, 10).toInt()
                            )
                            makeToast(R.string.add_routine_success.toString())
                            startActivity(intent)
                        }
                    }
                })
        }
        cancelBtn.setOnClickListener {
            startActivity(intent)
        }
    }

    private fun max(a: Int, b: Int): Int {
        return if (a > b) a
        else b
    }

    private fun makeToast(message:String){
        Toast.makeText(
            this@AddRoutineActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}