package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.Toast
import com.example.mybird.R
import org.intellij.lang.annotations.Language

class BackButton(
    resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
    private val language: String
) : Sprite {

    //    private val retryBtn: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.retry_button)
    private val backBtn: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.back_btn)
    override fun draw(canvas: Canvas) {
//        canvas.drawBitmap(retryBtn, (screenWidth / 2 - retryBtn.width / 2).toFloat(), (screenHeight / 4).toFloat(),null)

        val scaledWidth = (backBtn.width * 0.2).toInt() // Thay đổi tỉ lệ theo ý muốn
        val scaledHeight = (backBtn.height * 0.2).toInt() // Thay đổi tỉ lệ theo ý muốn

        // Tính toán vị trí để căn giữa
        val left = (scaledWidth/2).toFloat()
        val top = (scaledWidth/2).toFloat()

        canvas.drawBitmap(
            Bitmap.createScaledBitmap(backBtn, scaledWidth, scaledHeight, true),
            left,
            top,
            null
        )
    }

    override fun update() {
        // Không có logic cập nhật trong lớp GameMessage
    }

    fun isTouched(x: Float, y: Float): Boolean {
        val scaledWidth = (backBtn.width * 0.2).toInt() // Thay đổi tỉ lệ theo ý muốn
        val scaledHeight = (backBtn.height * 0.2).toInt() // Thay đổi tỉ lệ theo ý muốn

        // Tính toán vị trí để căn giữa
        val left = (scaledWidth/2).toFloat()
        val top = (scaledWidth/2).toFloat()

        return x >= left && x <= left + scaledWidth &&
                y >= top && y <= top + scaledHeight

    }
}