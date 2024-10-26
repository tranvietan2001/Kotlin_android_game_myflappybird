package com.example.mybird

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

    private var accountEmail= ""
    private var password = ""

    private lateinit var firebaseManager: FirebaseManager

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

        failLoginTxt.visibility = View.GONE

        loginBtn.setOnClickListener {
            accountEmail = emailTxt.text.toString()
            password = passwordTxt.text.toString()
            if ((accountEmail == "" && password == "") || accountEmail == "" || password == "") {
                failLoginTxt.visibility = View.VISIBLE
                Toast.makeText(this, "ERROR: =========", Toast.LENGTH_LONG).show()
            } else {
                failLoginTxt.visibility = View.GONE  // ẩn
//                val result = firebaseManager.loginAccount(accountName, password)

                firebaseManager.loginAccount(accountEmail, password){ result ->
                    Toast.makeText(this, "Dang nhap: $result", Toast.LENGTH_SHORT).show()
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