package com.example.pawfect.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityMainBinding
import com.example.pawfect.databinding.FragmentProfileBinding
import com.example.pawfect.ui.LoginActivity
import com.example.pawfect.ui.profile.adapter.FragmentPageAdapter
import com.example.pawfect.ui.profile.fragments.AdoptionsFragment
import com.example.pawfect.ui.profile.fragments.PublicationsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabAdapter: FragmentPageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Inicializar ViewPager2 y TabLayout
        viewPager = binding.viewpager
        tabLayout = binding.tablayout


        // Crear y asignar el adaptador del ViewPager2
        tabAdapter = FragmentPageAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = tabAdapter

        // Agregar los fragmentos al adaptador
        tabAdapter.addFragment(PublicationsFragment())
        tabAdapter.addFragment(AdoptionsFragment())

        // Vincular el ViewPager2 con el TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Publicaciones"
                1 -> tab.text = "Adopciones"
            }
        }.attach()


        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_logout -> {
                clickOnLogOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun clickOnLogOut() {
            auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()

    }


}