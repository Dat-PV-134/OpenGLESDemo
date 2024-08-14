package com.rekoj134.opengldemo.from_book.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.GL_TRIANGLE_STRIP
import android.opengl.GLES20.glDrawArrays
import com.rekoj134.opengldemo.util.Geometry
import kotlin.math.cos
import kotlin.math.sin

class ObjectBuilder private constructor(sizeInVertices: Int) {
    private var vertexData: FloatArray = FloatArray(sizeInVertices * FLOAT_PER_VERTEX)
    private var offset = 0
    private val drawList by lazy { ArrayList<DrawCommand>() }

    companion object {
        private const val FLOAT_PER_VERTEX = 3


        class GeneratedData(val vertexData: FloatArray, val drawList: List<DrawCommand>) {

        }

        private fun sizeOfCircleInVertices(numPoints: Int) : Int {
            return 1 + (numPoints + 1)
        }

        private fun sizeOfOpenCylinderInVertices(numPoints: Int) : Int {
            return (numPoints + 1) * 2
        }

        fun createPunk(puck: Geometry.Cylinder, numPoints: Int) : GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)

            val builder = ObjectBuilder(size)

            val puckTop = Geometry.Circle(puck.center.translateY(puck.height/2f), puck.radius)
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)

            return builder.build()
        }

        fun createMallet(center: Geometry.Point, radius: Float, height: Float, numPoints: Int) : GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2
            val builder = ObjectBuilder(size)

            val baseHeight = height * 0.25f
            val baseCircle = Geometry.Circle(center.translateY(-baseHeight), radius)
            val baseCylinder = Geometry.Cylinder(baseCircle.center.translateY(-baseHeight/2f), radius, baseHeight)

            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f
            val handleCircle = Geometry.Circle(center.translateY(height * 0.5f), handleRadius)
            val handleCylinder = Geometry.Cylinder(handleCircle.center.translateY(-handleHeight/2f), handleRadius, handleHeight)

            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)

            return builder.build()
        }
    }

    private fun build() : GeneratedData {
        return GeneratedData(vertexData, drawList)
    }

    private fun appendCircle(circle: Geometry.Circle, numPoints: Int) {
        val startVertex = offset / FLOAT_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z

        for (i in 0.. numPoints) {
            val angleInRadians = ((i / numPoints.toFloat()) * (Math.PI * 2f).toFloat())
            vertexData[offset++] = circle.center.x + circle.radius * cos(angleInRadians)
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = circle.center.z + circle.radius * sin(angleInRadians)
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    private fun appendOpenCylinder(cylinder: Geometry.Cylinder, numPoints: Int) {
        val startVertex = offset / FLOAT_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - (cylinder.height / 2f)
        val yEnd = cylinder.center.y + (cylinder.height / 2f)

        for (i in 0..numPoints) {
            val angleInRadians = ((i/numPoints.toFloat()) * (Math.PI * 2f).toFloat())
            val xPosition = cylinder.center.x + cylinder.radius * cos(angleInRadians)
            val zPosition = cylinder.center.z + cylinder.radius * sin(angleInRadians)

            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition

            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    interface DrawCommand {
        fun draw()
    }
}