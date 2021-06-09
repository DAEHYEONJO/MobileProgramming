package com.example.teamproject

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.teamproject.databinding.ActivityMainBinding
import com.example.teamproject.databinding.DbexampleBinding
import com.example.teamproject.stopwatch.StopWatchFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val REQUEST_VOICE = 100

    val stopWatchFragment by lazy { StopWatchFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()//stt 기능을 위해 RECORD_AUDIO 권한 요청

        bottomNaviInit()

        binding.button.setOnClickListener {
            val intent = Intent(this, Example::class.java)
            startActivity(intent) /// db 예시 화면으로 갑니다.
        }
    }

    private fun bottomNaviInit() {//각자 기능 프래그먼트 만들어서 리플레이스 프레그먼트 함수 호출하심 되여
        //메뉴 이름이나 메뉴 아이디, 아이콘 같은건 bottom_navigation_menu.xml에서 바꾸시면 되요
        binding.bottomNavi.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottom_one->{ return@setOnNavigationItemSelectedListener true }
                R.id.bottom_two->{return@setOnNavigationItemSelectedListener true }
                R.id.bottom_three->{return@setOnNavigationItemSelectedListener true }
                R.id.bottom_four->{return@setOnNavigationItemSelectedListener true }
                R.id.bottom_stop_watch->{
                    replaceFragment(stopWatchFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {return@setOnNavigationItemSelectedListener true }
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.main_root_layout,fragment).commit()
    }

    private fun requestPermission() {
        if(ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.RECORD_AUDIO),REQUEST_VOICE)
        }else{
            Toast.makeText(this@MainActivity, "RECORD_AUDIO 권한 수락 완료", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_VOICE->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this@MainActivity, "RECORD_AUDIO 권한 수락 완료", Toast.LENGTH_SHORT).show()
                }else{
                    requestPermission()
                }
            }
        }
    }
}