package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.*
import com.rekoj134.opengldemo.R

class RayShaderProgram(context: Context) : ShaderProgram(context, R.raw.line_vertex_shader, R.raw.line_fragment_shader) {
    
    var uMatrixLocation: Int = 0
    var aPositionLocation: Int = 0
    var aDirectionLocation: Int = 0

    init {
        // Get the locations of the attributes and uniforms
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = glGetAttribLocation(program, A_POSTION)
        aDirectionLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR)
    }

    // Set uniform values for matrix (projection/modelview) transformations
    fun setUniforms(matrix: FloatArray) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }
}
