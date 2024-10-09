package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.mybird.R

class Background(
    resources: Resources,
    private val screenHeight: Int
) : Sprite {

    private val top: Bitmap
    private val bottom: Bitmap
    private val topHeight: Int = resources.getDimension(R.dimen.bkg_top_height).toInt()         // chiều dài image bg sky
    private val bottomHeight: Int = resources.getDimension(R.dimen.bkg_bottom_height).toInt()   // chiều dài image bg ground

    init {
        val bkgTop = BitmapFactory.decodeResource(resources, R.drawable.sky)
        val bkgBottom = BitmapFactory.decodeResource(resources, R.drawable.ground)
        top = Bitmap.createScaledBitmap(bkgTop, bkgTop.width, topHeight, false)
        bottom = Bitmap.createScaledBitmap(bkgBottom, bkgBottom.width, bottomHeight, false)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(top, 0f, 0f, null)
//        canvas.drawBitmap(top, top.width.toFloat(), 0f, null)
        canvas.drawBitmap(bottom, 0f, (screenHeight - bottom.height).toFloat() + 200, null)
//        canvas.drawBitmap(bottom, bottom.width.toFloat(), (screenHeight - bottom.height).toFloat(), null)
    }

    override fun update() {
        // Không có logic cập nhật trong lớp Background
    }
}