package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var loginBtn: Button
    private lateinit var failLoginTxt: TextView
    private lateinit var createAccountTxt: TextView
    private lateinit var forgotPassTxt: TextView
    private lateinit var loadingIV: ImageView
    private var accountEmail = ""
    private var password = ""

    private lateinit var firebaseManager: FirebaseManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
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
        loadingIV.visibility = View.GONE
        failLoginTxt.visibility = View.GONE

        startRotationAnimation()

        loginBtn.setOnClickListener {
            loadingIV.visibility = View.VISIBLE
            accountEmail = emailTxt.text.toString()
            password = passwordTxt.text.toString()
            if ((accountEmail == "" && password == "") || accountEmail == "" || password == "") {
                failLoginTxt.visibility = View.VISIBLE
                loadingIV.visibility = View.GONE
//                Toast.makeText(this, "ERROR: =========", Toast.LENGTH_LONG).show()
            } else {
                failLoginTxt.visibility = View.GONE  // ẩn
                loginBtn.visibility = View.VISIBLE
                firebaseManager.loginAccount(accountEmail, password) { result ->
//                    Toast.makeText(this, "Dang nhap FAIL: $result", Toast.LENGTH_SHORT).show()
                    if (result != "fail") {
                        if (result != "") {
                            if (result != "null") {
                                emailTxt.setText("")
                                passwordTxt.setText("")
                                loadingIV.visibility = View.GONE
                                val changeUi = Intent(this, InforAfterLoginActivity::class.java)
                                changeUi.putExtra("NAME_ACCOUNT", result)
                                startActivity(changeUi)
                            }
                        }
////                        Toast.makeText(this, "Dang nhap OK: $result", Toast.LENGTH_SHORT).show()
//                        emailTxt.setText("")
//                        passwordTxt.setText("")
//
//                        val changeUi = Intent(this, InforAfterLoginActivity::class.java)
//                        changeUi.putExtra("NAME_ACCOUNT", result)
//                        startActivity(changeUi)
                    } else {
                        failLoginTxt.visibility = View.VISIBLE
                        loadingIV.visibility = View.GONE
                        Toast.makeText(this, "Dang nhap FAIL: $result", Toast.LENGTH_SHORT).show()
                    }

                }
//                Toast.makeText(this, "$accountName---$password", Toast.LENGTH_LONG).show()
//                val changeUi = Intent(this,PlayGameActivity::class.java)
//                startActivity(changeUi)
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

    }


//    private fun hideSystemUI() {
//        // Thiết lập chế độ toàn màn hình
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            hideSystemUI() // Đảm bảo chế độ toàn màn hình khi có tiêu điểm
//        }
//    }

    private fun startRotationAnimation() {
        // Tạo ObjectAnimator để quay hình ảnh
        val animator = ObjectAnimator.ofFloat(loadingIV, "rotation", 0f, 360f)
        animator.duration = 2000 // Thời gian quay (2 giây)
        animator.repeatCount = ObjectAnimator.INFINITE // Lặp lại vô hạn
        animator.start() // Bắt đầu hoạt động
    }
}