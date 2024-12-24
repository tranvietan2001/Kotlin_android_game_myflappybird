package com.example.mybird

import android.graphics.Canvas
import android.view.SurfaceHolder

class MainThread (
    private val surfaceHolder: SurfaceHolder,
    private val gameManager: GameManager
) : Thread(){

    private var running: Boolean = false
    private var canvas: Canvas? = null
    private val targetFPS: Int = 90

    override fun run() {
        val targetTime = 1000 / targetFPS
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long

        while (running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameManager.update()
                    canvas?.let {
                        gameManager.draw(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                canvas?.let {
                    try {
                        surfaceHolder.unlockCanvasAndPost(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime > 0) {
                try {
                    sleep(waitTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
}