package com.example.mybird

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.mybird.*
import com.example.mybird.sprites.*


class GameManager(context: Context, attrs: AttributeSet) : SurfaceView(context), SurfaceHolder.Callback, GameManagerCallback{
    private val thread: MainThread = MainThread(holder, this)

    private val APP_NAME = "MyBird"
    private var gameState: GameState = GameState.INITIAL
//    private var gameState: GameState = GameState.PLAYING


    private lateinit var bird: Bird
    private lateinit var background: Background

//    private  lateinit var obstacle: Obstacle // test vật cản

    private lateinit var dm: DisplayMetrics
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var gameOver: GameOver
    private lateinit var gameMessage: GameMessage
    private lateinit var scoreSprite: Score
    private var score: Int = 0
    private var birdPosition: Rect = Rect()
    private var obstaclePositions: HashMap<Obstacle, List<Rect>> = HashMap()

    private lateinit var mpPoint: MediaPlayer
    private lateinit var mpSwoosh: MediaPlayer
    private lateinit var mpDie: MediaPlayer
    private lateinit var mpHit: MediaPlayer
    private lateinit var mpWing: MediaPlayer


    init {
        initSounds()
        holder.addCallback(this)
        dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        initGame()
    }

    private fun initGame() {
        score = 0
        birdPosition = Rect()
        obstaclePositions = HashMap()
        bird = Bird(resources, dm.heightPixels, this)
        background = Background(resources, dm.heightPixels)

//        obstacle = Obstacle(resources,dm.heightPixels, dm.widthPixels) // test vật cản
        obstacleManager = ObstacleManager(resources, dm.heightPixels, dm.widthPixels, this) // quản lý việc vật cản xuất bện và duy chuyển

        gameOver = GameOver(resources, dm.heightPixels, dm.widthPixels)
        gameMessage = GameMessage(resources, dm.heightPixels, dm.widthPixels)
        scoreSprite = Score(resources, dm.heightPixels, dm.widthPixels)
    }

    private fun initSounds() {
        mpPoint = MediaPlayer.create(context, R.raw.point)
        mpSwoosh = MediaPlayer.create(context, R.raw.swoosh)
        mpDie = MediaPlayer.create(context, R.raw.die)
        mpHit = MediaPlayer.create(context, R.raw.hit)
        mpWing = MediaPlayer.create(context, R.raw.wing)
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

        when (gameState) {
            GameState.PLAYING -> {
                bird.update()
                obstacleManager.update()
            }
            GameState.GAME_OVER -> {
                bird.update()
            }
            else -> {}
        }


//        bird.update()
//        obstacleManager.update()
//        println("====================> GameManager Update --- AnTV test")
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)


        canvas.drawRGB(150, 255, 255)
        background.draw(canvas)

//        bird.draw(canvas)
////        obstacle.draw(canvas) // test
//        obstacleManager.draw(canvas)

        when (gameState) {
            GameState.PLAYING -> {
                obstacleManager.draw(canvas)
                bird.draw(canvas)
                scoreSprite.draw(canvas)
                calculateCollision()
            }

            GameState.INITIAL -> {
                bird.draw(canvas)
                gameMessage.draw(canvas)
            }
            GameState.GAME_OVER -> {
                obstacleManager.draw(canvas)
                bird.draw(canvas)
                gameOver.draw(canvas)
                scoreSprite.draw(canvas)
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        bird.onTouchEvent()

        when (gameState) {
            GameState.PLAYING -> {
                bird.onTouchEvent()
                mpWing.start()
            }
            GameState.INITIAL -> {
//                initGame()
//                bird.onTouchEvent()
                mpWing.start()
                gameState = GameState.PLAYING
                mpSwoosh.start()
            }
            GameState.GAME_OVER -> {
//                initGame()
//                gameState = GameState.INITIAL

                // game over thì có nút replay khi click vào đó thì mới init v game states
            }
        }
//        bird.onTouchEvent()
        return super.onTouchEvent(event)
    }

    override fun updatePosition(birdPosition: Rect) {
        this.birdPosition = birdPosition
    }

    override fun updatePosition(obstacle: Obstacle, positions: ArrayList<Rect>) {
//        if (obstaclePositions.containsKey(obstacle)) {
//            obstaclePositions.remove(obstacle)
//        }
        obstaclePositions.remove(obstacle)
        obstaclePositions[obstacle] = positions
    }

    override fun removeObstacle(obstacle: Obstacle) {
        obstaclePositions.remove(obstacle)
//        score +=10000
        score++
        scoreSprite.updateScore(score)
        mpPoint.start()
    }

    fun calculateCollision() {
        var collision = false
        if (birdPosition.bottom > dm.heightPixels) {
            collision = true
        } else {
            println("=========================================>xxxxx$obstaclePositions")
            for (obstacle in obstaclePositions.keys) {
                println("=========================================>yyyy")
                val bottomRectangle = obstaclePositions[obstacle]!![0]
                val topRectangle = obstaclePositions[obstacle]!![1]
                println("=========================================>")
                println("=========================================>OB: ${bottomRectangle.left}==${topRectangle.left}")
                println("=========================================>Bird: $birdPosition.right ")
                if (birdPosition.right > bottomRectangle.left && birdPosition.left < bottomRectangle.right && birdPosition.bottom > bottomRectangle.top) {
                    collision = true
                } else if (birdPosition.right > topRectangle.left && birdPosition.left < topRectangle.right && birdPosition.top < topRectangle.bottom) {
                    collision = true
                }
            }
        }

        if (collision) {
            gameState = GameState.GAME_OVER
            bird.collision()
            println("=========================================> GAME OVER")
            scoreSprite.collision(context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE))
            mpHit.start()
            mpHit.setOnCompletionListener(OnCompletionListener { mpDie.start() })
        }
    }
}