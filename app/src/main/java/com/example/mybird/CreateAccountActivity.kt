package com.example.mybird

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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


class CreateAccountActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwardTxt: EditText
    private lateinit var nameAccTxt: EditText
    private lateinit var createAccBtn: ImageButton
    private lateinit var nofiticalCreateTxt: TextView
    private lateinit var loadingIV: ImageView

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var sharedPrefManager: SharedPreferenceManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //ẩn phần viền trên
        setContentView(R.layout.create_account_activity_main)

        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()

        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailTxt = findViewById(R.id.emailAccTxt)
        passwardTxt = findViewById(R.id.passwordAccTxt)
        nameAccTxt = findViewById(R.id.nameAccTxt)
        createAccBtn = findViewById(R.id.createAccBtn)
        nofiticalCreateTxt = findViewById(R.id.failCreateTxt)
        loadingIV = findViewById(R.id.loadingIV)
        loadingIV.visibility = View.GONE
        startRotationAnimation()
        nofiticalCreateTxt.visibility = View.GONE

        if(language == "en"){
            createAccBtn.setImageResource(R.drawable.create_button_en)
        }
        else if(language == "vi"){
            createAccBtn.setImageResource(R.drawable.create_button_vi)
        }

        createAccBtn.setOnClickListener {
            nofiticalCreateTxt.visibility = View.GONE
            loadingIV.visibility = View.VISIBLE

            val email = emailTxt.text
            val password = passwardTxt.text
            val name = nameAccTxt.text

            val isCheckEmail = isValidEmail(email.toString())
            val isCheckName = isValidNameAcc(name.toString())

//            Toast.makeText(this, "$isCheckEmail", Toast.LENGTH_SHORT).show()

            // nếu ok thì chuyển đế tram login
            if (isCheckEmail && password.toString().length >=6 && isCheckName) {
                // Gọi hàm tạo tài khoản trong coroutine
                lifecycleScope.launch {
                    val result = firebaseManager.createAccount(email.toString(), password.toString())
                    Toast.makeText(this@CreateAccountActivity, result, Toast.LENGTH_SHORT).show()
                    val nameResult = firebaseManager.createAccountName(name.toString())
                    Toast.makeText(this@CreateAccountActivity, nameResult, Toast.LENGTH_SHORT).show()


                    // thêm điều kiệm -> phải đổi kiểu trả về thành t/f hoặc string quy ước, -> xét hiển thị notifical

                }
            } else {
                // rule create acc
//                nofiticalCreateTxt.text = "Check if email is valid, password length must be over 6 characters"
                nofiticalCreateTxt.visibility = View.VISIBLE
                loadingIV.visibility = View.GONE
            }
        }

        val valMark = findViewById<EditText>(R.id.testMarkTxt)
        val updateMarlBtn = findViewById<Button>(R.id.updateMarkBtn)
        val testListData = findViewById<TextView>(R.id.testListDataTxt)
        updateMarlBtn.setOnClickListener {
            val mark = valMark.text.toString()
            val resultUpdate = firebaseManager.updateMark(nameAccount = nameAccTxt.toString(), mark.toInt())
            Toast.makeText(this@CreateAccountActivity, resultUpdate, Toast.LENGTH_SHORT).show()

            testListData.text = resultUpdate
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

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]{3,}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return Regex(emailRegex).matches(email.trim())
    }
    fun isValidNameAcc(name: String): Boolean{
        val nameRegex = "^[a-zA-Z]{3,10}$"
        return Regex(nameRegex).matches(name.trim())
    }

    fun startRotationAnimation() {
        // Tạo ObjectAnimator để quay hình ảnh
        val animator = ObjectAnimator.ofFloat(loadingIV, "rotation", 0f, 360f)
        animator.duration = 2000 // Thời gian quay (2 giây)
        animator.repeatCount = ObjectAnimator.INFINITE // Lặp lại vô hạn
        animator.start() // Bắt đầu hoạt động
    }
}