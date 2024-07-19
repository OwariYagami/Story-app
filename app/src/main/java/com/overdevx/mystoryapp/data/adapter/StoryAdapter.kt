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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.ui.home.DetailStoryActivity

class StoryAdapter(private var storyList: List<ListStoryItem?>?): RecyclerView.Adapter<StoryAdapter.storyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.story_item,parent,false)
        return storyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoryAdapter.storyViewHolder, position: Int) {
        val currentStorypos= storyList?.get(position)
        if (currentStorypos != null) {
            holder.storyName.text=currentStorypos.name
            holder.storyDesc.text=currentStorypos.description
        }
        if (currentStorypos != null) {
            Glide.with(holder.context)
                .load(currentStorypos.photoUrl)
                .placeholder(R.drawable.img_placeholder)
                .into(holder.storyImage)
        }

        holder.card.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                   holder.context as Activity,
                    Pair(holder.storyName, "title"),
                    Pair(holder.storyDesc, "desc"),
                    Pair(holder.card, "image"),
                )
            val currentStory = ListStoryItem(
                name = currentStorypos?.name,
                description = currentStorypos?.description,
                createdAt = currentStorypos?.createdAt,
                photoUrl = currentStorypos?.photoUrl
            )
            val intent = Intent(holder.context, DetailStoryActivity::class.java)
            intent.putExtra("story", currentStory)
            holder.context.startActivity(intent,optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return storyList?.size ?: 0
    }

    class storyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val storyName : TextView = itemView.findViewById(R.id.tv_title)
        val storyDesc : TextView = itemView.findViewById(R.id.tv_description)
        val storyImage : ImageView = itemView.findViewById(R.id.image_view)
        val card : MaterialCardView = itemView.findViewById(R.id.card_item)
        val context : Context = itemView.context
    }
}