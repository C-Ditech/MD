package com.example.mycapstone

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.mycapstone.databinding.ActivityArtikelBinding
import com.example.mycapstone.databinding.ActivityMainBinding

class ArtikelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtikelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                isLoading(true)
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView, url: String) {
                isLoading(false)
                Toast.makeText(this@ArtikelActivity, "Web berhasil dimuat", Toast.LENGTH_LONG).show()
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        val receivedUrl = intent.getStringExtra("url") ?: ""
        binding.webView.loadUrl(receivedUrl)

    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}