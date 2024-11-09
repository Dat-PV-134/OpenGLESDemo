package com.rekoj134.opengldemo.from_book.chapter_10

import android.content.Context
import android.graphics.Color
import android.opengl.GLES10.GL_BLEND
import android.opengl.GLES10.GL_ONE
import android.opengl.GLES10.glBlendFunc
import android.opengl.GLES10.glEnable
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.from_book.objects.ParticleShooter
import com.rekoj134.opengldemo.from_book.objects.ParticleSystem
import com.rekoj134.opengldemo.from_book.programs.ParticleShaderProgram
import com.rekoj134.opengldemo.util.Geometry
import com.rekoj134.opengldemo.util.MatrixHelper
import com.rekoj134.opengldemo.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Chapter10Renderer(val context: Context) : GLSurfaceView.Renderer {
    private lateinit var particleProgram: ParticleShaderProgram
    private lateinit var particleSystem: ParticleSystem
    private lateinit var redParticleShooter: ParticleShooter
    private lateinit var greenParticleShooter: ParticleShooter
    private lateinit var blueParticleShooter: ParticleShooter
    private var globalStartTime = 0L

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private var angleVarianceInDegree = 5f
    private var speedVariance = 10f
    private var texture = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        particleProgram = ParticleShaderProgram(context)
        particleSystem = ParticleSystem(3000)
        globalStartTime = System.nanoTime()

        val particleDirection = Geometry.Vector(0f, 0.5f, 0f)

        redParticleShooter = ParticleShooter(
            Geometry.Point(-1.5f, 0f, 0f),
            particleDirection,
            Color.rgb(255, 50, 5),
            angleVarianceInDegree,
            speedVariance
        )

        greenParticleShooter = ParticleShooter(
            Geometry.Point(0f, 0f, 0f),
            particleDirection,
            Color.rgb(25, 255, 25),
            angleVarianceInDegree,
            speedVariance
        )

        blueParticleShooter = ParticleShooter(
            Geometry.Point(1.5f, 0f, 0f),
            particleDirection,
            Color.rgb(5, 50, 255),
            angleVarianceInDegree,
            speedVariance
        )

        texture = TextureHelper.loadTexture(context, R.drawable.partical_texture)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)

        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat()/height, 1f, 10f)

        setIdentityM(viewMatrix, 0)
        translateM(viewMatrix, 0, 0f, -3.9f, -10f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter.addParticles(particleSystem, currentTime, 5)
        greenParticleShooter.addParticles(particleSystem, currentTime, 5)
        blueParticleShooter.addParticles(particleSystem, currentTime, 5)

        particleProgram.useProgram()
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()
    }
}