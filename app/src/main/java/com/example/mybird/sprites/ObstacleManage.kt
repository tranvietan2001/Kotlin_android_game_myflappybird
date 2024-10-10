package com.example.mybird.sprites

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
//import com.example.mybird.GameManagerCallback
import com.example.mybird.R

class ObstacleManager(
    private val resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
//    private val callback: GameManagerCallback
) /*: ObstacleCallback*/ {

    private var interval: Int = resources.getDimension(R.dimen.obstacle_interval).toInt()
//    private var speed: Int = resources.getDimension(R.dimen.obstacle_speed).toInt()
//    private var progress: Int = 0
    private val obstacles: MutableList<Obstacle> = mutableListOf(Obstacle(resources, screenHeight, screenWidth))

    fun update() {
//        progress += speed
//        if (progress > interval) {
//            progress = 0
//            obstacles.add(Obstacle(resources, screenHeight, screenWidth))
//        }
//        val duplicate = ArrayList(obstacles) // Tạo một bản sao của danh sách
//        for (obstacle in duplicate) {
//            obstacle.update()
//        }


        for (obstacle in obstacles) {
            obstacle.update()
        }

    }

    fun draw(canvas: Canvas) {
        for (obstacle in obstacles) {
            obstacle.draw(canvas)
        }
    }

//    override fun obstacleOffScreen(obstacle: Obstacle) {
//        obstacles.remove(obstacle)
//        callback.removeObstacle(obstacle)
//    }
//
//    override fun updatePosition(obstacle: Obstacle, positions: ArrayList<Rect>) {
//        callback.updatePosition(obstacle, positions)
//    }
}