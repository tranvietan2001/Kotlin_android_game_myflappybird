package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var loginBtn: ImageButton
    private lateinit var failLoginTxt: TextView
    private lateinit var createAccountTxt: TextView
    private lateinit var forgotPassTxt: TextView
    private lateinit var loadingIV: ImageView
    private lateinit var backBtn: ImageButton
    private var accountEmail = ""
    private var password = ""

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
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

        setContentView(R.layout.login_activity_main)


        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        loginBtn = findViewById(R.id.loginBtn)
        failLoginTxt = findViewById(R.id.failLoginTxt)
        createAccountTxt = findViewById(R.id.signAccountTxt)
        forgotPassTxt = findViewById(R.id.forgotPassTxt)
        loadingIV = findViewById(R.id.loadingIV)
        backBtn = findViewById(R.id.backBtn)
        loadingIV.visibility = View.GONE
        failLoginTxt.visibility = View.GONE

        if (language == "en") {
            loginBtn.setImageResource(R.drawable.login_button_en)
        } else if (language == "vi") {
            loginBtn.setImageResource(R.drawable.login_button_vi)
        }

        startRotationAnimation()

//        loginBtn.setOnClickListener {
//            loadingIV.visibility = View.VISIBLE
//            accountEmail = emailTxt.text.toString()
//            password = passwordTxt.text.toString()
//            if ((accountEmail == "" && password == "") || accountEmail == "" || password == "") {
//                failLoginTxt.visibility = View.VISIBLE
//                loadingIV.visibility = View.GONE
////                Toast.makeText(this, "ERROR: =========", Toast.LENGTH_LONG).show()
//            } else {
//                failLoginTxt.visibility = View.GONE  // ẩn
//                loginBtn.visibility = View.VISIBLE
//                firebaseManager.loginAccount(accountEmail, password) { result ->
////                    Toast.makeText(this, "Dang nhap FAIL: $result", Toast.LENGTH_SHORT).show()
//                    if (result != "fail") {
//                        if (result != "") {
//                            if (result != "null") {
//                                emailTxt.setText("")
//                                passwordTxt.setText("")
//                                loadingIV.visibility = View.GONE
//                                val changeUi = Intent(this, InforAfterLoginActivity::class.java)
//                                changeUi.putExtra("NAME_ACCOUNT", result)
//                                startActivity(changeUi)
//                            }
//                        }
//////                        Toast.makeText(this, "Dang nhap OK: $result", Toast.LENGTH_SHORT).show()
////                        emailTxt.setText("")
////                        passwordTxt.setText("")
////
////                        val changeUi = Intent(this, InforAfterLoginActivity::class.java)
////                        changeUi.putExtra("NAME_ACCOUNT", result)
////                        startActivity(changeUi)
//                    } else {
//                        failLoginTxt.visibility = View.VISIBLE
//                        loadingIV.visibility = View.GONE
//                        Toast.makeText(this, "Dang nhap FAIL: $result", Toast.LENGTH_SHORT).show()
//                    }
//                }
////                Toast.makeText(this, "$accountName---$password", Toast.LENGTH_LONG).show()
////                val changeUi = Intent(this,PlayGameActivity::class.java)
////                startActivity(changeUi)
//            }
//        }


        loginBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    loadingIV.visibility = View.VISIBLE
                    accountEmail = emailTxt.text.toString()
                    password = passwordTxt.text.toString()
                    if ((accountEmail == "" && password == "") || accountEmail == "" || password == "") {
                        failLoginTxt.visibility = View.VISIBLE
                        loadingIV.visibility = View.GONE
                    } else {
                        failLoginTxt.visibility = View.GONE  // ẩn
                        loginBtn.visibility = View.VISIBLE
                        firebaseManager.loginAccount(accountEmail, password) { result ->
                            if (result != "fail") {
                                if (result != "") {
                                    if (result != "null") {
                                        emailTxt.setText("")
                                        passwordTxt.setText("")
                                        loadingIV.visibility = View.GONE


                                        firebaseManager.getScore { resultScore ->
                                            Toast.makeText(this, resultScore, Toast.LENGTH_SHORT).show()
                                        }

                                        val changeUi = Intent(this, InforAfterLoginActivity::class.java)
                                        changeUi.putExtra("NAME_ACCOUNT", result)
                                        startActivity(changeUi)
                                    }
                                }
                            } else {
                                failLoginTxt.visibility = View.VISIBLE
                                loadingIV.visibility = View.GONE
                                Toast.makeText(this, "Dang nhap FAIL: $result", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
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

        createAccountTxt.setOnClickListener {
            val changeUi = Intent(this, CreateAccountActivity::class.java)
            startActivity(changeUi)
        }

        forgotPassTxt.setOnClickListener {
            val changeUi = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(changeUi)
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

    private fun startRotationAnimation() {
        // Tạo ObjectAnimator để quay hình ảnh
        val animator = ObjectAnimator.ofFloat(loadingIV, "rotation", 0f, 360f)
        animator.duration = 2000 // Thời gian quay (2 giây)
        animator.repeatCount = ObjectAnimator.INFINITE // Lặp lại vô hạn
        animator.start() // Bắt đầu hoạt động
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