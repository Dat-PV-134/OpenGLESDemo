package com.rekoj134.opengldemo.from_book.chapter_8

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Chapter8Activity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(this@Chapter8Activity)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(Chapter8Renderer(this@Chapter8Activity))
            rendererSet = true
        } else {
            Toast.makeText(this@Chapter8Activity, "This device doesn't support OpenGL ES 2.0", Toast.LENGTH_SHORT).show()
            return
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
