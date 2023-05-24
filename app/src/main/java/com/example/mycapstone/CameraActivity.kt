package com.example.mycapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycapstone.databinding.ActivityCameraBinding
import com.example.mycapstone.databinding.ActivityMainBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        binding.button2.setOnClickListener {
            val intent = Intent(this@CameraActivity, intencobaAcitvity::class.java)
            startActivity(intent)
        }


    }
}