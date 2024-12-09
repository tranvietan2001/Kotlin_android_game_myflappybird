package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.mybird.FirebaseManager
import com.example.mybird.R

class Coin(
    resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
    private val playerMode: String
) : Sprite {

    private val coinImg:Bitmap = BitmapFactory.decodeResource(resources, getRetryButtonResource())

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
    private var vCoin: Int = 0

    private var score: Int = 0
    private var collision: Boolean = false

    override fun draw(canvas: Canvas) {

        if(playerMode == "offline"){
            updateCoinFromFB(2223)
        }
        else {
            val firebaseManager: FirebaseManager = FirebaseManager()
            firebaseManager.initFirebase()
            firebaseManager.getCoinGold { result ->
                val coin = result.toInt()
                updateCoinFromFB(coin)
            }
        }

        val scaledWidth = (coinImg.width * 0.075).toInt() // Thay đổi tỉ lệ theo ý muốn
        val scaledHeight = (coinImg.height * 0.075).toInt() // Thay đổi tỉ lệ theo ý muốn
        // Tính toán vị trí để căn giữa
        val left = (screenWidth / 3).toFloat()
        val top = (scaledHeight/2).toFloat()

        canvas.drawBitmap(
            Bitmap.createScaledBitmap(coinImg, scaledWidth, scaledHeight, true),
            left,
            top,
            null
        )
//// khi play se + score tang dan
//        if (!collision) {
//            val digits = convertToBitmaps(vCoin)
//            for (i in digits.indices) {
//                val x = screenWidth / 2 - digits.size * zero.width / 2 + zero.width * i
//                canvas.drawBitmap(digits[i], x.toFloat(), (screenHeight / 4).toFloat(), null)
//            }
//        }
//        // khi gameover se show scores
//        else {
            val currentDigits = convertToBitmaps(vCoin)
            for (i in currentDigits.indices) {
                val x = left + scaledWidth + 15 /*- currentDigits.size * zero.width / 2*/ + zero.width * i
                canvas.drawBitmap(
                    currentDigits[i],
                    x,
                    top,
                    null
                )
            }
//
//        }






    }

    override fun update() {


    }

//    fun updateCoin(coin: Int) {
//        this.vCoin = coin
//    }

    private fun updateCoinFromFB(coin: Int) {
        this.vCoin = coin
    }

    private fun getRetryButtonResource(): Int {
        return if (playerMode == "offline") {
            R.drawable.coin_silver
        } else {
            R.drawable.coin_gold
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
}