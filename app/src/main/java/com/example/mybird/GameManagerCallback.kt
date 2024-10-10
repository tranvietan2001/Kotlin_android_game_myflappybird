package com.example.mybird

import android.graphics.Rect
import com.example.mybird.sprites.Obstacle

interface GameManagerCallback {
    fun updatePosition(birdPosition: Rect)
    fun updatePosition(obstacle: Obstacle, positions: ArrayList<Rect>)
    fun removeObstacle(obstacle: Obstacle)
}