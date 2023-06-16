package com.example.pawfect.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pawfect.databinding.ActivityEditAnimalBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.model.Animal
import com.example.pawfect.ui.home.adapter.ImageViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class EditAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAnimalBinding
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    private val firebaseStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var animal: Animal
    private var listImages = arrayListOf<Uri>()
    private val PICK_IMAGE_MULTIPLE = 1
    private val uid = Firebase.auth.currentUser?.uid
    private var hasUploadImages: Boolean = false
    private val firebaseDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAnimalBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)


        animal = intent.getSerializableExtra("animalData") as Animal

        setUpData()
        initListeners()
    }

    private fun setUpData() {
        binding.editAnimalName.setText(animal.name)
        binding.editAnimalAge.setText(animal.age)
        binding.editDescription.setText(animal.description)
        binding.spinnerGenre.setSelection(if (animal.genre == "Macho") 0 else 1)
        binding.spinnerType.setSelection(if (animal.type == "Perro") 0 else if (animal.type == "Gato") 1 else 2)

        initViewPager()
    }

    private fun initListeners() {
        clickOnSaveChangesButton()
        clickOnDeleteButton()
        clickOnBackToolbar()
        clickOnSelectImagesButton()
        checkNameError()
        checkAgeError()
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


    private fun correctPublication(): Boolean {
        if (checkName() && checkAge()) {
            return true
        }
        return false
    }

    private fun checkNameError() {
        binding.editAnimalName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputAnimalName.error = null
            }
        })
    }

    private fun checkName(): Boolean {
        if (binding.editAnimalName.text.isNullOrEmpty()) {
            binding.inputAnimalName.error = "Este campo no puede quedar vacio"
        } else {
            binding.inputAnimalName.error = null
            return true
        }

        return false
    }


    private fun checkAgeError() {
        binding.editAnimalAge.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputAnimalAge.error = null
            }
        })
    }

    private fun checkAge(): Boolean {
        if (binding.editAnimalAge.text.isNullOrEmpty()) {
            binding.inputAnimalAge.error = "Este campo no puede quedar vacio"
        } else {
            binding.inputAnimalAge.error = null
            return true
        }

        return false
    }


    private fun clickOnDeleteButton() {
        binding.buttonDelete.setOnClickListener {
            if (Utils.isNetworkAvailable(this)) {
                firebaseDB.collection("animals").document(animal.id.toString()).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se ha eliminado la publicacion", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(
                            Intent(this, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "No se ha podido eliminar la publicacion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
            }

        }
    }

    private fun clickOnSaveChangesButton() {
        binding.buttonSaveChanges.setOnClickListener {
            if (correctPublication()) {
                if(!hasUploadImages){
                    saveAnimalData(arrayListOf(animal.images.toString().removeSuffix("]").removePrefix("[")))
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                }else if (listImages.size > 0) {
                    if (Utils.isNetworkAvailable(this)) {
                        showProgressBar()

                        val downloadURLTask = arrayListOf<String>()

                        for (photoAnimal in listImages) {
                            val imageName = "$uid${System.currentTimeMillis()}.png"
                            val uploadImage =
                                firebaseStorageRef.child("animals/$imageName").putFile(photoAnimal)
                            uploadImage.addOnSuccessListener {
                                firebaseStorageRef.child("animals/$imageName").downloadUrl.addOnSuccessListener {
                                    downloadURLTask.add(it.toString())

                                    if (listImages.last() == photoAnimal) {
                                        saveAnimalData(downloadURLTask)
                                        startActivity(
                                            Intent(this, MainActivity::class.java)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        )
                                    }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@EditAnimalActivity,
                                    "La imagen no se ha subido correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                binding.progressBarLoader.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                if (listImages.last() == photoAnimal) {
                                    saveAnimalData(downloadURLTask)
                                    finish()
                                }
                            }
                        }
                    } else {
                        Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
                    }
                }else {
                    Toast.makeText(
                        this,
                        "Por favor, selecciona una imagen",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBarLoader.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun clickOnBackToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun clickOnSelectImagesButton() {
        binding.buttonSelectImages.setOnClickListener {
            listImages = arrayListOf()
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent, "Selecciona la/s imagen/es"
                ), PICK_IMAGE_MULTIPLE
            )
        }
    }

    private fun saveAnimalData(imageUrl: ArrayList<String>) {
        firebaseDB.collection("animals").document(animal.id.toString()).update(
            mapOf(
                "uid" to uid,
                "name" to binding.editAnimalName.text.toString(),
                "age" to binding.editAnimalAge.text.toString(),
                "genre" to binding.spinnerGenre.selectedItem.toString(),
                "type" to binding.spinnerType.selectedItem.toString(),
                "description" to binding.editDescription.text.toString(),
                "image" to imageUrl.toString()
            )
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {
            var totalImages = 1
            data.clipData?.let { clipData ->
                totalImages = clipData.itemCount
                for (i in 0 until totalImages) {
                    listImages.add(clipData.getItemAt(i).uri)
                }
            } ?: run {
                listImages.add(data.data!!)
            }
            viewPagerAdapter = ImageViewPagerAdapter(listImages)
            setUpViewPager(totalImages)
            hasUploadImages = true
        } else {
            Toast.makeText(this, "No has seleccionado ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpViewPager(totalImages: Int) {
        binding.viewPagerImageAnimal.adapter = viewPagerAdapter
        binding.viewPagerImageAnimal.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val currentPageIndex = 0
        binding.viewPagerImageAnimal.currentItem = currentPageIndex

        binding.textIndicator.text = "1 / $totalImages"

        binding.viewPagerImageAnimal.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    binding.textIndicator.text = "${position + 1} / $totalImages"
                }

            }
        )
    }
}