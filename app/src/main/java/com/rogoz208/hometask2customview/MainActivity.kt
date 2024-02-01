package com.rogoz208.hometask2customview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rogoz208.hometask2customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadImage()
        setupListeners()
    }

    private fun loadImage() {
        Glide.with(this)
            .asBitmap()
            .load("https://loremflickr.com/320/240")
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.wheel.image = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
    }

    private fun setupListeners() {
        binding.startButton.setOnClickListener {
            binding.wheel.rotateWheelToRandomTarget()
        }

        binding.resetButton.setOnClickListener {
            binding.wheel.resetView()
        }
    }
}