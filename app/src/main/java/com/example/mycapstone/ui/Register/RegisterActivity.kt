package com.example.mycapstone.ui.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycapstone.MainActivity
import com.example.mycapstone.databinding.ActivityRegisterBinding
import com.example.mycapstone.ui.Login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        playanimate()
        setupInput()
        binding.seePassword.setOnClickListener {
            if (binding.seePassword.isChecked) {
                binding.inputPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.confirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.inputPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.confirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        binding.registerButton.setOnClickListener {
            Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun playanimate() {
        val photo = ObjectAnimator.ofFloat(binding.imgLogin, View.ALPHA, 1f).setDuration(1000)
        val welcome = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.inputName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val confpass =
            ObjectAnimator.ofFloat(binding.confirmPassword, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(500)
        val checkbox = ObjectAnimator.ofFloat(binding.seePassword, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                photo,
                welcome,
                name,
                email,
                pass,
                confpass,
                checkbox,
                button,
                register
            )
            start()
        }
    }

    private fun setupInput(){val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            val isNameEmpty = binding.inputName.text.toString().trim().isEmpty()
            val isEmailEmpty = binding.inputEmail.text.toString().trim().isEmpty()
            val isPasswordEmpty = binding.inputPassword.text.toString().trim().isEmpty()
            val isPasswordValid = binding.inputPassword.text.toString().length >= 8
            val isConfirmEmpty = binding.confirmPassword.text.toString().trim().isEmpty()
            val isPasswordMatching = binding.inputPassword.text.toString() == binding.confirmPassword.text.toString()
            binding.registerButton.isEnabled = !isNameEmpty && !isEmailEmpty && !isPasswordEmpty && isPasswordValid && !isConfirmEmpty && isPasswordMatching
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
        binding.inputName.addTextChangedListener(loginTextWatcher)
        binding.inputEmail.addTextChangedListener(loginTextWatcher)
        binding.inputPassword.addTextChangedListener(loginTextWatcher)
        binding.confirmPassword.addTextChangedListener(loginTextWatcher)

    }
}