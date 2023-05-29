package com.example.pawfect.ui.home

import AnimalAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pawfect.R
import com.example.pawfect.databinding.FragmentHomeBinding
import com.example.pawfect.model.Animal
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupStaggeredGridView()
        return binding.root
    }

    private fun setupStaggeredGridView() {
        binding.animalsRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = AnimalAdapter(createAnimalList()){animal, position ->
                Toast.makeText(requireContext(),"Click en ${animal.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAnimalList(): ArrayList<Animal> {
        return arrayListOf(
            Animal("Gato", R.drawable.cat_img),
            Animal("Gato2", R.drawable.cat2_img),
            Animal("Perro", R.drawable.dog_img),
            Animal("Perro2", R.drawable.dog2_img),
            Animal("Perro3", R.drawable.dog3_img),
            Animal("Perro4", R.drawable.dog4_img),
            Animal("Gato3", R.drawable.cat3_img),
            Animal("Gato4", R.drawable.cat4_img)
        )
    }



}