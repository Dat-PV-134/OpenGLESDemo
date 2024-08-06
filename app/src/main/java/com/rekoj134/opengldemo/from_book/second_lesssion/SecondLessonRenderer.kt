package com.rekoj134.opengldemo.from_book.second_lesssion

import android.content.Context
import android.opengl.GLSurfaceView.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import com.rekoj134.opengldemo.R
import com.rekoj134.opengldemo.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SecondLessonRenderer : Renderer {
    companion object {
        private const val BYTES_PER_FLOAT = 4
    }

    private val POSITION_COMPONENT_COUNT = 2
    private var vertexData: FloatBuffer
    private lateinit var context: Context

    constructor(context: Context) : super() {
        this.context = context
    }

    init {
        val tableVertices = floatArrayOf(
            0f, 0f,
            9f, 14f,
            0f, 14f,

            0f, 0f,
            9f, 0f,
            9f, 14f,

            0f, 7f,
            9f, 7f,

            4.5f, 2f,
            4.5f, 12f)

        vertexData = ByteBuffer.allocateDirect(tableVertices.count() * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexData.put(tableVertices)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.lesson_2_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.lesson_2_fragment_shader)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
    }
}