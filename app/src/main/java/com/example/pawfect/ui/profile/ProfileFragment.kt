package com.example.pawfect.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.pawfect.R
import com.example.pawfect.databinding.FragmentProfileBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.model.Animal
import com.example.pawfect.ui.LoginActivity
import com.example.pawfect.ui.contact.ContactPublicationAdapter
import com.example.pawfect.ui.main.AnimalDetailsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val PICK_SINGLE_IMAGE = 111
    private lateinit var adapter: ContactPublicationAdapter
    private val firebaseStorageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        initListeners()
        initAdapter()
        getPublications()
        return binding.root
    }

    private fun initAdapter() {
        binding.recyclerAnimals.layoutManager =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)

        adapter = ContactPublicationAdapter(
            onAnimalClick = { animal ->
                startActivity(
                    Intent(requireActivity(), AnimalDetailsActivity::class.java)
                        .putExtra("ANIMAL", animal)
                )
            }
        )
        binding.recyclerAnimals.adapter = adapter
    }

    private fun initListeners() {
        recoverUserData()
        clickOnEditUserDataButton()
        changeProfileImage()

    }

    private fun changeProfileImage(){
        binding.profileImg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent, "Selecciona la imagen de perfil"
                ), PICK_SINGLE_IMAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_SINGLE_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {

            data.data?.let { imageSelected ->
                Glide.with(requireActivity())
                    .load(imageSelected)
                    .into(binding.profileImg)
                val imageName = "${auth.currentUser?.uid}${System.currentTimeMillis()}.png"
                val uploadImage =
                    firebaseStorageRef.child("users/$imageName").putFile(imageSelected)
                uploadImage.addOnSuccessListener {
                    firebaseStorageRef.child("users/$imageName").downloadUrl.addOnSuccessListener {
                        db.collection("users").document(auth.currentUser?.uid.toString()).update(
                            "image", it
                        )
                        Toast.makeText(
                            requireActivity(),
                            "Se ha guardado la imagen correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "La imagen no se ha subido correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            Toast.makeText(
                requireActivity(),
                "No has seleccionado ninguna imagen",
                Toast.LENGTH_SHORT
            ).show()
        }
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

    private fun clickOnEditUserDataButton() {
        binding.editProfileButton.setOnClickListener {

            val name = binding.nameText.text.toString()
            val email = binding.emailText.text.toString()
            val phone = binding.phoneText.text.toString()


            val bundle = Bundle().apply {
                putString("name", name)
                putString("phone", phone)
                putString("email", email)
            }

            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("profileBundle", bundle)
            launcher.launch(intent)
        }
    }

    private fun recoverUserData() {
        val user = auth.currentUser
        db.collection("users").document(user?.uid.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = task.result.data
                    val name = data?.get("name") as String? ?: binding.nameText.text.toString()
                    val phone = data?.get("phone") as String? ?: "Sin numero de telefono"
                    val email = data?.get("email") as String? ?: binding.emailText.text.toString()
                    val image = data?.get("image") as String?
                    // Mostrar los datos en las vistas correspondientes
                    binding.nameText.text = name
                    binding.phoneText.text = phone
                    binding.emailText.text = email

                    image?.let {
                        Glide.with(requireActivity())
                            .load(it)
                            .fallback(R.drawable.person_logo)
                            .error(R.drawable.person_logo)
                            .placeholder(R.drawable.person_logo)
                            .into(binding.profileImg)
                    }

                } else {
                    // Manejar errores al obtener los documentos de Firestore
                    Toast.makeText(
                        requireContext(),
                        "Error al obtener los datos del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
                activityResult.data?.extras?.let { extras ->
                    binding.emailText.text = extras.getString("email")
                    binding.phoneText.text = extras.getString("phone")
                    binding.nameText.text = extras.getString("name")
                }
            }
        }


    private fun getPublications(){
        if(Utils.isNetworkAvailable(requireActivity())) {
            val listAnimal = arrayListOf<Animal>()
            db.collection("animals").whereEqualTo("uid", auth.currentUser?.uid).get()
                .addOnSuccessListener {
                    if(it.documents.size > 0) {
                        for (animalData in it.documents) {
                            var listImages = animalData.data?.get("image") as String
                            listImages = listImages.removePrefix("[").removeSuffix("]")

                            val animal = Animal().apply {
                                name = animalData.data?.get("name") as String?
                                age = animalData.data?.get("age") as String?
                                genre = animalData.data?.get("genre") as String?
                                userID = animalData.data?.get("uid") as String?
                                id = animalData.id
                                type = animalData.data?.get("type") as String?
                                description = animalData.data?.get("description") as String?
                                longitude = animalData.data?.get("longitude") as String?
                                latitude = animalData.data?.get("latitude") as String?
                                images = listImages.split(",")
                            }

                            listAnimal.add(animal)
                        }

                        adapter.submitList(listAnimal)
                    }

                }.addOnFailureListener {
                    // Manejar errores al obtener los documentos de Firestore
                    Toast.makeText(
                        requireActivity(),
                        "Error al obtener los datos del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }else{
            Utils.showAlert("Error", "Revisa tu conexion a Internet", requireActivity())
        }
    }
}