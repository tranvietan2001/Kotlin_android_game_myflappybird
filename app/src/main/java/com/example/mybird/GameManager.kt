package com.example.mybird

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.example.mybird.sprites.*

class GameManager(context: Context, attrs: AttributeSet) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: MainThread = MainThread(holder, this)

//    private val APP_NAME = "FlappyBirdClone"
//    private var gameState: GameState = GameState.INITIAL
//
    private lateinit var bird: Bird
//    private lateinit var background: Background
    private lateinit var dm: DisplayMetrics
//    private lateinit var obstacleManager: ObstacleManager
//    private lateinit var gameOver: GameOver
//    private lateinit var gameMessage: GameMessage
//    private lateinit var scoreSprite: Score
//    private var score: Int = 0
//    private var birdPosition: Rect = Rect()
//    private var obstaclePositions: HashMap<Obstacle, List<Rect>> = HashMap()
//
//    private lateinit var mpPoint: MediaPlayer
//    private lateinit var mpSwoosh: MediaPlayer
//    private lateinit var mpDie: MediaPlayer
//    private lateinit var mpHit: MediaPlayer
//    private lateinit var mpWing: MediaPlayer


    init {
        holder.addCallback(this)
        dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        initGame()
    }

    private fun initGame() {
//        score = 0
//        birdPosition = Rect()
//        obstaclePositions = HashMap()
        bird = Bird(resources, dm.heightPixels)
//        bird = Bird(resources, dm.heightPixels, this)
//        background = Background(resources, dm.heightPixels)
//        obstacleManager = ObstacleManager(resources, dm.heightPixels, dm.widthPixels, this)
//        gameOver = GameOver(resources, dm.heightPixels, dm.widthPixels)
//        gameMessage = GameMessage(resources, dm.heightPixels, dm.widthPixels)
//        scoreSprite = Score(resources, dm.heightPixels, dm.widthPixels)
    }

    private fun initSounds() {
//        mpPoint = MediaPlayer.create(context, R.raw.point)
//        mpSwoosh = MediaPlayer.create(context, R.raw.swoosh)
//        mpDie = MediaPlayer.create(context, R.raw.die)
//        mpHit = MediaPlayer.create(context, R.raw.hit)
//        mpWing = MediaPlayer.create(context, R.raw.wing)
    }




    // Xử lý khi surface được tạo
    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    // Xử lý khi surface thay đổi
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    // Xử lý khi surface bị hủy
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        bird.update()
//        println("====================> GameManager Update --- AnTV test")
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        bird.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        bird.onTouchEvent()
        return super.onTouchEvent(event)
    }
}