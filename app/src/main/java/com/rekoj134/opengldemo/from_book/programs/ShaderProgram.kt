package com.rekoj134.opengldemo.from_book.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import com.rekoj134.opengldemo.util.ShaderHelper
import com.rekoj134.opengldemo.util.TextResourceReader

open class ShaderProgram {
    // Uniform constants
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"

    // Attribute constants
    protected val A_POSTION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

    // Shader program
    protected var program = 0

    constructor() {}

    constructor(
        context: Context,
        vertexShaderResourceId: Int,
        fragmentShaderResourceId: Int
    ) {
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        )
    }

    fun useProgram() {
        glUseProgram(program)
    }
}