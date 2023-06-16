package com.example.pawfect.ui.home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pawfect.databinding.ItemSliderImageBinding

class ImageViewPagerAdapter(private val imageUrlList: List<Uri>) :
    RecyclerView.Adapter<ImageViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(val binding: ItemSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUri: Uri) {

            Glide.with(binding.root.context)
                .load(imageUri)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageItem)
        }

    }

    override fun getItemCount(): Int = imageUrlList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = ItemSliderImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        holder.setData(imageUrlList[position])
    }

}