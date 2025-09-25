package com.example.ch8_event

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch8_event.databinding.ActivityMainBinding
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    // 사용자가 Stop을 눌렀을 때 지금까지 흐른 시각을 저장
    // 중단 후 다시 시작할 때 저장됬던 값에서부터 시간이 흐르도록 한다
    // 시간을 보정하기 위한 음수값이 저장된다
    var pauseTime = 0L //0L은 Long 타입이다

    // 사용자가 뒤로 가기 버튼을 언제 눌렀는지를 저장
    // '2초 안에 한 번 더 누르면 프로그램 종료'와 같은 기능을 위해 사용한다
    var initTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // XML 레이아웃 파일과 Kotlin 코드를 연결하기 위한 뷰 바인딩
        // 중간에 값이 바뀔 일이 존재하지 않으므로 val 타입으로 설정
        val binding = ActivityMainBinding.inflate(layoutInflater)   // ActivityMainBinding 클래스에서 binding이라는 변수 명을 가진 객체 생성
        setContentView(binding.root)    // 해당 객체의 root를 전체 화면에 출력

        // start 버튼을 눌렀을 때의 리스너
        binding.startButton.setOnClickListener {
            // chronometer.base는 시간 계산을 위한 기준점을 의미한다
            // SystemClock.elapsedRealtime()는 현재 시간을 가져온다
            // 즉, chronometer.base = SystemClock.elapsedRealtime()라는 코드는
            // 시간 계산을 위한 기준점에 현재의 시간을 담는 것을 뜻한다
            // 현재의 시간을 0으로 삼아 시간 측정을 시작하라는 것
            // 만약 이전에 눌렀던 시간(pauseTime)이 존재한다면 해당 시간을 감안하고 출력할 것
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime

            // 타이머가 시간을 세기 시작함
            binding.chronometer.start()

            // 한 번 start 버튼을 누르면 start는 비활성화, stop/reset은 활성화 되어야 한다
            binding.startButton.isEnabled = false
            binding.stopButton.isEnabled = true
            binding.resetButton.isEnabled = true
        }

        binding.stopButton.setOnClickListener {
            // start 버튼을 누른 시간에서 stop을 누른 현재 시간을 빼서 흘러간 시간을 음수로 저장
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime()
            binding.chronometer.stop()

            binding.startButton.isEnabled = true
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = true
        }

        binding.resetButton.setOnClickListener {
            pauseTime = 0L  // 흘러간 시간 초기화
            binding.chronometer.base = SystemClock.elapsedRealtime() // 화면에 표시되는 시간을 00:00으로 되돌리기 위해 베이스 시간을 초기화

            binding.startButton.isEnabled = true
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = false
        }
    }

    // 뒤로 가기의 기본 동작은 현재 화면(Activity)을 종료하는 것
    // 부모 클래스의 함수를 오버라이드 해야 한다
    // 이전 기기들은 뒤로 가기 기능을 물리 버튼으로 처리했음으로 뒤로 가기 물리버튼이 사라진 지금까지도 키 이벤트로 처리한다
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "종료를 원하면 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                initTime = System.currentTimeMillis()
                return true // 이벤트 종료
            }
        }

        return super.onKeyDown(keyCode, event) // 특별히 처리하지 않은 나머지 경우는 모두 원래 시스템의 기본 동작을 따르게 하는 코드
        // 이 경우 원래의 이벤트 대로 뒤로가기가 실행된다
    }

}