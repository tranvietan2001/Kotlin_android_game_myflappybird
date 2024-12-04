package com.example.mybird

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

@Suppress("DEPRECATION")
class RankRecyclerViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var searchTxt: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var refreshBtn: ImageButton
    private lateinit var backBtn: ImageButton

    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("ClickableViewAccessibility")
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


        setContentView(R.layout.recycler_view_main)

        searchTxt = findViewById(R.id.searchTxt)
        searchBtn = findViewById(R.id.searchBtn)
        refreshBtn = findViewById(R.id.refreshBtn)
        backBtn = findViewById(R.id.backBtn)

        firebaseManager = FirebaseManager()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseManager.rankQuery { list ->
//            println("ARR: -V: " + list.size)
            recyclerView.adapter = RankAdapter(list)
        }

//        searchBtn.setOnClickListener {
//            firebaseManager.rankQuery { list ->
//                val nameToFind = searchTxt.text.toString()
//                val matchedUsers = list.filter { it.name == nameToFind }
//                Toast.makeText(this, matchedUsers.toString(), Toast.LENGTH_SHORT).show()
//                recyclerView.adapter = RankAdapter(matchedUsers)
//            }
//        }
//        refreshBtn.setOnClickListener {
//            firebaseManager.rankQuery { list ->
//                searchTxt.setText("")
//                recyclerView.adapter = RankAdapter(list)
//            }
//        }

        searchBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    firebaseManager.rankQuery { list ->
                        val nameToFind = searchTxt.text.toString()
                        val matchedUsers = list.filter { it.name == nameToFind }
                        Toast.makeText(this, matchedUsers.toString(), Toast.LENGTH_SHORT).show()
                        recyclerView.adapter = RankAdapter(matchedUsers)
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


        refreshBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)
                    firebaseManager.rankQuery { list ->
                        searchTxt.setText("")
                        recyclerView.adapter = RankAdapter(list)
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