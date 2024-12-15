package com.example.mybird

import android.annotation.SuppressLint
import android.content.Intent
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
class ShopRecyclerViewActivity : AppCompatActivity() {
    private lateinit var sttPlayerMode: TextView
    private lateinit var coinImg: ImageView
    private lateinit var coinTxt: TextView
    private lateinit var birdUsedNameTxt: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val shopList = mutableListOf<ShopInfor>()

    private lateinit var backBtn: ImageButton

    private var coin: Int = 0
    private var coinOnline = 0
    private var listBirdOnline: List<String>? = null

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() //ẩn phần viền trên

        setContentView(R.layout.recycler_shop_activity_main)

        sttPlayerMode = findViewById(R.id.sttPlayerMode)
        coinImg = findViewById(R.id.coinImg)
        coinTxt = findViewById(R.id.coinTxt)
        birdUsedNameTxt = findViewById(R.id.birdUsedNameTxt)

        sharedPreferenceManager = SharedPreferenceManager(this)


        val modePlayer = intent.getStringExtra("MODE_PLAYER")
        if (modePlayer == "@Onl_play") {
            val firebaseManager: FirebaseManager = FirebaseManager()
            firebaseManager.initFirebase()

            firebaseManager.getNameAccount { result->
                sttPlayerMode.text = result
            }
            coinImg.setImageResource(R.drawable.coin_gold)

            firebaseManager.getCoinGold { resultCoin ->
                coin = resultCoin.toInt()
                coinTxt.text = coin.toString()
                setCoinOnline(coin)
            }

            firebaseManager.getBirdUsed { result->
                birdUsedNameTxt.text = result
            }

        } else if (modePlayer == "@Off_play") {
            sttPlayerMode.text = ""
            coinImg.setImageResource(R.drawable.coin_silver)
            coin = sharedPreferenceManager.getCoinSilver()
            coinTxt.text = coin.toString()
            birdUsedNameTxt.text = sharedPreferenceManager.getBirdUsed()
        } else Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()


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

            if (modePlayer == "@Onl_play") {
                val firebaseManager: FirebaseManager = FirebaseManager()
                firebaseManager.initFirebase()

                firebaseManager.getListBird { birdsString ->
                    val birdsList: List<String> = birdsString.split(",").map { it.trim() }
                    if (birdsList.contains(shopInfo.nameBird)) {
                        firebaseManager.updateBirdUsed(shopInfo.nameBird)
                        firebaseManager.getBirdUsed { nameBird ->
                            birdUsedNameTxt.text = nameBird
                        }
                    } else {
                        val birdPrice = shopInfo.coinSilver
                        if (coin >= birdPrice) {
                            coin -= birdPrice
//                            // update lại coin -> update lại coinTxt
//                            sharedPreferenceManager.setCoinSilver(coin)
                            firebaseManager.updateCoin(coin)
                            firebaseManager.getCoinGold { result->
                                coin= result.toInt()
                                coinTxt.text = coin.toString()
                            }

                            firebaseManager.updateBirds(shopInfo.nameBird) {
                                adapter.notifyDataSetChanged()      // Cập nhật RecyclerView
                            }
                            firebaseManager.updateBirdUsed(shopInfo.nameBird)
                            firebaseManager.getBirdUsed { nameBird ->
                                birdUsedNameTxt.text = nameBird
                            }
//
//                            // thêm bird mua vào ds
//                            sharedPreferenceManager.savePurchasedBird(shopInfo.nameBird)
//                            // Cập nhật RecyclerView
//                            adapter.notifyDataSetChanged()
//
//                            sharedPreferenceManager.setBirdUsed(shopInfo.nameBird)
//                            birdUsedNameTxt.text = sharedPreferenceManager.getBirdUsed().toString()
                        } else Toast.makeText(this, "not enough coins -ON", Toast.LENGTH_SHORT).show()

                    }
                }

            } else if (modePlayer == "@Off_play") {

                // kiểm tra ds mua đã tồn tại hay chưa nếu chưa thì thực hiện mua nếu đã có thì ko trừ tiên

                if (sharedPreferenceManager.getPurchasedBirds().contains(shopInfo.nameBird)) {
                    sharedPreferenceManager.setBirdUsed(shopInfo.nameBird)
                    birdUsedNameTxt.text = sharedPreferenceManager.getBirdUsed().toString()
                } else {

                    val birdPrice = shopInfo.coinSilver
                    if (coin >= birdPrice) {
                        coin -= birdPrice
                        // update lại coin -> update lại coinTxt
                        sharedPreferenceManager.setCoinSilver(coin)
                        coin = sharedPreferenceManager.getCoinSilver()
                        coinTxt.text = coin.toString()

                        // thêm bird mua vào ds
                        sharedPreferenceManager.savePurchasedBird(shopInfo.nameBird)
                        // Cập nhật RecyclerView
                        adapter.notifyDataSetChanged()

                        sharedPreferenceManager.setBirdUsed(shopInfo.nameBird)
                        birdUsedNameTxt.text = sharedPreferenceManager.getBirdUsed().toString()
                    } else Toast.makeText(this, "not enough coins - OFF", Toast.LENGTH_SHORT).show()
                }
            } else Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        backBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    if (modePlayer == "@Off_play") {
                        val changeUi = Intent(this, MainActivity::class.java)
                        changeUi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(changeUi)
                        finish()
                    } else {
                        val changeUi = Intent(this, InforAfterLoginActivity::class.java)
                        changeUi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(changeUi)
                        finish()
                    }
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

    private fun setCoinOnline(coin: Int) {
        this.coinOnline = coin
    }

    private fun setListBirdOnline(list: List<String>) {
        this.listBirdOnline = list
    }
}