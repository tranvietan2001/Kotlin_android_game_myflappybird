package com.example.mybird

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

public class GameManager(context: Context):SurfaceView(context), SurfaceHolder.Callback{

    // Xử lý khi surface được tạo
    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    // Xử lý khi surface thay đổi
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    // Xử lý khi surface bị hủy
    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }
}