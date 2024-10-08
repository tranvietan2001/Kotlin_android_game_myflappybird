package com.example.mybird.sprites

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.mybird.R

class Bird(
    resources: Resources,
    private val screenHeight: Int,
//    private val callback: GameManagerCallback
) : Sprite{

//    private val bird: Bitmap
//        get() {
//            TODO()
//        }
//
//    @SuppressLint("ResourceType")
//    private var birdX: Int = resources.getDimension(bird_level).toInt()


    private val birdDown: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_down)
        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
            resources.getDimension(R.dimen.bird_height).toInt(), false) }

    private val birdUp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_up)
        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
            resources.getDimension(R.dimen.bird_height).toInt(), false) }

    private var birdX: Int = resources.getDimension(R.dimen.bird_x).toInt()
    private var birdY: Int = screenHeight / 2
    private var currentFallingSpeed: Float = 0f
    private val gravity: Float = resources.getDimension(R.dimen.gravity)
    private val flappyBoost: Float = resources.getDimension(R.dimen.flappy_boost)
    private var collision: Boolean = false




    override fun draw(canvas: Canvas) {
        val bitmapToDraw = if (currentFallingSpeed < 0) birdUp else birdDown
        canvas.drawBitmap(bitmapToDraw, birdX.toFloat(), birdY.toFloat(), null)
    }

    override fun update() {
        TODO("Not yet implemented")
    }
}