package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1i
import android.opengl.GLES20.glUniformMatrix4fv
import com.rekoj134.opengldemo.R

class TextureShaderProgram(
    context: Context
) : ShaderProgram(context, R.raw.texture_vertext_shader, R.raw.texture_fragment_shader) {
    // Uniform location
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0

    // Attribute location
    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        aPositionLocation = glGetAttribLocation(program, A_POSTION)
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation() : Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation() : Int {
        return aTextureCoordinatesLocation
    }
}