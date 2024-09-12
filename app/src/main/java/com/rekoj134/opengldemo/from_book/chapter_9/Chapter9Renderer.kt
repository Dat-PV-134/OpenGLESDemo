package com.rekoj134.opengldemo.from_book.chapter_9

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.invertM
import android.opengl.Matrix.multiplyMV
import android.util.Log
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.from_book.objects.Mallet
import com.rekoj134.opengldemo.from_book.objects.Puck
import com.rekoj134.opengldemo.from_book.objects.Table
import com.rekoj134.opengldemo.from_book.programs.ColorShaderProgram
import com.rekoj134.opengldemo.from_book.programs.TextureShaderProgram
import com.rekoj134.opengldemo.util.Geometry
import com.rekoj134.opengldemo.util.Geometry.intersectionPoint
import com.rekoj134.opengldemo.util.MatrixHelper
import com.rekoj134.opengldemo.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Chapter9Renderer(private val context: Context) : GLSurfaceView.Renderer {
    companion object {
        private const val BYTES_PER_FLOAT = 4
        private const val A_POSITION = "a_Position"
        private const val A_COLOR = "a_Color"
        private const val U_MATRIX = "u_Matrix"
    }

    private val projectionMatrix by lazy { FloatArray(16) }
    private val modelMatrix by lazy { FloatArray(16) }
    private val viewMatrix by lazy { FloatArray(16) }
    private val viewProjectionMatrix by lazy { FloatArray(16) }
    private val modelViewProjectionMatrix by lazy { FloatArray(16) }
    private val invertedViewProjectionMatrix by lazy { FloatArray(16) }
    private val table by lazy { Table() }
    private lateinit var puck: Puck
    private lateinit var mallet: Mallet
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture: Int = 0

    private var malletPressed = false
    private lateinit var blueMalletPosition: Geometry.Point

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)

        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)

        blueMalletPosition = Geometry.Point(-0.15f, mallet.height / 2f, 0.4f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
        Matrix.setLookAtM(viewMatrix, 0,0f, 1.2f, 3f, 0f,0f, 0f, 0f, 1f, 0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)

        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniform(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniform(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z)
        colorProgram.setUniform(modelViewProjectionMatrix, 0f, 0f, 1f)
        mallet.bindData(colorProgram)
        mallet.draw()
    }

    private fun positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    fun handleTouchPress(normalizeX: Float, normalizeY: Float) {
        val ray = convertNormalized2DPointToRay(normalizeX, normalizeY)
        val malletBoundingSphere = Geometry.Sphere(
            Geometry.Point(
                blueMalletPosition.x,
                blueMalletPosition.y,
                blueMalletPosition.z
            ), mallet.height / 2f
        )
        malletPressed = Geometry.intersects(malletBoundingSphere, ray)
    }

    fun handleTouchDrag(normalizeX: Float, normalizeY: Float) {
        if (malletPressed) {
            val ray = convertNormalized2DPointToRay(normalizeX, normalizeY)
            val plane = Geometry.Plane(Geometry.Point(0f, 0f, 0f), Geometry.Vector(0f, 1f, 0f))
            val touchedPoint = intersectionPoint(ray, plane)
            blueMalletPosition = Geometry.Point(touchedPoint.x, mallet.height / 2f, touchedPoint.z)
        }
    }

    private fun convertNormalized2DPointToRay(normalizeX: Float, normalizeY: Float) : Geometry.Ray {
        val nearPointNdc = floatArrayOf(normalizeX, normalizeY, -1f, 1f)
        val farPointNdc = floatArrayOf(normalizeX, normalizeY, 1f, 1f)

        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0)
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0)

        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        val nearPointRay = Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])

        return Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay))
    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }
}