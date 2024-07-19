package com.overdevx.mystoryapp.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.data.utils.withDateFormat
import com.overdevx.mystoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBars.bottom)
            insets
        }
        val story: ListStoryItem? = intent.getParcelableExtra("story")
        story?.let {
            binding.tvTitle.text = story.name
            binding.tvDesc.text = story.description
            binding.tvCreatedat.text =
                getString(R.string.dateFormat, story.createdAt?.withDateFormat() ?: "")
            Glide.with(this)
                .load(story.photoUrl)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.ivImage)
        }


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}