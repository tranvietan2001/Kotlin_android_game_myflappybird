package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
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
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.ArrayList
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var sOnlineBtn: ImageButton
    private lateinit var sOfflineBtn: ImageButton
    private lateinit var sShopBtn: ImageButton
    private lateinit var sConfigBtn: ImageButton

    private lateinit var sharedPrefManager: SharedPreferenceManager

    private val PERMISSION_REQUEST_CODE = 1606
    private var permissions = arrayOf(android.Manifest.permission.INTERNET)

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() //ẩn phần viền trên

        checkPermission()

        sharedPrefManager = SharedPreferenceManager(this)
        sharedPrefManager.savePlayerMode("offline")
        sharedPrefManager.savePurchasedBird("bird1_down")

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

        sOnlineBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    //XIN CAP QUYEN SU DUNG INTERNET -> OK thì chuyển trang ko thì vân ở lại trang MAIN

                    ///////
                    if (isInternetAvailable()) {
                        sharedPrefManager.savePlayerMode("online")
                        val changeUi = Intent(this, LoginActivity::class.java)
                        startActivity(changeUi)
                    }else{
                        Toast.makeText(this, getString(R.string.internet_denied), Toast.LENGTH_SHORT).show()
                    }



                    true
                }

                MotionEvent.ACTION_DOWN -> {
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
                MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    sharedPrefManager.savePlayerMode("offline")
                    val changeUi = Intent(this, PlayGameActivity::class.java)
                    changeUi.putExtra("NAME_ACCOUNT", "@Off_play")
                    startActivity(changeUi)
//            finish()

                    true
                }

                MotionEvent.ACTION_DOWN -> {
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
                MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    val changeUi = Intent(this, ShopRecyclerViewActivity::class.java)
                    changeUi.putExtra("MODE_PLAYER", "@Off_play")
                    startActivity(changeUi)
                    true
                }

                MotionEvent.ACTION_DOWN -> {
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
                MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    val changeUi = Intent(this, ConfigActivity::class.java)
                    startActivity(changeUi)
//                    finish()
                    true
                }

                MotionEvent.ACTION_DOWN -> {
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


    //    private fun checkPermission(): Boolean{
//        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
//    }
    private fun checkPermission() {
        val permissionsNeed: ArrayList<String> = ArrayList()
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeed.add(android.Manifest.permission.INTERNET)
        }
        if (permissionsNeed.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeed.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            Toast.makeText(this, getString(R.string.internet_granted), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, getString(R.string.internet_granted), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, getString(R.string.internet_denied), Toast.LENGTH_LONG).show()
                    checkPermission()
                }

//                    val requestAgainLater = shouldShowRequestPermissionRationale(permissions[i])
//                    if(requestAgainLater){
//                        Toast.makeText(this,"Quyền truy cập đã bị từ chối", Toast.LENGTH_SHORT).show()
//                    }
//                    else{
//                        Toast.makeText(this,"Đi đến phần cài đặt để cấp quyền cho ứng dụng",
//                            Toast.LENGTH_SHORT).show()
//                    }
            }
        }
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