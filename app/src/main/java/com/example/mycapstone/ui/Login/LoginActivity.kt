package com.example.mycapstone.ui.Login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mycapstone.databinding.ActivityLoginBinding
import com.example.mycapstone.ui.Register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        binding.seePassword.setOnClickListener {
            if (binding.seePassword.isChecked) {
                binding.inputPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.inputPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
        playanimate()
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playanimate() {
        val photo = ObjectAnimator.ofFloat(binding.imgLogin, View.ALPHA, 1f).setDuration(1000)
        val welcome = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(500)
        val pleaseLogin =
            ObjectAnimator.ofFloat(binding.pleaselogin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(500)
        val checkbox = ObjectAnimator.ofFloat(binding.seePassword, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(welcome, pleaseLogin)
        }
        AnimatorSet().apply {
            playSequentially(photo, together, email, pass, checkbox, button, register)
            start()
        }
    }
}