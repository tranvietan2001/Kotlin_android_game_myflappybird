package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.Toast
import com.example.mybird.R

class RetryButton(
    resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int
) : Sprite {

    private val retryBtn: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.retry_button)

    override fun draw(canvas: Canvas) {
//        canvas.drawBitmap(retryBtn, (screenWidth / 2 - retryBtn.width / 2).toFloat(), (screenHeight / 4).toFloat(),null)

        val scaledWidth = (retryBtn.width * 0.35).toInt() // Thay đổi tỉ lệ theo ý muốn
        val scaledHeight = (retryBtn.height * 0.35).toInt() // Thay đổi tỉ lệ theo ý muốn

        // Tính toán vị trí để căn giữa
        val left = (screenWidth / 2 - scaledWidth / 2).toFloat()
        val top = (screenHeight - 2 * scaledHeight).toFloat()

        canvas.drawBitmap(
            Bitmap.createScaledBitmap(retryBtn, scaledWidth, scaledHeight, true),
            left,
            top,
            null
        )
    }

    override fun update() {
        // Không có logic cập nhật trong lớp GameMessage
    }

    fun onTouchEvent() {
        println("TOUCH --->")
    }

    fun isTouched(x: Float, y: Float): Boolean {
        val scaledWidth = (retryBtn.width * 0.35).toInt() // Thay đổi tỉ lệ theo ý muốn
        val scaledHeight = (retryBtn.height * 0.35).toInt() // Thay đổi tỉ lệ theo ý muốn

        // Tính toán vị trí để căn giữa
        val left = (screenWidth / 2 - scaledWidth / 2).toFloat()
        val top = (screenHeight - 2 * scaledHeight).toFloat()

        return x >= left && x <= left + scaledWidth &&
                y >= top && y <= top + scaledHeight

    }
}