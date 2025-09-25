package com.example.ch8_event

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch8_event.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 뒤로 가기 버튼을 누른 시각을 저장
    val initTime = 0L

    // 멈춘 시각을 저장
    val pauseTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // start 버튼을 눌렀을 때의 리스너
        binding.startButton.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            binding.chronometer.start()

            // 한 번 start 버튼을 누르면 start는 비활성화, stop/reset은 활성화 되어야 한다
            binding.startButton.isEnabled = false
            binding.stopButton.isEnabled = true
            binding.resetButton.isEnabled = true
        }
    }
}