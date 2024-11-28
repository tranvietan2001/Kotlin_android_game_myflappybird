package com.example.mybird

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShopRecyclerViewActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val shopList = mutableListOf<ShopInfor>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.recycler_shop_activity_main)

        sharedPreferenceManager = SharedPreferenceManager(this)

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
    }
}