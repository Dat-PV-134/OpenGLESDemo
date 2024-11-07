package com.rekoj134.opengldemo.from_book.first_lesson

import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FistLessonRenderer : Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(1.0f, 1.0f, 0f, 0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
    }
}