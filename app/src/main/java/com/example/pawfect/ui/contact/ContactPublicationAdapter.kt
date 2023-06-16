package com.example.pawfect.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawfect.R
import com.example.pawfect.databinding.ContactAnimalItemBinding
import com.example.pawfect.model.Animal


class ContactPublicationAdapter(
    private val onAnimalClick: (Animal) -> Unit
) : ListAdapter<Animal, ContactPublicationAdapter.AnimalViewHolder>(AnimalDiffCallback()) {
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
        private val binding: ContactAnimalItemBinding
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

            binding.layoutAnimal.setOnClickListener {
                onAnimalClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): AnimalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactAnimalItemBinding.inflate(layoutInflater, parent, false)
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
