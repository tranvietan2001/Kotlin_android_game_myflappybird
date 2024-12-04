package com.example.mybird

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.example.mybird.*
import com.example.mybird.sprites.*


class GameManager(
    context: Context,
    attrs: AttributeSet
) : SurfaceView(context, attrs),
    SurfaceHolder.Callback, GameManagerCallback {
    private val thread: MainThread = MainThread(holder, this)

    private val APP_NAME = "@MY_BIRD"
    private var gameState: GameState = GameState.INITIAL

    private lateinit var sharedPrefManager: SharedPreferenceManager

    private lateinit var bird: Bird
    private lateinit var background: Background

//    private  lateinit var obstacle: Obstacle // test vật cản

    private lateinit var dm: DisplayMetrics
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var gameOver: GameOver
    private lateinit var gameMessage: GameMessage
    private lateinit var scoreSprite: Score

    private lateinit var retryBtn: RetryButton
    private lateinit var backBtn: BackButton

    private var score: Int = 0
    private var birdPosition: Rect = Rect()
    private var obstaclePositions: HashMap<Obstacle, List<Rect>> = HashMap()

    private lateinit var mpPoint: MediaPlayer
    private lateinit var mpSwoosh: MediaPlayer
    private lateinit var mpDie: MediaPlayer
    private lateinit var mpHit: MediaPlayer
    private lateinit var mpWing: MediaPlayer

    private var isPlaying: Boolean = false

    var playerMode = ""
    var soundStt = false
    var language = "en"

    init {

        holder.addCallback(this)
        dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        initGame()
        initSounds()
    }

    private fun initGame() {
        score = 0
        birdPosition = Rect()
        obstaclePositions = HashMap()
        bird = Bird(resources, dm.heightPixels, this)
        background = Background(resources, dm.heightPixels)

//        obstacle = Obstacle(resources,dm.heightPixels, dm.widthPixels) // test vật cản
        obstacleManager = ObstacleManager(
            resources,
            dm.heightPixels,
            dm.widthPixels,
            this
        ) // quản lý việc vật cản xuất bện và duy chuyển

        sharedPrefManager = SharedPreferenceManager(context)
        playerMode = sharedPrefManager.getPlayerMode()
        soundStt = sharedPrefManager.getStatusSoundConfig()
        language = sharedPrefManager.getLanguageConfig()

        gameOver = GameOver(resources, dm.heightPixels, dm.widthPixels)
        gameMessage = GameMessage(resources, dm.heightPixels, dm.widthPixels, language)
        scoreSprite = Score(resources, dm.heightPixels, dm.widthPixels)
        retryBtn = RetryButton(resources, dm.heightPixels, dm.widthPixels, language)
        backBtn = BackButton(resources, dm.heightPixels, dm.widthPixels, language)
    }

    private fun initSounds() {
        if(soundStt) {
            mpPoint = MediaPlayer.create(context, R.raw.point)
            mpSwoosh = MediaPlayer.create(context, R.raw.swoosh)
            mpDie = MediaPlayer.create(context, R.raw.die)
            mpHit = MediaPlayer.create(context, R.raw.hit)
            mpWing = MediaPlayer.create(context, R.raw.wing)
        }
        else{
            mpPoint = MediaPlayer.create(context, R.raw.mute)
            mpSwoosh = MediaPlayer.create(context, R.raw.mute)
            mpDie = MediaPlayer.create(context, R.raw.mute)
            mpHit = MediaPlayer.create(context, R.raw.mute)
            mpWing = MediaPlayer.create(context, R.raw.mute)
        }
    }


    // Xử lý khi surface được tạo
    override fun surfaceCreated(holder: SurfaceHolder) {
        isPlaying = true
        thread.setRunning(true)
        thread.start()
    }

    // Xử lý khi surface thay đổi
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    // Xử lý khi surface bị hủy
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isPlaying = true
//        thread.setRunning(isPlaying)
        while (isPlaying) {
            try {
//                thread.join()
                isPlaying = false
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
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawRGB(150, 255, 255)
        background.draw(canvas)

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

                retryBtn.draw(canvas)
                backBtn.draw(canvas)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (gameState) {
            GameState.PLAYING -> {
                bird.onTouchEvent()
                mpWing.start()
            }
            GameState.INITIAL -> {
                if (event != null) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val x = event.x
                        val y = event.y
                        if (gameMessage.isTouched(x, y)) {
                            mpWing.start()
                            gameState = GameState.PLAYING
                            mpSwoosh.start()
                        }
                    }
                }
            }

            GameState.GAME_OVER -> {

                // game over thì có nút replay khi click vào đó thì mới init v game states
                // vex nút retry, neu touch ddungs vaof vij tris nuts thif init vaf cguyeenr tranjg nthais

                if (event != null) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val x = event.x
                        val y = event.y
                        if (retryBtn.isTouched(x, y)) {
//                            println("TOUCH -- $x --$y")
                            initGame()
                            gameState = GameState.INITIAL
                        }

                        if(backBtn.isTouched(x,y)){
                            while (isPlaying) {
                                try {
//                                    thread.join()
                                    isPlaying = false
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                            }
                            gotoBack()
                            println("------> BACK")
                        }
                    }
                }

            }
        }
        return super.onTouchEvent(event)
    }

    override fun updatePosition(birdPosition: Rect) {
        this.birdPosition = birdPosition
    }

    override fun updatePosition(obstacle: Obstacle, positions: ArrayList<Rect>) {
        obstaclePositions.remove(obstacle)
        obstaclePositions[obstacle] = positions
    }

    override fun removeObstacle(obstacle: Obstacle) {
        obstaclePositions.remove(obstacle)
        score += 5
//        score++
        scoreSprite.updateScore(score)
        mpPoint.start()
    }

    fun calculateCollision() {
        var collision = false
        if (birdPosition.bottom > dm.heightPixels) {
            collision = true
        } else {
            for (obstacle in obstaclePositions.keys) {
                val bottomRectangle = obstaclePositions[obstacle]!![0]
                val topRectangle = obstaclePositions[obstacle]!![1]
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
            scoreSprite.collision(context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE))
            mpHit.start()
            mpHit.setOnCompletionListener(OnCompletionListener { mpDie.start() })
        }
    }

    private fun gotoBack(){
        if(playerMode == "online"){
            val intent = Intent(context, InforAfterLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        else {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }
}