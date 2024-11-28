package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.mybird.*

class Bird(
    resources: Resources,
    private val screenHeight: Int,
    private val callback: GameManagerCallback
) : Sprite{

    private val birdDown: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird1_down)
        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
            resources.getDimension(R.dimen.bird_height).toInt(), false) }

    private val birdUp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird1_up)
        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
            resources.getDimension(R.dimen.bird_height).toInt(), false) }



//    private var birdWith: Int = resources.getDimension(R.dimen.bird_width).toInt()
//    private var birdHeight: Int = resources.getDimension(R.dimen.bird_height).toInt()
//    private val birdBmpDown: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_down)
//    private val birdBmpUp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_up)
//    private val bird_down: Bitmap = Bitmap.createScaledBitmap(birdBmpDown,birdWith, birdHeight, false)
//    private val bird_up: Bitmap = Bitmap.createScaledBitmap(birdBmpUp,birdWith, birdHeight, false)

    private var birdX: Int = resources.getDimension(R.dimen.bird_x).toInt()
    private var birdY: Int = screenHeight / 2
    private var currentFallingSpeed: Float = 0f
    private var gravity: Float = resources.getDimension(R.dimen.gravity) // rơi nhanh(>) chậm m(<) (0.5)
    private var flappyBoost: Float = resources.getDimension(R.dimen.flappy_boost) // bay lên nhanh (>) chậm (>) (-9)
    internal var collision: Boolean = false




    override fun draw(canvas: Canvas) {
        val bitmapToDraw = if (currentFallingSpeed < 0) birdUp else birdDown
        canvas.drawBitmap(bitmapToDraw, birdX.toFloat(), birdY.toFloat(), null)

//        if(currentFallingSpeed < 0){
//            canvas.drawBitmap(bird_down,birdX.toFloat(), birdY.toFloat(),  null)
//        }
//        else  canvas.drawBitmap(bird_up,birdX.toFloat(), birdY.toFloat(), null)
    }

    override fun update() {
        if (collision) {
            if (birdY + birdDown.height < screenHeight) {
                birdY += currentFallingSpeed.toInt()
                currentFallingSpeed += gravity
            }
        } else {
            birdY += currentFallingSpeed.toInt()
            currentFallingSpeed += gravity
            val birdPosition = Rect(birdX, birdY, birdX + birdDown.width, birdY + birdDown.height /*-200*/)
            callback.updatePosition(birdPosition)
        }


//        birdY += currentFallingSpeed.toInt()
//        currentFallingSpeed += gravity
    }

    fun onTouchEvent(){
        if(!collision) {
            currentFallingSpeed = flappyBoost
        }
//        print("==========> y")
//        println(birdY.toInt())
    }

    fun collision(){
        collision = true
    }
}