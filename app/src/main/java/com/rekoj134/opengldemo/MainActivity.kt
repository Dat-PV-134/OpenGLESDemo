package com.rekoj134.opengldemo

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.opengldemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myRenderer: MyGLRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myGlSurfaceView.setEGLContextClientVersion(2)
        myRenderer = MyGLRender()
        binding.myGlSurfaceView.setRenderer(myRenderer)
        binding.myGlSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}