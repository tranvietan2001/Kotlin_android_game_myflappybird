package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PlayGameActivity : AppCompatActivity() {
    private lateinit var namePlayer: TextView
//    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.activity_main)

//        gameManager = findViewById(R.id.gameManager)

        val nameAccount = intent.getStringExtra("NAME_ACCOUNT")
        namePlayer = findViewById(R.id.nameAccountPlayTxt)
        if(nameAccount != "@Off_play"){
            namePlayer.text = nameAccount
        }
        else namePlayer.text = ""

    }

//    private fun hideSystemUI() {
//        // Thiết lập chế độ toàn màn hình
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            hideSystemUI() // Đảm bảo chế độ toàn màn hình khi có tiêu điểm
//        }
//    }
}