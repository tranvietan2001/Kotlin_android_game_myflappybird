package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.example.mybird.R
import java.util.*

class Obstacle(
    resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
//    private val callback: ObstacleCallback
) : Sprite {

    private val image: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pipes)
    private var height: Int
    private var width: Int = resources.getDimension(R.dimen.obstacle_width).toInt()
    private var separation: Int = resources.getDimension(R.dimen.obstacle_separation).toInt()
    private var xPosition: Int = /*screenWidth*/ 300
    private var speed: Int = resources.getDimension(R.dimen.obstacle_speed).toInt()
    private var headHeight: Int = resources.getDimension(R.dimen.head_height).toInt()
    private var headExtraWidth: Int = resources.getDimension(R.dimen.head_extra_width).toInt()
    private var obstacleMinPosition: Int = resources.getDimension(R.dimen.obstacle_min_position).toInt()

    init {
        val random = Random(System.currentTimeMillis())
        height = random.nextInt(screenHeight - 2 * obstacleMinPosition - separation) + obstacleMinPosition
    }

    override fun draw(canvas: Canvas) {

        val bottomPipe = Rect(xPosition + headExtraWidth, screenHeight - height, xPosition + width + headExtraWidth, screenHeight + 200 /**/) // bệ ngang dưới
        val bottomHead = Rect(xPosition, screenHeight - height - headHeight, xPosition + width + 2 * headExtraWidth, screenHeight - height)
        val topPipe = Rect(xPosition + headExtraWidth, 0, xPosition + headExtraWidth + width, screenHeight - height - separation - 2 * headHeight) // bệ ngang trên
        val topHead = Rect(xPosition, screenHeight - height - separation - 2 * headHeight, xPosition + width + 2 * headExtraWidth, screenHeight - height - separation - headHeight)

        val paint = Paint()
        canvas.drawBitmap(image, null, bottomPipe, paint)
        canvas.drawBitmap(image, null, bottomHead, paint)
        canvas.drawBitmap(image, null, topPipe, paint)
        canvas.drawBitmap(image, null, topHead, paint)
    }

    override fun update() {
//        xPosition -= speed
//        if (xPosition <= 0 - width - 2 * headExtraWidth) {
////            callback.obstacleOffScreen(this)
//        } else {
//            val positions = ArrayList<Rect>()
//            val bottomPosition = Rect(xPosition, screenHeight - height - headHeight, xPosition + width + 2 * headExtraWidth, screenHeight)
//            val topPosition = Rect(xPosition, 0, xPosition + width + 2 * headExtraWidth, screenHeight - height - headHeight - separation)
//
//            positions.add(bottomPosition)
//            positions.add(topPosition)
////            callback.updatePosition(this, positions)
//        }
    }
}