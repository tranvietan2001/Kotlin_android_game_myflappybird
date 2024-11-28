package com.example.mybird

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class ConfigActivity : AppCompatActivity() {

    private lateinit var soundOnCb: CheckBox
    private lateinit var soundOffCb: CheckBox
    private lateinit var lgEnCb: CheckBox
    private lateinit var lgViCb: CheckBox
    private lateinit var confirmBtn: ImageButton
    private lateinit var birdView: ImageView
    private lateinit var nextBtn: ImageButton
    private lateinit var preBtn: ImageButton
    private lateinit var birdName: TextView
    var i = 0

    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "UseCompatLoadingForDrawables")
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

        setContentView(R.layout.config_activity_main)


        soundOnCb = findViewById(R.id.soundOnCB)
        soundOffCb = findViewById(R.id.soundOffCB)
        lgEnCb = findViewById(R.id.lgEnCB)
        lgViCb = findViewById(R.id.lgViCB)
        confirmBtn = findViewById(R.id.confirmBtn)
        birdView = findViewById(R.id.birdView)
        nextBtn = findViewById(R.id.nextBtn)
        preBtn = findViewById(R.id.preBtn)
        birdName = findViewById(R.id.birdNameTxt)

        val drawableNames = listOf("bird1_down", "bird2_down", "bird3_down", "bird4_down", "bird5_down") // Danh sách tên drawable



        if (language == "en") {
            confirmBtn.setImageResource(R.drawable.confirm_button_en)
        } else if (language == "vi") {
            confirmBtn.setImageResource(R.drawable.confirm_button_vi)
        }


        val sttSound = sharedPrefManager.getStatusSoundConfig()
        val sttLang = sharedPrefManager.getLanguageConfig()

        if (sttSound) {
            soundOnCb.isChecked = true
            soundOffCb.isChecked = false
        } else {
            soundOnCb.isChecked = false
            soundOffCb.isChecked = true
        }

        if (sttLang == "en") {
            lgEnCb.isChecked = true
            lgViCb.isChecked = false
        } else {
            lgEnCb.isChecked = false
            lgViCb.isChecked = true
        }

        soundOnCb.setOnClickListener {
            !soundOnCb.isChecked
            soundOffCb.isChecked = !soundOnCb.isChecked
            sharedPrefManager.saveStatusSoundConfig(soundOnCb.isChecked)
        }

        soundOffCb.setOnClickListener {
            !soundOffCb.isChecked
            soundOnCb.isChecked = !soundOffCb.isChecked
            sharedPrefManager.saveStatusSoundConfig(soundOnCb.isChecked)
        }

        lgEnCb.setOnClickListener {
            !lgEnCb.isChecked
            lgViCb.isChecked = !lgEnCb.isChecked
            if (lgEnCb.isChecked) {
                sharedPrefManager.saveLanguageConfig("en")
            } else sharedPrefManager.saveLanguageConfig("vi")
        }

        lgViCb.setOnClickListener {
            !lgViCb.isChecked
            lgEnCb.isChecked = !lgViCb.isChecked
            if (lgEnCb.isChecked) {
                sharedPrefManager.saveLanguageConfig("en")
            } else sharedPrefManager.saveLanguageConfig("vi")
        }

        preBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    i--
                    if (i < 0) i = 4

                    birdName.text = drawableNames[i].toString()
                    val drawableId = resources.getIdentifier(
                        drawableNames[i],
                        "drawable",
                        packageName
                    ) // Lấy ID drawable từ tên

                    if (drawableId != 0) {
                        val drawable = resources.getDrawable(drawableId, null) // Lấy drawable
                        birdView.setImageDrawable(drawable) // Gán cho ImageView
                    } else {
                        // Xử lý khi không tìm thấy drawable
//            Log.e("TAG", "Drawable not found")
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
        nextBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    i++
                    if (i > 4) i = 0

                    birdName.text = drawableNames[i].toString()

                    val drawableId = resources.getIdentifier(
                        drawableNames[i],
                        "drawable",
                        packageName
                    ) // Lấy ID drawable từ tên

                    if (drawableId != 0) {
                        val drawable = resources.getDrawable(drawableId, null) // Lấy drawable
                        birdView.setImageDrawable(drawable) // Gán cho ImageView
                    } else {
                        // Xử lý khi không tìm thấy drawable
//            Log.e("TAG", "Drawable not found")
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

        confirmBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    val changeUi = Intent(this, MainActivity::class.java)
                    startActivity(changeUi)
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

}