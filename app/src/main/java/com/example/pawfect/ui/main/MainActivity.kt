package com.example.pawfect.ui.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityMainBinding
import com.example.pawfect.ui.favorites.FavoritesFragment
import com.example.pawfect.ui.home.HomeFragment
import com.example.pawfect.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)


    replaceFragment(HomeFragment())
    initListeners()

    }

    private fun initListeners(){
        binding.bottomNavigation.setOnTabSelectListener(object: AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int, lastTab: AnimatedBottomBar.Tab?, newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                when(newIndex){
                    0 -> replaceFragment(HomeFragment())
                    1 -> replaceFragment(FavoritesFragment())
                    2 -> replaceFragment(ProfileFragment())

                    }
                }
        })
    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()

    }

}