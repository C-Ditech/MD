package com.example.mycapstone.ui.hasil


import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.mycapstone.databinding.ActivityHasilBinding
import com.example.mycapstone.ui.main.MainActivity
import java.io.File

class HasilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)


        val intent = intent
        val file = intent.getSerializableExtra("img") as File
        val bitmap = BitmapFactory.decodeFile(file.path)



        val disease = intent.getStringExtra("EXTRA_DISEASE")
        val accuracy = intent.getDoubleExtra("EXTRA_ACCURACY", 0.0)


        val formattedDisease = "Penyakit: $disease"
        val formattedAccuracy = "Akurasi: $accuracy%"

        println("Penyakit : $formattedDisease")
        println("Akurasi : $formattedAccuracy")

        binding.resultdesc.text = formattedDisease
        binding.akurasi.text = formattedAccuracy
        binding.imgPrev.setImageBitmap(bitmap)

        if (disease == "Healthy") {
            val sehatText = "Sehat"
            val spannable = SpannableString(sehatText)
            spannable.setSpan(
                ForegroundColorSpan(Color.GREEN),
                0,
                sehatText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.result.text = spannable
        } else {
            binding.result.setText("Berpenyakit")
        }

        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}