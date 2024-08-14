package com.rekoj134.opengldemo.util

object Geometry {
    class Point(val x: Float, val y: Float, val z: Float) {
        fun translateY(distance: Float) : Point {
            return Point(x, y + distance, z)
        }
    }

    class Circle(val center: Point, val radius: Float) {
        fun scale(scale: Float) : Circle {
            return Circle(center, radius)
        }
    }

    class Cylinder(val center: Point, val radius: Float, val height: Float) {

    }
}