package com.rekoj134.opengldemo.from_book.objects

import com.rekoj134.opengldemo.from_book.data.VertexArray
import com.rekoj134.opengldemo.from_book.programs.ColorShaderProgram
import com.rekoj134.opengldemo.util.Geometry

class Puck(
    radius: Float,
    val height: Float,
    numPointsAroundPuck: Int
) {
    private val POSITION_COMPONENT_COUNT = 3
    private var vertexArray: VertexArray
    private val drawList by lazy { ArrayList<ObjectBuilder.DrawCommand>() }

    init {
        val generatedData = ObjectBuilder.createPunk(Geometry.Cylinder(Geometry.Point(0f, 0f, 0f), radius, height), numPointsAroundPuck)
        vertexArray = VertexArray(generatedData.vertexData)
        drawList.addAll(generatedData.drawList)
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        drawList.forEach {
            it.draw()
        }
    }
}