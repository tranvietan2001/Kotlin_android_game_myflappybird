package com.example.mybird

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class InforAfterLoginActivity : AppCompatActivity() {

    private lateinit var nameAccountTxt: TextView
    private lateinit var playBtn: ImageButton
    private lateinit var rankBtn: ImageButton
    private lateinit var shopBtn: ImageButton
    private lateinit var backBtn: ImageButton

    private lateinit var sharedPrefManager: SharedPreferenceManager
    private lateinit var firebaseManager: FirebaseManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.infor_after_login_main)

        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()
        val sttPlayerMode = sharedPrefManager.getPlayerMode()

        nameAccountTxt = findViewById(R.id.nameAccountTxt)
        playBtn = findViewById(R.id.playBtn)
        rankBtn = findViewById(R.id.rankBtn)
        shopBtn = findViewById(R.id.shopBtn)
        backBtn = findViewById(R.id.backBtn)

        if (language == "en") {
            playBtn.setImageResource(R.drawable.play_button_en)
            rankBtn.setImageResource(R.drawable.rank_button_en)
            shopBtn.setImageResource(R.drawable.shop_button_en)
        } else if (language == "vi") {
            playBtn.setImageResource(R.drawable.play_button_vi)
            rankBtn.setImageResource(R.drawable.rank_button_vi)
            shopBtn.setImageResource(R.drawable.shop_button_vi)
        }

        if (sttPlayerMode == "offline") {
            nameAccountTxt.text = ""
        } else {
            firebaseManager = FirebaseManager()
            firebaseManager.initFirebase()
            firebaseManager.getNameAccount { result ->
                nameAccountTxt.text = result
            }
        }

        playBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    if (isInternetAvailable()) {
                        val changeUi = Intent(this, PlayGameActivity::class.java)
//                    changeUi.putExtra("NAME_ACCOUNT", nameAccount)
                        startActivity(changeUi)
                    } else Toast.makeText(this, getString(R.string.internet_interrupted), Toast.LENGTH_LONG).show()
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

        rankBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
//                    finish()
                    if (isInternetAvailable()) {
                        val changeUi = Intent(this, RankRecyclerViewActivity::class.java)
                        startActivity(changeUi)
                    } else Toast.makeText(this, getString(R.string.internet_interrupted), Toast.LENGTH_LONG).show()
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

        shopBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    if (isInternetAvailable()) {
                        val changeUi = Intent(this, ShopRecyclerViewActivity::class.java)
                        changeUi.putExtra("MODE_PLAYER", "@Onl_play")
                        startActivity(changeUi)
                    } else Toast.makeText(this, getString(R.string.internet_interrupted), Toast.LENGTH_LONG).show()
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

        backBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    finish()
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

    override fun onResume() {
        super.onResume()
        hideSystemUI()
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

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            // Đối với các phiên bản Android trước Marshmallow
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

}