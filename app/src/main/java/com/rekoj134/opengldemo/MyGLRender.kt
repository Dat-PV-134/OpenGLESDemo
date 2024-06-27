package com.rekoj134.opengldemo

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRender : GLSurfaceView.Renderer {
    var colorRed = 0f
    var colorGreen = 1.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(colorRed, colorGreen, 0.0f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(colorRed, colorGreen, 0.0f, 1.0f)
        gl?.glClear(GL10.GL_COLOR_BUFFER_BIT)
    }

    fun update() {
        colorRed = 1f
        colorGreen = 0f
    }
}