package com.rekoj134.opengldemo.from_book.chapter_7

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.from_book.objects.Mallet
import com.rekoj134.opengldemo.from_book.objects.Table
import com.rekoj134.opengldemo.from_book.programs.ColorShaderProgram
import com.rekoj134.opengldemo.from_book.programs.TextureShaderProgram
import com.rekoj134.opengldemo.util.LoggerConfig
import com.rekoj134.opengldemo.util.MatrixHelper
import com.rekoj134.opengldemo.util.ShaderHelper
import com.rekoj134.opengldemo.util.TextResourceReader
import com.rekoj134.opengldemo.util.TextureHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Chapter7Renderer(private val context: Context) : GLSurfaceView.Renderer {
    companion object {
        private const val BYTES_PER_FLOAT = 4
        private const val A_POSITION = "a_Position"
        private const val A_COLOR = "a_Color"
        private const val U_MATRIX = "u_Matrix"
    }

    private val projectionMatrix by lazy { FloatArray(16) }
    private val modelMatrix by lazy { FloatArray(16) }
//    private val table by lazy { Table() }
//    private val mallet by lazy { Mallet() }
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
        GLES20.glViewport(0, 0, width, height)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3.2f)
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)
        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.count())
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        textureProgram.useProgram()
        textureProgram.setUniforms(projectionMatrix, texture)
//        table.bindData(textureProgram)
//        table.draw()

        colorProgram.useProgram()
//        colorProgram.setUniform(projectionMatrix)
//        mallet.bindData(colorProgram)
//        mallet.draw()
    }
}
