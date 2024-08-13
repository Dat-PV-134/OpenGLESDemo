package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniformMatrix4fv
import com.rekoj134.opengldemo.R

class ColorShaderProgram(context: Context) : ShaderProgram(
    context, R.raw.lesson_2_vertex_shader, R.raw.lesson_2_fragment_shader
) {
    // Uniform locations
    private var uMatrixLocation = 0

    // Attribute locations
    private var aPositonLocation = 0
    private var aColorLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositonLocation = glGetAttribLocation(program, A_POSTION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
    }

    fun setUniform(matrix: FloatArray) {
        glUniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )
    }

    fun getPositionAttributeLocation() : Int {
        return aPositonLocation
    }

    fun getColorAttributeLocation() : Int {
        return aColorLocation
    }
}