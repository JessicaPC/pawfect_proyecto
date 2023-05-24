package com.example.pawfect.ui.profile.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pawfect.R
import com.example.pawfect.databinding.FragmentAdoptionsBinding
import com.example.pawfect.ui.home.HomeFragment

class AdoptionsFragment : Fragment() {
private lateinit var binding : FragmentAdoptionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdoptionsBinding.inflate(inflater,container,false)

        return binding.root
    }

    private fun clickOnAdoptionButton(){
        binding.adoptionButton.setOnClickListener {
            startActivity(Intent())
        }
    }

}