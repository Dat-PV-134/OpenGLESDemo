package com.rekoj134.opengldemo.from_book.objects

import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.rekoj134.opengldemo.from_book.constant.BYTES_PER_FLOAT
import com.rekoj134.opengldemo.from_book.data.VertexArray
import com.rekoj134.opengldemo.from_book.programs.ColorShaderProgram
import javax.microedition.khronos.opengles.GL

class Mallet {
    private val POSTION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE = (POSTION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    private val VERTEX_DATA = floatArrayOf(
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f,
    )
    private var vertexArray: VertexArray

    init {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttributePointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            POSTION_COMPONENT_COUNT,
            STRIDE
        )

        vertexArray.setVertexAttributePointer(
            POSTION_COMPONENT_COUNT,
            colorProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, 2)
    }
}