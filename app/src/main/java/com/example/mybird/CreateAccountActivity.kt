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


class CreateAccountActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwardTxt: EditText
    private lateinit var nameAccTxt: EditText
    private lateinit var createAccBtn: Button
    private lateinit var failCreateTxt: TextView

    private lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.create_account_activity_main)

        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailTxt = findViewById(R.id.emailAccTxt)
        passwardTxt = findViewById(R.id.passwordAccTxt)
        nameAccTxt = findViewById(R.id.nameAccTxt)
        createAccBtn = findViewById(R.id.createAccBtn)
        failCreateTxt = findViewById(R.id.failCreateTxt)

        failCreateTxt.visibility = View.VISIBLE

        createAccBtn.setOnClickListener {
            val email = emailTxt.text
            val password = passwardTxt.text
            val name = nameAccTxt.text

            val isCheckEmail = isValidEmail(email.toString())

            Toast.makeText(this, "$isCheckEmail", Toast.LENGTH_SHORT).show()


            if (email.isNotEmpty() && password.isNotEmpty() && isCheckEmail && password.toString().length >=6) {
                // Gọi hàm tạo tài khoản trong coroutine
                lifecycleScope.launch {
                    /*val result = */firebaseManager.createAccount(email.toString(), password.toString())
//                    Toast.makeText(this@CreateAccountActivity, result, Toast.LENGTH_SHORT).show()
                }
            } else {
//                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
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

    fun isValidEmail(email: String): Boolean {
        // Biểu thức chính quy cho địa chỉ email
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return Regex(emailRegex).matches(email.trim())
    }
}