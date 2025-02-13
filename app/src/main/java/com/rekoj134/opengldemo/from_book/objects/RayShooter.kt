package com.rekoj134.opengldemo.from_book.objects

import android.opengl.Matrix.multiplyMV
import android.opengl.Matrix.setRotateEulerM
import com.rekoj134.opengldemo.util.Geometry
import java.util.Random

class RayShooter(
    val position: Geometry.Point,
    val direction: Geometry.Vector,
    var color: Int
) {
    private val random = Random()

    private val rotationMatrix = FloatArray(16)
    private val directionVector = FloatArray(4)
    private val resultVector = FloatArray(4)

    init {
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    fun changeColor(color: Int) {
        this.color = color
    }

    fun addRays(raySystem: RaySystem, count: Int) {
        for (i in 0 until count) {
            // Apply random rotation to direction
            setRotateEulerM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * 15f, 
                             (random.nextFloat() - 0.5f) * 15f, 
                             (random.nextFloat() - 0.5f) * 15f)
            multiplyMV(resultVector, 0, rotationMatrix, 0, directionVector, 0)

            val rayDirection = Geometry.Vector(resultVector[0], resultVector[1], resultVector[2])

            // Add ray to the system (no time, no gravity)
            raySystem.addRay(position, rayDirection)
        }
    }
}
