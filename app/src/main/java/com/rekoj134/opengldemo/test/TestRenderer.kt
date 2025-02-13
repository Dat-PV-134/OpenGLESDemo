package com.rekoj134.opengldemo.test

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import com.rekoj134.opengldemo.from_book.objects.RayShooter
import com.rekoj134.opengldemo.from_book.objects.RaySystem
import com.rekoj134.opengldemo.from_book.programs.RayShaderProgram
import com.rekoj134.opengldemo.util.Geometry
import com.rekoj134.opengldemo.util.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var rayProgram: RayShaderProgram
    private lateinit var raySystem: RaySystem
    private lateinit var rayShooter: RayShooter

    private var globalStartTime = 0L
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        // Initialize ray shader program and systems
        rayProgram = RayShaderProgram(context)
        raySystem = RaySystem(4000)  // max 1000 rays
        rayShooter = RayShooter(Geometry.Point(0f, 0.5f, 0f), Geometry.Vector(0.0f, 1.0f, 0.0f), Color.RED)

        globalStartTime = System.nanoTime()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE)

        // Set up projection and view matrix
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat()/height, 1f, 10f)
        setIdentityM(viewMatrix, 0)
        translateM(viewMatrix, 0, 0f, -3.9f, -10f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        rayShooter.addRays(raySystem, 10)  // Shoot 1 ray per frame

        // Use the ray shader program and set the uniforms
        rayProgram.useProgram()

        // Set the transformation matrix
        rayProgram.setUniforms(viewProjectionMatrix)

        // Bind data for ray system
        raySystem.bindData(rayProgram)

        // Draw the rays (using GL_LINES to render as lines)
        raySystem.draw()
    }
}