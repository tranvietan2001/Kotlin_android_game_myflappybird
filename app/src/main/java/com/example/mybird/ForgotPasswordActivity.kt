package com.example.mybird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailForgotTxt:EditText
    private lateinit var resetBtn:Button
    private lateinit var backBtn: Button
    private lateinit var nofitiTxt: TextView

    private lateinit var firebaseManager: FirebaseManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.forgot_password_activity_main)

        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailForgotTxt = findViewById(R.id.emailForgotTxt)
        resetBtn = findViewById(R.id.resetBtn)
        backBtn = findViewById(R.id.backBtn)
        nofitiTxt = findViewById(R.id.notifiTxt)

        nofitiTxt.visibility = View.GONE
        val emailForgot = emailForgotTxt.text

        resetBtn.setOnClickListener {
            nofitiTxt.visibility = View.GONE
//            nofitiTxt.text = "Wait a moment for verification"
            nofitiTxt.text = getString(R.string.wait_a_moment_for_verification)
            //có thể cho xác thực send mail reset ok thì tự back về login luôn cũng đc
            firebaseManager.forgotPass(emailForgot.toString()){ result ->
                if(result == "checkMail"){
                    nofitiTxt.text = getString(R.string.check_email_reset_password)
                }else if(result == "resetFail"){
                    nofitiTxt.text = getString(R.string.reset_password_fail)
                }else if(result == "emailNotExist"){
                    nofitiTxt.text = getString(R.string.email_dose_not_exist)
                }
//                nofitiTxt.text = result
                nofitiTxt.visibility = View.VISIBLE
            }
//            {result ->
//                nofitiTxt.setText(result)
//            }


        }

        backBtn.setOnClickListener {
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