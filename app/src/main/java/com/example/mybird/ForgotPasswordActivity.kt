package com.example.mybird

//import android.annotation.SuppressLint
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.ColorSpace.Rgb
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailForgotTxt:EditText
    private lateinit var resetBtn:ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var nofitiTxt: TextView

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var sharedPrefManager: SharedPreferenceManager

//    @SuppressLint("MissingInflatedId")
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        setContentView(R.layout.forgot_password_activity_main)

        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContentView(R.layout.forgot_password_activity_main)

        // Khởi tạo FirebaseManager
        firebaseManager = FirebaseManager()
        firebaseManager.initFirebase()

        emailForgotTxt = findViewById(R.id.emailForgotTxt)
        resetBtn = findViewById(R.id.resetBtn)
        backBtn = findViewById(R.id.backBtn)
        nofitiTxt = findViewById(R.id.notifiTxt)

        if(language == "en"){
            resetBtn.setImageResource(R.drawable.reset_button_en)
            backBtn.setImageResource(R.drawable.back_button_en)
        }
        else if(language == "vi"){
            resetBtn.setImageResource(R.drawable.reset_button_vi)
            backBtn.setImageResource(R.drawable.back_button_vi)
        }

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
                else if(result == "There is a problem with the connection, please check again."){
                    nofitiTxt.text = getString(R.string.error_connect)
                }
//                nofitiTxt.text = result
                nofitiTxt.setTextColor(R.color.txt_error)
                nofitiTxt.visibility = View.VISIBLE
            }



        }

        backBtn.setOnClickListener {
            finish()
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
}