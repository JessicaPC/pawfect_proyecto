package com.example.pawfect.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityAnimalDetailsBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.model.Animal
import com.example.pawfect.ui.contact.ContactProfileActivity
import com.example.pawfect.ui.home.adapter.ImageViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AnimalDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimalDetailsBinding
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private lateinit var animal: Animal
    private var isFavorite = false
    private lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        animal = intent.getSerializableExtra("ANIMAL") as Animal

        setUpData()
        initViewPager()
        initListeners()
        isFavorite()
        hasPermissionToEdit()
    }

    private fun setUpData() {
        setAnimalData()
        setGenreIcon()
    }

    private fun setAnimalData(){
        binding.textName.text = animal.name
        binding.textAge.text = animal.age
        binding.textDescription.text = animal.description
        binding.textDistance.text = animal.distance
    }

    private fun setGenreIcon(){
        Glide.with(this)
            .load(
                if (animal.genre.equals("Macho", true) == true) {
                    R.drawable.ic_gender_male
                } else {
                    R.drawable.ic_gender_female
                }
            )
            .into(binding.imageGenre)
    }

    private fun initViewPager() {
        val urls = animal.images.toString()
        val urlsList = urls.substring(1, urls.length - 1).split(",  ")
        val uriList = urlsList.map { Uri.parse(it) }

        viewPagerAdapter = ImageViewPagerAdapter(uriList)

        binding.viewPagerImageAnimal.adapter = viewPagerAdapter

        binding.viewPagerImageAnimal.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val currentPageIndex = 0
        binding.viewPagerImageAnimal.currentItem = currentPageIndex

        binding.textIndicator.text = "1 / ${uriList.size}"

        binding.viewPagerImageAnimal.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    binding.textIndicator.text = "${position + 1} / ${uriList.size}"
                }

            }
        )
    }

    private fun initListeners() {
        clickOnBackToolbar()
        clickOnFavoriteIcon()
        clickOnContactButton()
        clickOnEditButton()
    }

    private fun clickOnBackToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun clickOnContactButton(){
        animal.userID?.let {
            binding.buttonContact.setOnClickListener {
                startActivity(Intent(this, ContactProfileActivity::class.java)
                    .putExtra("userUID", animal.userID))
            }
        }
    }

    private fun clickOnEditButton(){
        binding.buttonEdit.setOnClickListener {
            startActivity(Intent(this, EditAnimalActivity::class.java)
            .putExtra("animalData", animal))
        }
    }

    private fun clickOnFavoriteIcon(){
        binding.imageFavorite.setOnClickListener {
            if (Utils.isNetworkAvailable(this)) {
                Handler().postDelayed({
                    if (!isFavorite) {
                        uploadFavoriteToBD()
                    } else {
                        deleteFavoriteFromBD()
                    }
                }, 500)
            }else{
                Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
            }
        }
    }

    private fun uploadFavoriteToBD(){
        val uidUser = auth.currentUser?.uid
        val uploadFavorite = firebaseDB.collection("favorites").document("${animal.id}$uidUser")
        documentID = uploadFavorite.id

        uploadFavorite.set(
            hashMapOf(
                "userID" to uidUser,
                "animalID" to animal.id
            )
        ).addOnSuccessListener {
            isFavorite = true

            Glide.with(this)
                .load(R.drawable.ic_favorite_selected)
                .into(binding.imageFavorite)

            Toast.makeText(this, "Se ha añadido a favoritos", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "No se ha podido añadir a favoritos", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun deleteFavoriteFromBD(){
        firebaseDB.collection("favorites").document(documentID).delete()
            .addOnSuccessListener {
                isFavorite = false

                Glide.with(this)
                    .load(R.drawable.ic_favorite_unselected)
                    .into(binding.imageFavorite)

                Toast.makeText(this, "Se ha eliminado de favoritos", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "No se ha podido eliminar de favoritos",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun isFavorite() {
        if(Utils.isNetworkAvailable(this)) {
            firebaseDB.collection("favorites")
                .whereEqualTo("userID", auth.currentUser?.uid)
                .whereEqualTo("animalID", animal.id).get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        isFavorite = true

                        documentID = it.documents.first().id

                        Glide.with(this)
                            .load(R.drawable.ic_favorite_selected)
                            .into(binding.imageFavorite)
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "No es favorito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }else{
            Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
        }
    }

    private fun hasPermissionToEdit(){
            if(auth.currentUser?.uid == animal.userID){
                binding.buttonEdit.visibility = View.VISIBLE
            }
    }
}