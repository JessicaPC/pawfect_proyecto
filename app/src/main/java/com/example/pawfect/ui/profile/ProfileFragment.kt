package com.example.pawfect.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pawfect.R
import com.example.pawfect.databinding.FragmentProfileBinding
import com.example.pawfect.ui.LoginActivity
import com.example.pawfect.ui.profile.adapter.FragmentPageAdapter
import com.example.pawfect.ui.profile.fragments.AdoptionsFragment
import com.example.pawfect.ui.profile.fragments.PublicationsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabAdapter: FragmentPageAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userDocumentRef: DocumentReference


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

        initListeners()


        return binding.root
    }


    private fun initListeners(){
        recoverUserData()
        clickOnEditUserDataButton()

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

    private fun clickOnEditUserDataButton(){
        binding.editProfileButton.setOnClickListener {
            val name = binding.nameText.text.toString()
            val email = binding.emailText.text.toString()
            val phone = binding.phoneText.text.toString()


            val bundle = Bundle().apply {
                putString("name", name)
                putString("phone", phone)
                putString("email",email)
            }

            val intent = Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("profileBundle",bundle)
            startActivity(intent)
        }
        /*
        binding.editProfileButton.setOnClickListener {
            if (isEditMode){
                // El perfil ya está en modo de edición, realiza la lógica de guardado de datos aquí
                saveProfileChanges()

                // Cambia al modo de visualización del perfil
                showProfileData()

            }else{
                // El perfil está en modo de visualización, cambia al modo de edición
                showEditProfileForm()
            }
        }

         */
    }

    /*
    private fun saveProfileChanges() {
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()

        changeUserEmail(email)
        userDocumentRef.update(
            mapOf(
                "name" to name,
                "email" to email
            )



        ).addOnSuccessListener {
            Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
            showProfileData() // Cambiar al modo de visualización después de guardar los cambios
            recoverUserData()
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
            Log.e("ProfileFragment", "Error al guardar los cambios", e)
        }//   binding.inputName
    }

    private fun changeUserEmail(email:String){
        auth.currentUser?.updateEmail(email)?.addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(requireContext(),"Correo cambiado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Se produjo un error al cambiar el correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProfileData(){
        showUserData()


        binding.editProfileButton.text = "Editar Perfil"
        isEditMode = false
    }

    private fun showEditProfileForm(){
        hideUserData()

        val currentName = binding.nameText.text.toString()
        val currentEmail = binding.emailText.text.toString()
        binding.editName.setText(currentName)
        binding.editEmail.setText(currentEmail)

        binding.editProfileButton.text = "Guardar Cambios"
        isEditMode = true
    }


    private fun hideUserData(){
        binding.nameText.visibility = View.INVISIBLE
        binding.inputName.visibility = View.VISIBLE
        binding.emailText.visibility = View.INVISIBLE
        binding.inputEmail.visibility = View.VISIBLE
        binding.locationText.visibility = View.INVISIBLE
        binding.tablayout.visibility = View.INVISIBLE
        binding.viewpager.visibility = View.INVISIBLE
    }

    private fun showUserData(){
        binding.nameText.visibility = View.VISIBLE
        binding.inputName.visibility = View.INVISIBLE
        binding.emailText.visibility = View.VISIBLE
        binding.inputEmail.visibility = View.INVISIBLE
        binding.tablayout.visibility = View.VISIBLE
        binding.viewpager.visibility = View.VISIBLE
    }

*/
    private fun recoverUserData(){

        val user = auth.currentUser

        val userEmail = user?.email
        if (userEmail != null) {
            val usersCollection = db.collection("users")
            val query = usersCollection.whereEqualTo("email", userEmail)

            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (!documents.isNullOrEmpty()) {
                        val userDocument = documents[0]
                        val name = userDocument.getString("name")
                        val phone = userDocument.getString("phone")
                        // Mostrar los datos en las vistas correspondientes
                        binding.nameText.text = name
                        binding.phoneText.text = phone
                    }
                } else {
                    // Manejar errores al obtener los documentos de Firestore
                    Toast.makeText(requireContext(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.nameText.text = user?.displayName
        binding.emailText.text = user?.email
        binding.phoneText.text = user?.phoneNumber


    }


}