package com.overdevx.mystoryapp.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.overdevx.mystoryapp.MainActivity
import com.overdevx.mystoryapp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userViewModel: UserViewModel
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupView()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(this)).get(UserViewModel::class.java)
        userViewModel.fetchToken()

        userViewModel.token.observe(this@SplashScreenActivity) { data ->
            if (data != null) {
                token = data
                proceedBasedOnToken()
            }
        }
    }
    private fun proceedBasedOnToken() {
        lifecycleScope.launch {
            delay(1000)
            if (token.isNotEmpty()) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            }
            finish()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}