package com.rekoj134.opengldemo.util

import android.util.Log
import kotlin.math.sqrt

object Geometry {
    class Point(val x: Float, val y: Float, val z: Float) {
        fun translateY(distance: Float) : Point {
            return Point(x, y + distance, z)
        }

        fun translate(vector: Vector) : Point {
            return Point(x + vector.x, y + vector.y, z + vector.z)
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
        fun translateY(distance: Float) : Ray {
            return Ray(Point(point.x, point.y + distance, point.z), vector)
        }
    }

    class Vector(val x: Float, val y: Float, val z: Float) {
        fun length() : Float {
            return sqrt(x * x + y * y + z * z)
        }

        fun crossProduct(other: Vector) : Vector {
            return Vector((y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x))
        }

        fun dotProduct(other: Vector) : Float {
            return x * other.x + y * other.y + z * other.z
        }

        fun scale(factor: Float) : Vector {
            return Vector(x * factor, y * factor, z * factor)
        }
    }

    class Plane(val point: Point, val normal: Vector)

    class Sphere(val center: Point, val radius: Float)

    fun vectorBetween(from: Point, to: Point) : Vector {
        return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
    }

    fun intersects(sphere: Sphere, ray: Ray) : Boolean {
        Log.e("ANCUTKO", distanceBetween(sphere.center, ray).toString() + " ---  " + sphere.radius)
        return distanceBetween(sphere.center, ray) < sphere.radius
    }

    fun distanceBetween(point: Point, ray: Ray) : Float {
        val p1ToPoint = vectorBetween(ray.point, point)
        val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)

        val areaOfTriangleTimeTwo = p1ToPoint.crossProduct(p2ToPoint).length()
        val lengthOfBase = ray.vector.length()

        val distanceFromPointToRay = areaOfTriangleTimeTwo / lengthOfBase

        return distanceFromPointToRay
    }

    fun intersectionPoint(ray: Ray, plane: Plane) : Point {
        val rayToPlaneVector = vectorBetween(ray.point, plane.point)
        val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
        val intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor))
        return intersectionPoint
    }
}