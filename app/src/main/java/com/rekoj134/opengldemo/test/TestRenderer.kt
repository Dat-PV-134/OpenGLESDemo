package com.rekoj134.opengldemo.test

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniformMatrix4fv
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.setLookAtM
import android.opengl.Matrix.translateM
import android.os.SystemClock
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.util.MatrixHelper
import com.rekoj134.opengldemo.util.ShaderHelper
import com.rekoj134.opengldemo.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val modelMatrix by lazy { FloatArray(16) }
    private val viewMatrix by lazy { FloatArray(16) }
    private val projectionMatrix by lazy { FloatArray(16) }
    private val modelViewProjectionMatrix by lazy { FloatArray(16) }

    private var triangle1Vertices: FloatBuffer

    private var uMatrixLocation = 0
    private var aPositionLocation = 0
    private var aColorLocation = 0

    private val BYTE_PER_FLOAT = 4
    private val POSTION_COMPONEN_COUNT = 3
    private val COLOR_COMPONENT_COUNT = 4
    private val STRIDE = (POSTION_COMPONEN_COUNT + COLOR_COMPONENT_COUNT) * BYTE_PER_FLOAT

    init {
        val data = floatArrayOf(
            -0.5f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.25f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f
        )

        triangle1Vertices = ByteBuffer.allocateDirect(data.size * BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer()
        triangle1Vertices.put(data)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        setLookAtM(viewMatrix, 0, 0f, 0f, 2f, 0f, 0f, -5f, 0f, 1f, 0f)
        val vertexShader = ShaderHelper.compileVertexShader(TextResourceReader.readTextFileFromResource(context, R.raw.test_vertex_shader))
        val fragmentShader = ShaderHelper.compileFragmentShader(TextResourceReader.readTextFileFromResource(context, R.raw.test_fragment_shader))
        val program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        uMatrixLocation = glGetUniformLocation(program, "u_Matrix")
        aPositionLocation = glGetAttribLocation(program, "a_Position")
        aColorLocation = glGetAttribLocation(program, "a_Color")
        glUseProgram(program)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width/height.toFloat(), 0f, 10f)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GLES20.GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = (360.0f / 10000.0f) * (time.toInt())

        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, angleInDegrees, 0f, 0f, 1f)
        translateM(modelMatrix, 0, 0f, 0f, -2f)
        drawTriangle()
    }

    private fun drawTriangle() {
        triangle1Vertices.position(0)
        glVertexAttribPointer(aPositionLocation, POSTION_COMPONEN_COUNT, GL_FLOAT, false, STRIDE, triangle1Vertices)
        glEnableVertexAttribArray(aPositionLocation)

        triangle1Vertices.position(POSTION_COMPONEN_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, triangle1Vertices)
        glEnableVertexAttribArray(aColorLocation)

        multiplyMM(modelViewProjectionMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewProjectionMatrix, 0)
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelViewProjectionMatrix, 0)
        glDrawArrays(GL_TRIANGLES, 0, 3)
    }
}