package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InforAfterLoginActivity : AppCompatActivity() {

    private lateinit var nameAccountTxt: TextView
    private lateinit var playBtn: ImageButton
    private lateinit var rankBtn: ImageButton

    private lateinit var sharedPrefManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.infor_after_login_main)

        val nameAccount = intent.getStringExtra("NAME_ACCOUNT")


        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()

        nameAccountTxt = findViewById(R.id.nameAccountTxt)
        playBtn = findViewById(R.id.playBtn)
        rankBtn = findViewById(R.id.rankBtn)

        if(language == "en"){
            playBtn.setImageResource(R.drawable.play_button_en)
            rankBtn.setImageResource(R.drawable.rank_button_en)
        }
        else if(language == "vi"){
            playBtn.setImageResource(R.drawable.play_button_vi)
            rankBtn.setImageResource(R.drawable.rank_button_vi)

        }

        nameAccountTxt.text = nameAccount

        playBtn.setOnClickListener {
            val changeUi = Intent(this,PlayGameActivity::class.java)
            changeUi.putExtra("NAME_ACCOUNT", nameAccount)
            startActivity(changeUi)
        }

        rankBtn.setOnClickListener {
            val changeUi = Intent(this, RankRecyclerViewActivity::class.java)
            startActivity(changeUi)
        }

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