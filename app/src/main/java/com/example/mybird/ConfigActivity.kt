package com.example.mybird

import android.annotation.SuppressLint
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.config_activity_main)

        soundOnCb = findViewById(R.id.soundOnCB)
        soundOffCb = findViewById(R.id.soundOffCB)
        lgEnCb = findViewById(R.id.lgEnCB)
        lgViCb = findViewById(R.id.lgViCB)
        confirmBtn = findViewById(R.id.confirmBtn)

        soundOnCb.isChecked = true
        soundOffCb.isChecked = false

//        if(soundOffCb.isChecked){
//            Toast.makeText(this, "SOUND OFF", Toast.LENGTH_SHORT).show()
//            soundOnCb.isChecked = false
//        }
//        else{
//            Toast.makeText(this, "SOUND ON", Toast.LENGTH_SHORT).show()
//            soundOffCb.isChecked = false
//        }

        soundOnCb.setOnClickListener {
            !soundOnCb.isChecked
            soundOffCb.isChecked = !soundOnCb.isChecked
            Toast.makeText(this, "SOUND ON status: ${soundOnCb.isChecked.toString()} ", Toast.LENGTH_SHORT).show()
        }

        soundOffCb.setOnClickListener {
            !soundOffCb.isChecked
            soundOnCb.isChecked = !soundOffCb.isChecked
            Toast.makeText(this, "SOUND ON status: ${soundOffCb.isChecked.toString()} ", Toast.LENGTH_SHORT).show()
        }


        lgEnCb.setOnClickListener {
            !lgEnCb.isChecked
            lgViCb.isChecked = !lgEnCb.isChecked
            Toast.makeText(this, "SOUND ON status: ${lgEnCb.isChecked.toString()} ", Toast.LENGTH_SHORT).show()
        }

        lgViCb.setOnClickListener {
            !lgViCb.isChecked
            lgEnCb.isChecked = !lgViCb.isChecked
            Toast.makeText(this, "SOUND ON status: ${lgViCb.isChecked.toString()} ", Toast.LENGTH_SHORT).show()
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