package com.example.pawfect.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pawfect.databinding.ActivityPublicationBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.ui.home.adapter.ImageViewPagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Locale

class PublicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicationBinding
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    private val firebaseStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 111
    private val firebaseDB = FirebaseFirestore.getInstance()
    private var listImages = arrayListOf<Uri>()
    private val PICK_IMAGE_MULTIPLE = 1
    private val uid = Firebase.auth.currentUser?.uid
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicationBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        initListeners()
        setUpLocation()
    }

    private fun initListeners() {
        clickOnPublicationButton()
        clickOnBackToolbar()
        clickOnSelectImagesButton()
        checkNameError()
        checkAgeError()
    }

    private fun correctPublication():Boolean{
        if (checkName() && checkAge()){
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


    private fun clickOnPublicationButton(){
        binding.buttonPublication.setOnClickListener {
            if (correctPublication()) {
                if (listImages.size > 0) {
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
                                        finish()
                                    }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@PublicationActivity,
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

    private fun showProgressBar(){
        binding.progressBarLoader.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun clickOnBackToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun clickOnSelectImagesButton(){
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

    private fun setUpLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    private fun saveAnimalData(imageUrl: ArrayList<String>) {
        firebaseDB.collection("animals").document("$uid${System.currentTimeMillis()}").set(
            hashMapOf(
                "uid" to uid,
                "name" to binding.editAnimalName.text.toString(),
                "age" to binding.editAnimalAge.text.toString(),
                "genre" to binding.spinnerGenre.selectedItem.toString(),
                "type" to binding.spinnerType.selectedItem.toString(),
                "description" to binding.editDescription.text.toString(),
                "longitude" to longitude.toString(),
                "latitude" to latitude.toString(),
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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }



    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    location?.let{
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Por favor activa la localización", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            locationPermissionCode
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

}