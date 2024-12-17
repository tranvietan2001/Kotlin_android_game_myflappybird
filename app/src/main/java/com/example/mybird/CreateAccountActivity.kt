package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Locale


@Suppress("DEPRECATION")
class CreateAccountActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var nameAccTxt: EditText
    private lateinit var createAccBtn: ImageButton
    private lateinit var nofiticalCreateTxt: TextView
    private lateinit var loadingIV: ImageView
    private lateinit var backBtn: ImageButton

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId", "SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() //ẩn phần viền trên
//        setContentView(R.layout.create_account_activity_main)

        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContentView(R.layout.create_account_activity_main)


        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailTxt = findViewById(R.id.emailAccTxt)
        passwordTxt = findViewById(R.id.passwordAccTxt)
        nameAccTxt = findViewById(R.id.nameAccTxt)
        createAccBtn = findViewById(R.id.createAccBtn)
        nofiticalCreateTxt = findViewById(R.id.failCreateTxt)
        loadingIV = findViewById(R.id.loadingIV)
        backBtn = findViewById(R.id.backBtn)

        loadingIV.visibility = View.GONE
        startRotationAnimation()
        nofiticalCreateTxt.visibility = View.GONE
//        nofiticalCreateTxt.text = ""
        if (language == "en") {
            createAccBtn.setImageResource(R.drawable.create_button_en)
        } else if (language == "vi") {
            createAccBtn.setImageResource(R.drawable.create_button_vi)

        }

        createAccBtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    scaleView(v, 1f)

                    nofiticalCreateTxt.visibility = View.GONE
                    loadingIV.visibility = View.VISIBLE

                    val email = emailTxt.text
                    val password = passwordTxt.text
                    val name = nameAccTxt.text

                    val isCheckEmail = isValidEmail(email.toString())
                    val isCheckName = isValidNameAcc(name.toString())

                    // nếu ok thì chuyển đế tram login
                    if(isInternetAvailable()) {
                        if (isCheckEmail && password.toString().length >= 6 && isCheckName) {

                            firebaseManager.createAccount(
                                email.toString(),
                                password.toString()
                            ) { result1 ->
                                if (result1 == "account created successfully") {
                                    nofiticalCreateTxt.text =
                                        "====================================="
                                    loadingIV.visibility = View.VISIBLE

                                    firebaseManager.createAccountName(name.toString()) { result2 ->
                                        if (result2 == "name account created successfully") {
                                            loadingIV.visibility = View.GONE
//                                        val changeUi = Intent(this, LoginActivity::class.java)
//                                        startActivity(changeUi)
                                            finish()
                                        } else nofiticalCreateTxt.text = result2
                                    }
                                } else {
//                        nofiticalCreateTxt.text = result1.toString()
                                }
                            }
                        } else {
                            nofiticalCreateTxt.setText(R.string.check_if_email_is_valid_password_length_must_be_over_6_characters)
                            nofiticalCreateTxt.visibility = View.VISIBLE
                            loadingIV.visibility = View.GONE
                        }
                    }
                    else Toast.makeText(this, getString(R.string.internet_interrupted), Toast.LENGTH_LONG).show()

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

    override fun onResume() {
        super.onResume()
        hideSystemUI()
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

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]{3,}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return Regex(emailRegex).matches(email.trim())
    }

    fun isValidNameAcc(name: String): Boolean {
        val nameRegex = "^[a-zA-Z]{3,6}$"
        return Regex(nameRegex).matches(name.trim())
    }

    fun startRotationAnimation() {
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