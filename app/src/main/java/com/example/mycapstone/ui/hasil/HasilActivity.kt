package com.example.mycapstone.ui.hasil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycapstone.R
import com.example.mycapstone.databinding.ActivityHasilBinding

class HasilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val intent = intent
        val data1 = intent.getStringExtra("key1")
        val data2 = intent.getStringExtra("key2")
        val data3 = intent.getStringExtra("key3")

        binding.inputNama.setText(data1)
        binding.inputTanggal.setText(data2)
        binding.inputDeskripsi.setText(data3)
    }
}