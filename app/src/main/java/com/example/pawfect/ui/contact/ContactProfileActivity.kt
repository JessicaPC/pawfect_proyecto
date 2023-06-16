package com.example.pawfect.ui.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityContactProfileBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.model.Animal
import com.example.pawfect.model.User
import com.example.pawfect.ui.home.adapter.AnimalAdapter
import com.example.pawfect.ui.main.AnimalDetailsActivity
import com.google.firebase.firestore.FirebaseFirestore

class ContactProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactProfileBinding
    private lateinit var userUID: String
    private val firebaseDB = FirebaseFirestore.getInstance()
    private lateinit var adapter: ContactPublicationAdapter
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactProfileBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        userUID = intent.extras?.getString("userUID").toString()

        getData(userUID)
        initAdapter()
        initListeners()
        getPublications()
    }

    private fun getData(userUID: String) {
        if(Utils.isNetworkAvailable(this)) {
            getUserData()
        }else{
            Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
        }
    }

    private fun getUserData(){
        firebaseDB.collection("users").document(userUID).get()
            .addOnSuccessListener { result ->
                result.data?.let { data ->
                    user = User().apply {
                        name = data["name"] as String?
                        email = data["email"] as String?
                        numberPhone = data["phone"] as String? ?: "Sin numero de telefono"
                        photo = data["image"] as String?
                    }
                    setUpData()
                }

            }.addOnFailureListener {
                // Manejar errores al obtener los documentos de Firestore
                Toast.makeText(
                    this,
                    "Error al obtener los datos del usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setUpData() {
        binding.emailText.text = user.email
        binding.phoneText.text = user.numberPhone
        binding.nameText.text = user.name
        Glide.with(this)
            .load(user.photo)
            .placeholder(R.drawable.person_logo)
            .error(R.drawable.person_logo)
            .into(binding.profileImg)
    }

    private fun initAdapter() {
        binding.recyclerAnimals.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)

        adapter = ContactPublicationAdapter(
            onAnimalClick = { animal ->
                startActivity(
                    Intent(this, AnimalDetailsActivity::class.java)
                        .putExtra("ANIMAL", animal)
                )
                finish()
            }
        )
        binding.recyclerAnimals.adapter = adapter
    }

    private fun initListeners(){
        clickOnCallButton()
        clickOnWhatsappButton()
        clickOnBackButton()
    }

    private fun clickOnCallButton(){
        binding.buttonCall.setOnClickListener {
            if(user.numberPhone.toString() != "Sin numero de telefono" && user.numberPhone.toString() != "Sin numero movil"){
                startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${user.numberPhone}")))
            }else{
                Toast.makeText(this, "Este usuario no tiene numero de telefono", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clickOnBackButton(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun clickOnWhatsappButton(){
        binding.buttonWhatssap.setOnClickListener {
            if(user.numberPhone.toString() != "Sin numero de telefono" && user.numberPhone.toString() != "Sin numero movil"){
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://api.whatsapp.com/send?phone=:+34${user.numberPhone}")))
            }else{
                Toast.makeText(this, "Este usuario no tiene numero de telefono", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPublications(){
        if(Utils.isNetworkAvailable(this)) {
            val listAnimal = arrayListOf<Animal>()
            firebaseDB.collection("animals").whereEqualTo("uid", userUID).get()
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
                    Toast.makeText(this, "Error al obtener los datos del usuario",
                        Toast.LENGTH_SHORT).show()
                }
        }else{
            Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
        }
    }
}