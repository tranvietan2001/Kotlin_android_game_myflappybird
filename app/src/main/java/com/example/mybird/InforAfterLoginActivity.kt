package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InforAfterLoginActivity : AppCompatActivity() {

    private lateinit var nameAccountTxt: TextView
    private lateinit var playBtn: Button
    private lateinit var rankBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.infor_after_login_main)

        val nameAccount = intent.getStringExtra("NAME_ACCOUNT")

        nameAccountTxt = findViewById(R.id.nameAccountTxt)
        playBtn = findViewById(R.id.playBtn)
        rankBtn = findViewById(R.id.rankBtn)


        nameAccountTxt.text = nameAccount

        playBtn.setOnClickListener {  }

        rankBtn.setOnClickListener {
            val changeUi = Intent(this, RecyclerViewActivity::class.java)
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