package com.rekoj134.opengldemo.from_book.objects

import android.graphics.Color
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.rekoj134.opengldemo.from_book.constant.BYTES_PER_FLOAT
import com.rekoj134.opengldemo.from_book.data.VertexArray
import com.rekoj134.opengldemo.from_book.programs.ParticleShaderProgram
import com.rekoj134.opengldemo.util.Geometry
import kotlin.random.Random

class ParticleSystem(val maxParticleCount: Int) {
    private val POSITION_COMPONENT_COUNT = 3
    private val COLOR_COMPONENT_COUNT = 3
    private val VECTOR_COMPONENT_COUNT = 3
    private val PARTICLE_START_TIME_COMPONENT_COUNT = 1

    private val TOTAL_COMPONENT_COUNT =
        POSITION_COMPONENT_COUNT +
                COLOR_COMPONENT_COUNT +
                VECTOR_COMPONENT_COUNT +
                PARTICLE_START_TIME_COMPONENT_COUNT

    private val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT

    private var particles: FloatArray = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
    private var vertexArray: VertexArray = VertexArray(particles)

    private var currentParticleCount = 0
    private var nextParticle = 0

    fun addParticle(position: Geometry.Point, color: Int, direction: Geometry.Vector, particleStartTime: Float) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT

        var currentOffset = particleOffset
        nextParticle++

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }

        if (nextParticle == maxParticleCount) {
            nextParticle = 0
        }

        particles[currentOffset++] = position.x
        particles[currentOffset++] = position.y
        particles[currentOffset++] = position.z

        particles[currentOffset++] = Color.red(color) / 255f
        particles[currentOffset++] = Color.green(color) / 255f
        particles[currentOffset++] = Color.blue(color) / 255f

        particles[currentOffset++] = direction.x
        particles[currentOffset++] = direction.y
        particles[currentOffset++] = direction.z

        particles[currentOffset++] = particleStartTime

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT)
    }

    fun bindData(particalProgram: ParticleShaderProgram) {
        var dataOffset = 0
        vertexArray.setVertexAttributePointer(dataOffset, particalProgram.aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE)
        dataOffset += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttributePointer(dataOffset,
            particalProgram.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE)
        dataOffset += COLOR_COMPONENT_COUNT

        vertexArray.setVertexAttributePointer(dataOffset,
            particalProgram.aDirectionVectorLocation,
            VECTOR_COMPONENT_COUNT,
            STRIDE)
        dataOffset += VECTOR_COMPONENT_COUNT

        vertexArray.setVertexAttributePointer(dataOffset,
            particalProgram.aParticleStartTimeLocation,
            PARTICLE_START_TIME_COMPONENT_COUNT,
            STRIDE)
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount)
    }
}