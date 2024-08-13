package com.rekoj134.opengldemo.from_book.chapter_6

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.util.LoggerConfig
import com.rekoj134.opengldemo.util.MatrixHelper
import com.rekoj134.opengldemo.util.ShaderHelper
import com.rekoj134.opengldemo.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Chapter6Renderer(private val context: Context) : GLSurfaceView.Renderer {
    companion object {
        private const val BYTES_PER_FLOAT = 4
        private const val A_POSITION = "a_Position"
        private const val A_COLOR = "a_Color"
        private const val U_MATRIX = "u_Matrix"
    }

    private val projectionMatrix by lazy { FloatArray(16) }
    private val modelMatrix by lazy { FloatArray(16) }
    private val COLOR_COMPONENT_COUNT = 3
    private val POSITION_COMPONENT_COUNT = 2
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    private var vertexData: FloatBuffer
    private var program = 0
    private var aPositionLocation = 0
    private var aColorLocation = 0
    private var uMatrixLocation = 0

    init {
        val tableVertices = floatArrayOf(
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f)

        vertexData = ByteBuffer.allocateDirect(tableVertices.count() * BYTES_PER_FLOAT).order(
            ByteOrder.nativeOrder()).asFloatBuffer()
        vertexData.put(tableVertices)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.lesson_2_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.lesson_2_fragment_shader)

        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program)
        }

        GLES20.glUseProgram(program)

        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)

        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
        GLES20.glViewport(0, 0, width, height)
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, 0f, 0f, -3.2f)
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)
        val temp = FloatArray(16)
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.count())
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }
}
