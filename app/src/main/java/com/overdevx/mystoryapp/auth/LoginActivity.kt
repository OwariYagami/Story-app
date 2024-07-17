package com.overdevx.mystoryapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.overdevx.mystoryapp.MainActivity
import com.overdevx.mystoryapp.MainActivity2
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.customview.MyEditText
import com.overdevx.mystoryapp.customview.MyPasswordEditText
import com.overdevx.mystoryapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            insets
        }

        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(this)).get(UserViewModel::class.java)

        observeLogin()

        observeLoading()

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }


        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun observeLogin() {
        userViewModel.loginResult.observe(this, Observer { response ->
            if (response.error == false) {
                showSuccessDialog()
            } else {
                Toast.makeText(this, "Login failed : ${response.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        userViewModel.loginError.observe(this,Observer{message->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun observeLoading() {
        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()
        userViewModel.login(email, pass)
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
        val emailValid = (binding.etEmail as MyEditText).isValid
        val passwordValid = (binding.etPassword as MyPasswordEditText).isValid
        binding.btnLogin.isEnabled = emailValid && passwordValid
    }

    private fun showSuccessDialog() {

        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.success_dialog_layout, null)


        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        val submitButton: Button = dialogView.findViewById(R.id.btn_oke)

        submitButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
}