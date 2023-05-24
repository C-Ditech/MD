package com.example.mycapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycapstone.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Toast.makeText(this, "dasdad", Toast.LENGTH_SHORT).show()
        }
    }
}