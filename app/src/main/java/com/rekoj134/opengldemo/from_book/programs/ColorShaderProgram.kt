package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform4f
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
    private var uColorLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositonLocation = glGetAttribLocation(program, A_POSTION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
        uColorLocation = glGetUniformLocation(program, U_COLOR)
    }

    fun setUniform(matrix: FloatArray, r: Float, g: Float, b: Float) {
        glUniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )

        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation() : Int {
        return aPositonLocation
    }

    fun getColorAttributeLocation() : Int {
        return aColorLocation
    }
}