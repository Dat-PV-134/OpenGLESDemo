uniform mat4 u_Matrix;
attribute vec3 a_Position;
attribute vec3 a_DirectionVector;

void main() {
    // The start point of the ray is a_Position, and the direction is a_DirectionVector
    gl_Position = u_Matrix * vec4(a_Position + a_DirectionVector, 1.0);
}