package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sOnlineBtn: Button
    private lateinit var sOfflineBtn: Button
    private lateinit var sShopBtn: Button
    private lateinit var sConfigBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.select_game_mode_activity_main)

        sOnlineBtn = findViewById(R.id.sOnlineBtn)
        sOfflineBtn = findViewById(R.id.sOfflineBtn)
        sShopBtn = findViewById(R.id.sShopBtn)
        sConfigBtn = findViewById(R.id.sConfigBtn)

        sOnlineBtn.setOnClickListener {
            Toast.makeText(this,"online", Toast.LENGTH_SHORT).show()
            val changeUi = Intent(this,LoginActivity::class.java)
            startActivity(changeUi)
        }

        sOfflineBtn.setOnClickListener {
            Toast.makeText(this,"offline", Toast.LENGTH_SHORT).show()

            val changeUi = Intent(this,PlayGameActivity::class.java)
            startActivity(changeUi)
//            finish()
        }

        sShopBtn.setOnClickListener {
            Toast.makeText(this,"shop", Toast.LENGTH_SHORT).show()
            val changeUi = Intent(this,ShopActivity::class.java)
            startActivity(changeUi)
        }

        sConfigBtn.setOnClickListener {
            Toast.makeText(this,"config", Toast.LENGTH_SHORT).show()
            val changeUi = Intent(this,ConfigActivity::class.java)
            startActivity(changeUi)
        }
    }


    private fun hideSystemUI() {
        // Thiết lập chế độ toàn màn hình
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI() // Đảm bảo chế độ toàn màn hình khi có tiêu điểm
        }
    }
}