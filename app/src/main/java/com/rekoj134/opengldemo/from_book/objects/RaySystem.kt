package com.rekoj134.opengldemo.from_book.objects

import android.opengl.GLES20.GL_LINES
import android.opengl.GLES20.glDrawArrays
import com.rekoj134.opengldemo.from_book.constant.BYTES_PER_FLOAT
import com.rekoj134.opengldemo.from_book.data.VertexArray
import com.rekoj134.opengldemo.from_book.programs.RayShaderProgram
import com.rekoj134.opengldemo.util.Geometry

class RaySystem(val maxRayCount: Int) {
    private val POSITION_COMPONENT_COUNT = 3  // Start point of the ray
    private val DIRECTION_COMPONENT_COUNT = 3 // Direction vector of the ray

    private val TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + DIRECTION_COMPONENT_COUNT
    private val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT

    private var rays: FloatArray = FloatArray(maxRayCount * TOTAL_COMPONENT_COUNT)
    private var vertexArray: VertexArray = VertexArray(rays)

    private var currentRayCount = 0
    private var nextRay = 0

    fun addRay(position: Geometry.Point, direction: Geometry.Vector) {
        val rayOffset = nextRay * TOTAL_COMPONENT_COUNT

        var currentOffset = rayOffset
        nextRay++

        if (currentRayCount < maxRayCount) {
            currentRayCount++
        }

        if (nextRay == maxRayCount) {
            nextRay = 0
        }

        // Store the ray's start position and direction
        rays[currentOffset++] = position.x
        rays[currentOffset++] = position.y
        rays[currentOffset++] = position.z

        rays[currentOffset++] = direction.x
        rays[currentOffset++] = direction.y
        rays[currentOffset++] = direction.z

        vertexArray.updateBuffer(rays, rayOffset, TOTAL_COMPONENT_COUNT)
    }

    fun bindData(rayProgram: RayShaderProgram) {
        var dataOffset = 0
        vertexArray.setVertexAttributePointer(dataOffset, rayProgram.aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE)
        dataOffset += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttributePointer(dataOffset, rayProgram.aDirectionLocation, DIRECTION_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        glDrawArrays(GL_LINES, 0, currentRayCount * 2)  // Draw as lines
    }
}
