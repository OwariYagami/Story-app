package com.overdevx.mystoryapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.overdevx.mystoryapp.auth.UserViewModel
import com.overdevx.mystoryapp.auth.UserViewModelFactory
import com.overdevx.mystoryapp.data.MainViewModel
import com.overdevx.mystoryapp.data.MainViewModelFactory
import com.overdevx.mystoryapp.data.adapter.StoryAdapter
import com.overdevx.mystoryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var userViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBars.bottom)
            insets
        }
        val mainViewModelFactory = MainViewModelFactory(this)
        userViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        userViewModel.fetchStories(2,10)
        observeData()
        observeLoading()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { v, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    false
                }
        }

        binding.recyclerItem.layoutManager=LinearLayoutManager(this)
    }
    private fun observeData() {
        userViewModel.stories.observe(this, Observer { data ->
            binding.recyclerItem.adapter = StoryAdapter(data)
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
}