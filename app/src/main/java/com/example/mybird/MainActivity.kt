package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var sOnlineBtn: ImageButton
    private lateinit var sOfflineBtn: ImageButton
    private lateinit var sShopBtn: ImageButton
    private lateinit var sConfigBtn: ImageButton

    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên

        sharedPrefManager = SharedPreferenceManager(this)

        val language = sharedPrefManager.getLanguageConfig()
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContentView(R.layout.select_game_mode_activity_main)

        sOnlineBtn = findViewById(R.id.sOnlineBtn)
        sOfflineBtn = findViewById(R.id.sOfflineBtn)
        sShopBtn = findViewById(R.id.sShopBtn)
        sConfigBtn = findViewById(R.id.sConfigBtn)

        Toast.makeText(this, language, Toast.LENGTH_SHORT).show()
        if (language == "en") {
            sOnlineBtn.setImageResource(R.drawable.online_button_en)
            sOfflineBtn.setImageResource(R.drawable.offline_button_en)
            sShopBtn.setImageResource(R.drawable.shop_button_en)
            sConfigBtn.setImageResource(R.drawable.setting_button_en)
        } else if (language == "vi") {
            sOnlineBtn.setImageResource(R.drawable.online_button_vi)
            sOfflineBtn.setImageResource(R.drawable.offline_button_vi)
            sShopBtn.setImageResource(R.drawable.shop_button_vi)
            sConfigBtn.setImageResource(R.drawable.setting_button_vi)
        }

//        sharedPrefManager.savePlayerMode("offline")


//        sOnlineBtn.setOnClickListener {
//            sharedPrefManager.savePlayerMode("online")
//            val changeUi = Intent(this,LoginActivity::class.java)
//            startActivity(changeUi)
//        }

        sOnlineBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    sharedPrefManager.savePlayerMode("online")
                    val changeUi = Intent(this, LoginActivity::class.java)
                    startActivity(changeUi)

                    true
                }

                android.view.MotionEvent.ACTION_DOWN -> {
                    scaleView(v, 1.2f)
                    true
                }

                else -> {
                    false
                }
            }
        }

//        sOfflineBtn.setOnClickListener {
//            sharedPrefManager.savePlayerMode("offline")
//            val changeUi = Intent(this,PlayGameActivity::class.java)
//            changeUi.putExtra("NAME_ACCOUNT", "@Off_play")
//            startActivity(changeUi)
////            finish()
//        }

        sOfflineBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    sharedPrefManager.savePlayerMode("offline")
                    val changeUi = Intent(this, PlayGameActivity::class.java)
                    changeUi.putExtra("NAME_ACCOUNT", "@Off_play")
                    startActivity(changeUi)
//            finish()

                    true
                }

                android.view.MotionEvent.ACTION_DOWN -> {
                    scaleView(v, 1.2f)
                    true
                }

                else -> {
                    false
                }
            }
        }

//        sShopBtn.setOnClickListener {
//            val changeUi = Intent(this,ShopActivity::class.java)
//            startActivity(changeUi)
//        }
        sShopBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    val changeUi = Intent(this, ShopRecyclerViewActivity::class.java)
                    changeUi.putExtra("MODE_PLAYER", "@Off_play")
                    startActivity(changeUi)
                    true
                }

                android.view.MotionEvent.ACTION_DOWN -> {
                    scaleView(v, 1.2f)
                    true
                }

                else -> {
                    false
                }
            }
        }

//        sConfigBtn.setOnClickListener {
//            val changeUi = Intent(this,ConfigActivity::class.java)
//            startActivity(changeUi)
//            finish()
//        }

        sConfigBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    val changeUi = Intent(this, ConfigActivity::class.java)
                    startActivity(changeUi)
//                    finish()

                    true
                }

                android.view.MotionEvent.ACTION_DOWN -> {
                    scaleView(v, 1.2f)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun scaleView(view: View, scale: Float) {
        val animation = ScaleAnimation(
            scale, scale, // Scale X
            scale, scale, // Scale Y
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot Y
        )
        animation.duration = 100 // Thời gian cho animation
        animation.fillAfter = true // Giữ trạng thái cuối
        view.startAnimation(animation)
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