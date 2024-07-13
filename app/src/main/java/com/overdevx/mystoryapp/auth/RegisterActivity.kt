package com.overdevx.mystoryapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.customview.MyEditText
import com.overdevx.mystoryapp.customview.MyPasswordEditText
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import com.overdevx.mystoryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            insets
        }
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(this)).get(UserViewModel::class.java)

        observerRegister()
        observeLoading()
        binding.etName.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        userViewModel.register(name, email, password)
    }

    private fun observerRegister() {
        userViewModel.registerResult.observe(this, Observer { response ->
            if (response.error == false) {
                Toast.makeText(this, "Register success : ${response.message}", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Register failed : ${response.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun observeLoading() {
        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressindicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnable()
        }

        override fun afterTextChanged(s: Editable?) {
            // Do nothing.
        }
    }

    private fun setMyButtonEnable() {
        var nameValid = false
        val emailValid = (binding.etEmail as MyEditText).isValid
        val passwordValid = (binding.etPassword as MyPasswordEditText).isValid
        if (binding.etName.text.isNullOrEmpty()) {
            nameValid = false
        } else {
            nameValid = true
        }
        binding.btnRegister.isEnabled = emailValid && passwordValid && nameValid
    }
}