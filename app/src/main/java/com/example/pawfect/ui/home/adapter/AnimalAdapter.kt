package com.example.pawfect.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawfect.R
import com.example.pawfect.databinding.AnimalItemBinding
import com.example.pawfect.model.Animal

// https://www.youtube.com/watch?v=eUKVJf0aMG0n   (Adapter)

class AnimalAdapter(
    private val onAnimalClick: (Animal) -> Unit
) : ListAdapter<Animal, AnimalAdapter.AnimalViewHolder>(AnimalDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        return AnimalViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(getItem(position), onAnimalClick)
    }


    class AnimalViewHolder(
        private val binding: AnimalItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Animal,
            onAnimalClick: (Animal) -> Unit
        ) {
            val context = binding.root.context

            Glide.with(context)
                .load(item.images?.first())
                .placeholder(R.drawable.image_animal_placeholder)
                .error(R.drawable.image_animal_placeholder)
                .into(binding.animalImage)

            binding.textAnimalName.text = item.name
            binding.textAnimalDistance.text = item.distance


            if (item.genre == "Macho") {
                Glide.with(context)
                    .load(R.drawable.ic_gender_male)
                    .into(binding.imageAnimalGenre)
            } else {
                Glide.with(context)
                    .load(R.drawable.ic_gender_female)
                    .into(binding.imageAnimalGenre)
            }

            binding.layoutAnimal.setOnClickListener {
                onAnimalClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): AnimalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AnimalItemBinding.inflate(layoutInflater, parent, false)
                return AnimalViewHolder(binding)
            }
        }
    }

    class AnimalDiffCallback() : DiffUtil.ItemCallback<Animal>() {
        override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
