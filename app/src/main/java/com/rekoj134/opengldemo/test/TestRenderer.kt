package com.rekoj134.opengldemo.test

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.rekoj134.opengldemo.from_book.programs.ColorShaderProgram
import com.rekoj134.opengldemo.util.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val projectionMatrix by lazy { FloatArray(16) }
    private val modelMatrix by lazy { FloatArray(16) }
    private val viewMatrix by lazy { FloatArray(16) }
    private val viewProjectionMatrix by lazy { FloatArray(16) }
    private val modelViewProjectionMatrix by lazy { FloatArray(16) }

    private lateinit var colorProgram: ColorShaderProgram

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        colorProgram = ColorShaderProgram(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
        Matrix.setLookAtM(viewMatrix, 0,0f, 1.2f, 10f, 0f,0f, 0f, 0f, 1f, 0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        positionObjectInScene(0f, 0f, 0f)
        colorProgram.useProgram()
        colorProgram.setUniform(modelViewProjectionMatrix, 1f, 0f, 0f)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }
}