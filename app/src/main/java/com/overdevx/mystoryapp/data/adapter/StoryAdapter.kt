package com.overdevx.mystoryapp.data.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.databinding.StoryItemBinding
import com.overdevx.mystoryapp.ui.home.DetailStoryActivity

class StoryAdapter():
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class StoryViewHolder(private val binding: StoryItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data:ListStoryItem){
                binding.tvTitle.text = data.name
                binding.tvDescription.text = data.description
                Glide.with(binding.root.context)
                    .load(data.photoUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imageView)

                binding.cardItem.setOnClickListener{
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            binding.root.context as Activity,
                            Pair(binding.tvTitle, "title"),
                            Pair(binding.tvDescription, "desc"),
                            Pair(binding.cardItem, "image"),
                        )
                    val currentStory = ListStoryItem(
                        name = data.name,
                        description = data.description,
                        createdAt = data.createdAt,
                        photoUrl = data.photoUrl
                    )
                    val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                    intent.putExtra("story", currentStory)
                    binding.root.context.startActivity(intent,optionsCompat.toBundle())
                }
            }
        }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}