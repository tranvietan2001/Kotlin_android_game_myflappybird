package com.example.mybird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class ShopRecyclerViewActivity: AppCompatActivity() {
    private lateinit var sttPlayerMode: TextView
    private lateinit var coinImg: ImageView
    private lateinit var coinTxt: TextView


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val shopList = mutableListOf<ShopInfor>()

    private lateinit var backBtn: ImageButton

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên

        setContentView(R.layout.recycler_shop_activity_main)

        sttPlayerMode = findViewById(R.id.sttPlayerMode)
        coinImg = findViewById(R.id.coinImg)
        coinTxt = findViewById(R.id.coinTxt)

        val modePlayer = intent.getStringExtra("MODE_PLAYER")
        if(modePlayer == "@Onl_play"){
            sttPlayerMode.text = "ONL"
            coinImg.setImageResource(R.drawable.coin_gold)
            coinTxt.text = "200"
        }
        else if(modePlayer == "@Off_play"){
            sttPlayerMode.text = ""
            coinImg.setImageResource(R.drawable.coin_silver)
            coinTxt.text = "0"
        }
        else Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()

        sharedPreferenceManager = SharedPreferenceManager(this)

        backBtn = findViewById(R.id.backBtn)

        // Khởi tạo dữ liệu
        shopList.add(ShopInfor("bird1_down", 10, 5))
        shopList.add(ShopInfor("bird2_down", 20, 15))
        shopList.add(ShopInfor("bird3_down", 30, 25))
        shopList.add(ShopInfor("bird4_down", 40, 35))
        shopList.add(ShopInfor("bird5_down", 50, 45))


        // Cài đặt RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShopAdapter(this, shopList, sharedPreferenceManager) { shopInfo ->
            // Lưu tên bird đã mua vào SharedPreference
            sharedPreferenceManager.savePurchasedBird(shopInfo.nameBird)
            // Cập nhật RecyclerView
            adapter.notifyDataSetChanged() // Hoặc bạn có thể tạo lại adapter nếu cần
            Toast.makeText(this, "Buying ${shopInfo.nameBird}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter




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
}