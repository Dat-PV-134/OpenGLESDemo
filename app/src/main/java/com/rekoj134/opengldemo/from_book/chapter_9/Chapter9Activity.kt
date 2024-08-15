package com.rekoj134.opengldemo.from_book.chapter_9

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.from_book.chapter_8.Chapter8Renderer

class Chapter9Activity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(this@Chapter9Activity)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        val myRenderer = Chapter9Renderer(this@Chapter9Activity)
        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(myRenderer)
            rendererSet = true
        } else {
            Toast.makeText(
                this@Chapter9Activity,
                "This device doesn't support OpenGL ES 2.0",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        glSurfaceView.setOnTouchListener { v, event ->
            event?.let {
                val normalizeX = (event.x / v.width) * 2 - 1
                val normalizeY = -(event.y / v.height) * 2 - 1

                if (it.action == MotionEvent.ACTION_DOWN) {
                    glSurfaceView.queueEvent { myRenderer.handleTouchPress(normalizeX, normalizeY) }
                } else if (it.action == MotionEvent.ACTION_MOVE) {
                    glSurfaceView.queueEvent { myRenderer.handleTouchDrag(normalizeX, normalizeY) }
                }
                true
            }
            false
        }

        setContentView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            glSurfaceView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            glSurfaceView.onPause()
        }
    }
}