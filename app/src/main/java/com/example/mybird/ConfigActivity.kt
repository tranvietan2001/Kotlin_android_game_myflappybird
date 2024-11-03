package com.example.mybird

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {

    private lateinit var soundOnCb: CheckBox
    private lateinit var soundOffCb: CheckBox
    private lateinit var lgEnCb: CheckBox
    private lateinit var lgViCb: CheckBox
    private lateinit var confirmBtn: Button

    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.config_activity_main)

        sharedPrefManager = SharedPreferenceManager(this)

        soundOnCb = findViewById(R.id.soundOnCB)
        soundOffCb = findViewById(R.id.soundOffCB)
        lgEnCb = findViewById(R.id.lgEnCB)
        lgViCb = findViewById(R.id.lgViCB)
        confirmBtn = findViewById(R.id.confirmBtn)

        var sttSound = sharedPrefManager.getStatusSoundConfig()
        var sttLang = sharedPrefManager.getLanguageConfig()

        if (sttSound) {
            soundOnCb.isChecked = true
            soundOffCb.isChecked = false
        }else{
            soundOnCb.isChecked = false
            soundOffCb.isChecked = true
        }

        if(sttLang == "en"){
            lgEnCb.isChecked = true
            lgViCb.isChecked = false
        }else{
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

        confirmBtn.setOnClickListener {
            val getSttSound = sharedPrefManager.getStatusSoundConfig()
            val getSttLang = sharedPrefManager.getLanguageConfig()
            Toast.makeText(
                this,
                "CONFIG - SOUND: $getSttSound - LANGUAGE: $getSttLang",
                Toast.LENGTH_SHORT
            ).show()

            val changeUi = Intent(this, MainActivity::class.java)
            startActivity(changeUi)
            finish()
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