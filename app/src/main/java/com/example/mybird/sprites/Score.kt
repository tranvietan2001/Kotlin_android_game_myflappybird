package com.example.mybird.sprites

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.mybird.FirebaseManager
import com.example.mybird.R
import com.example.mybird.SharedPreferenceManager

class Score(
    resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
    private val playerMode: String

) : Sprite {

    private val SCORE_PREF = "Score_pref"
    private val zero: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.zero)
    private val one: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.one)
    private val two: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.two)
    private val three: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.three)
    private val four: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.four)
    private val five: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.five)
    private val six: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.six)
    private val seven: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.seven)
    private val eight: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.eight)
    private val nine: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.nine)
    private val bmpScore: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.score)
    private val bmpBest: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.best)

    private val map: HashMap<Int, Bitmap> = hashMapOf(
        0 to zero,
        1 to one,
        2 to two,
        3 to three,
        4 to four,
        5 to five,
        6 to six,
        7 to seven,
        8 to eight,
        9 to nine
    )

    private var score: Int = 0
    private var topScore: Int = 0
    private var collision: Boolean = false

    override fun draw(canvas: Canvas) {
        // khi play se + score tang dan
        if (!collision) {
            val digits = convertToBitmaps(score)
            for (i in digits.indices) {
                val x = screenWidth / 2 - digits.size * zero.width / 2 + zero.width * i
                canvas.drawBitmap(digits[i], x.toFloat(), (screenHeight / 4).toFloat(), null)
            }
        }
        // khi gameover se show scores
        else {
            val currentDigits = convertToBitmaps(score)
            val topDigits = convertToBitmaps(topScore)

            canvas.drawBitmap(
                bmpScore,
                (screenWidth / 2 - bmpScore.width / 2).toFloat(),
                (3 * screenHeight / 4 - 2 * zero.height - bmpScore.height).toFloat(),
                null
            )

            for (i in currentDigits.indices) {
                val x = screenWidth / 2 - currentDigits.size * zero.width / 2 + zero.width * i
                canvas.drawBitmap(
                    currentDigits[i],
                    x.toFloat(),
                    (3 * screenHeight / 4 - zero.height - bmpScore.height).toFloat(),
                    null
                )
            }

            canvas.drawBitmap(
                bmpBest,
                (screenWidth / 2 - bmpBest.width / 2).toFloat(),
                (3 * screenHeight / 4 + zero.height - bmpBest.height).toFloat(),
                null
            )
            for (i in topDigits.indices) {
                val x = screenWidth / 2 - topDigits.size * zero.width / 2 + zero.width * i
                canvas.drawBitmap(
                    topDigits[i],
                    x.toFloat(),
                    (3 * screenHeight / 4 + 2 * zero.height - bmpScore.height).toFloat(),
                    null
                )
            }
        }
    }

    // chuyển đổi score thành score bằng ảnh, ghép các số lại
    private fun convertToBitmaps(amount: Int): ArrayList<Bitmap> {
        val digits = ArrayList<Bitmap>()
        if (amount == 0) {
            digits.add(zero)
        }
        var tempAmount = amount
        while (tempAmount > 0) {
            val lastDigit = tempAmount % 10
            tempAmount /= 10
            digits.add(map[lastDigit]!!)
        }
        return ArrayList(digits.reversed())
    }

    override fun update() {
        // Không có logic cập nhật trong lớp Score
    }

    fun updateScore(score: Int) {
        this.score = score
    }

    fun collision(prefs: SharedPreferences) {
        collision = true
        topScore = 0
        if (playerMode == "offline") {
            topScore = prefs.getInt(SCORE_PREF, 0)
            if (topScore < score) {
                prefs.edit().putInt(SCORE_PREF, score).apply()
                topScore = score
            }
        } else {
            val firebaseManager: FirebaseManager = FirebaseManager()
            firebaseManager.initFirebase()
            firebaseManager.getScore { resultScore ->
                topScore = resultScore.toInt()
                if (topScore < score) {
                    firebaseManager.updateScore(score)
                    topScore = score
                }
            }
//            topScore = 10000
        }

    }

//    fun swapScore(vScore: Int){
//
//    }

}