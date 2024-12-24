package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class PlayGameActivity : AppCompatActivity() {
    private lateinit var namePlayer: TextView

    private lateinit var sharedPrefManager: SharedPreferenceManager
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var gameManager: GameManager

    private var isBackPress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.activity_main)

        sharedPrefManager = SharedPreferenceManager(this)
        val sttPlayerMode = sharedPrefManager.getPlayerMode()

        namePlayer = findViewById(R.id.nameAccountPlayTxt)

        if (sttPlayerMode == "offline") {
            namePlayer.text = ""
        } else {
            firebaseManager = FirebaseManager()
            firebaseManager.initFirebase()
            firebaseManager.getNameAccount { result ->
                namePlayer.text = result
            }
        }

        gameManager = findViewById(R.id.gameManager)

//        val nameAccount = intent.getStringExtra("NAME_ACCOUNT")
//        if(nameAccount != "@Off_play"){
//            namePlayer.text = nameAccount
//        }
//        else namePlayer.text = ""

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

    override fun onStart() {
        super.onStart()
        println("-----> onStart")
    }

    override fun onResume() {
        super.onResume()
        println("-----> onResume")
//        gameManager.startGame()
    }

    override fun onPause() {
        super.onPause()
        println("-----> onPause")

    }

    override fun onStop() {
        super.onStop()
        println("-----> onStop")
        if (!gameManager.getStatusBackBtn() && !isBackPress) {
            if (sharedPrefManager.getPlayerMode() == "offline") {
                val changeUi = Intent(this, MainActivity::class.java)
                changeUi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(changeUi)
                finish()
            } else {
                firebaseManager = FirebaseManager()
                firebaseManager.initFirebase()
                val changeUi = Intent(this, InforAfterLoginActivity::class.java)
                changeUi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(changeUi)
                finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        println("-----> onDestroy")
    }

    override fun onBackPressed() {
        isBackPress = true
        super.onBackPressed()
    }
}