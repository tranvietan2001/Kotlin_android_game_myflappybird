package com.example.mybird.sprites

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.mybird.*

class Bird(
    resources: Resources,
    private val screenHeight: Int,
    private val callback: GameManagerCallback,
    private val playerMode: String,
    private val context: Context
) : Sprite {

    private var birdDown: Bitmap? = null
    private var birdUp: Bitmap? = null

    init {
        getBirdDownResource { result ->
            birdDown = BitmapFactory.decodeResource(resources, result)
                .let {
                    Bitmap.createScaledBitmap(
                        it,
                        resources.getDimension(R.dimen.bird_width).toInt(),
                        resources.getDimension(R.dimen.bird_height).toInt(),
                        false
                    )
                }
        }
        getBirdUpResource { result ->
            birdUp = BitmapFactory.decodeResource(resources, result)
                .let {
                    Bitmap.createScaledBitmap(
                        it,
                        resources.getDimension(R.dimen.bird_width).toInt(),
                        resources.getDimension(R.dimen.bird_height).toInt(),
                        false
                    )
                }

        }
    }

//   birdDown: Bitmap = BitmapFactory.decodeResource(resources, /*R.drawable.bird1_down*/ getBirtDownResource())
//        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
//            resources.getDimension(R.dimen.bird_height).toInt(), false) }
//
//    birdUp: Bitmap = BitmapFactory.decodeResource(resources, /*R.drawable.bird1_up*/ getBirtUpResource())
//        .let { Bitmap.createScaledBitmap(it, resources.getDimension(R.dimen.bird_width).toInt(),
//            resources.getDimension(R.dimen.bird_height).toInt(), false) }


//    private var birdWith: Int = resources.getDimension(R.dimen.bird_width).toInt()
//    private var birdHeight: Int = resources.getDimension(R.dimen.bird_height).toInt()
//    private val birdBmpDown: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_down)
//    private val birdBmpUp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_up)
//    private val bird_down: Bitmap = Bitmap.createScaledBitmap(birdBmpDown,birdWith, birdHeight, false)
//    private val bird_up: Bitmap = Bitmap.createScaledBitmap(birdBmpUp,birdWith, birdHeight, false)

    private var birdX: Int = resources.getDimension(R.dimen.bird_x).toInt()
    private var birdY: Int = screenHeight / 2
    private var currentFallingSpeed: Float = 0f
    private var gravity: Float =
        resources.getDimension(R.dimen.gravity) // rơi nhanh(>) chậm m(<) (0.5)
    private var flappyBoost: Float =
        resources.getDimension(R.dimen.flappy_boost) // bay lên nhanh (>) chậm (>) (-9)
    internal var collision: Boolean = false

    private var birdUsed: String = "bird1_down"


    override fun draw(canvas: Canvas) {
        val bitmapToDraw = if (currentFallingSpeed < 0) birdUp else birdDown
//        canvas.drawBitmap(bitmapToDraw, birdX.toFloat(), birdY.toFloat(), null)

        // Kiểm tra bitmapToDraw trước khi vẽ
        bitmapToDraw?.let {
            canvas.drawBitmap(it, birdX.toFloat(), birdY.toFloat(), null)
        }
    }

    override fun update() {
        if (collision) {
            if (birdY + birdDown!!.height < screenHeight) {
                birdY += currentFallingSpeed.toInt()
                currentFallingSpeed += gravity
            }
        } else {
            birdY += currentFallingSpeed.toInt()
            currentFallingSpeed += gravity
            val birdPosition =
                Rect(birdX, birdY, birdX + birdDown!!.width, birdY + birdDown!!.height /*-200*/)
            callback.updatePosition(birdPosition)
        }
    }

    fun onTouchEvent() {
        if (!collision) {
            currentFallingSpeed = flappyBoost
        }
//        print("==========> y")
//        println(birdY.toInt())
    }

    fun collision() {
        collision = true
    }


    private fun birdUsed(callback: (String) -> Unit) {
        if (playerMode == "online") {
            val firebaseManager: FirebaseManager = FirebaseManager()
            firebaseManager.initFirebase()
            firebaseManager.getBirdUsed { nameBird ->
//
                callback(nameBird)
            }
        } else {
            val sharedPrefManager = SharedPreferenceManager(context)
            val b = sharedPrefManager.getBirdUsed()
//            setBirdUsed(b)
            callback(b)
        }
    }

    private fun setBirdUsed(nameBird: String) {
        this.birdUsed = nameBird
    }

    fun getBirdDownResource(callback: (Int) -> Unit) {
        birdUsed { nameBird ->
            val resourceId = when (nameBird) {
                "bird2_down" -> R.drawable.bird2_down
                "bird3_down" -> R.drawable.bird3_down
                "bird4_down" -> R.drawable.bird4_down
                "bird5_down" -> R.drawable.bird5_down
                else -> R.drawable.bird1_down
            }
            callback(resourceId)
        }
    }

    fun getBirdUpResource(callback: (Int) -> Unit) {
        birdUsed { nameBird ->
            val resourceId = when (nameBird) {
                "bird2_down" -> R.drawable.bird2_up
                "bird3_down" -> R.drawable.bird3_up
                "bird4_down" -> R.drawable.bird4_up
                "bird5_down" -> R.drawable.bird5_up
                else -> R.drawable.bird1_up
            }
            callback(resourceId)
        }
    }

}