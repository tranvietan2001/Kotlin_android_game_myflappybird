package com.example.mybird.sprites

import android.graphics.Rect

interface ObstacleCallback {
    fun obstacleOffScreen(obstacle: Obstacle)
//    fun updatePosition(obstacle: Obstacle, positions: ArrayList<Rect>)
}