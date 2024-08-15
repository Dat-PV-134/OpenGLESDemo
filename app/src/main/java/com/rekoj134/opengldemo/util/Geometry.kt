package com.rekoj134.opengldemo.util

import android.location.Location.distanceBetween
import java.util.Vector

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

    class Ray(val point: Geometry.Point, val vector: Vector) {

    }

    class Vector(x: Float, y: Float, z: Float) {

    }

    class Sphere(val center: Point, val radius: Float)

    fun vectorBetween(from: Point, to: Point) : Vector {
        return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
    }

    fun intersects(sphere: Sphere, ray: Ray) : Boolean {
        return distanceBetween(sphere.center, ray) < sphere.radius
    }

    fun distanceBetween(point: Point, ray: Ray) : Float {
        val p1ToPoint = vectorBetween(ray.point, point)
        val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)
    }
}