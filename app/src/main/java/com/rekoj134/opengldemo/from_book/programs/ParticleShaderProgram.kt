package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1f
import android.opengl.GLES20.glUniform1i
import android.opengl.GLES20.glUniformMatrix4fv
import com.rekoj134.opengldemo.R

class ParticleShaderProgram : ShaderProgram {
    var uMatrixLocation: Int = 0
    var uTimeLocation: Int = 0
    var aPositionLocation: Int = 0
    var aColorLocation: Int = 0
    var aDirectionVectorLocation: Int = 0
    var aParticleStartTimeLocation: Int = 0
    var uTextureUnitLocation: Int = 0

    constructor(context: Context) : super(context, R.raw.particle_vertext_shader, R.raw.particle_fragment_shader) {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = glGetUniformLocation(program, U_TIME)

        aPositionLocation = glGetAttribLocation(program, A_POSTION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR)
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME)

        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)
    }

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform1f(uTimeLocation, elapsedTime)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureUnitLocation, 0)
    }
}