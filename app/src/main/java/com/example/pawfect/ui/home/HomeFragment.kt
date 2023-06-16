package com.example.pawfect.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawfect.R
import com.example.pawfect.databinding.FragmentHomeBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.model.Animal
import com.example.pawfect.ui.home.adapter.AnimalAdapter
import com.example.pawfect.ui.main.AnimalDetailsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AnimalAdapter
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var listAnimal = arrayListOf<Animal>()
    private val locationPermissionCode = 111
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var meLocation: Location? = null
    private var typeSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getAnimals()
        initListeners()
        initAdapter()
        getLocation()
        return binding.root
    }

    private fun initAdapter() {
        binding.animalsRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        adapter = AnimalAdapter(
            onAnimalClick = { animal ->
                startActivity(
                    Intent(requireActivity(), AnimalDetailsActivity::class.java)
                        .putExtra("ANIMAL", animal)
                )
            }
        )
        binding.animalsRecycler.adapter = adapter
    }

    private fun initListeners() {
        clickOnAddPublicationFab()
        swipeRefreshAnimals()
        clickOnFilterChips()
        loadMoreAnimals()
    }

    private fun swipeRefreshAnimals(){
        binding.swipeRefresh.setOnRefreshListener {
            listAnimal = arrayListOf()
            getAnimals()
            binding.shimmerAnimal.visibility = View.VISIBLE
            binding.swipeRefresh.visibility = View.GONE

            binding.chipDog.isChecked = false
            binding.chipCat.isChecked = false
            binding.chipOthers.isChecked = false
        }
    }

    private fun clickOnAddPublicationFab(){
        binding.fabAddAnimal.setOnClickListener {
            startActivity(Intent(requireActivity(), PublicationActivity::class.java))
        }
    }

    private fun loadMoreAnimals(){
        binding.animalsRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    (binding.animalsRecycler.layoutManager as? GridLayoutManager)
                        ?.let {
                            if (it.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - 3 && !binding.swipeRefresh.isRefreshing) {
                                binding.swipeRefresh.isRefreshing = true
                                getAnimals(typeSelected, listAnimal.last().name)
                            }
                        }
                    super.onScrolled(recyclerView, dx, dy)
                }
            }
        )
    }
    private fun clickOnFilterChips(){
        binding.chipGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.chipDog.id -> {
                    adapter.submitList(listAnimal.filter { it.type == "Perro" })
                    typeSelected = "Perro"
                }
                binding.chipCat.id -> {
                    adapter.submitList(listAnimal.filter { it.type == "Gato" })
                    typeSelected = "Gato"
                }
                binding.chipOthers.id -> {
                    adapter.submitList(listAnimal.filter { it.type == "Otro" })
                    typeSelected = "Otro"
                }
                else -> {
                    adapter.submitList(listAnimal)
                    typeSelected = null
                }
            }
            binding.animalsRecycler.postDelayed({
                binding.animalsRecycler.smoothScrollToPosition(0)
            }, 500)
        }
    }

    private fun getAnimals(typeSelected: String? = null, startItems: String? = "A") {
        if (Utils.isNetworkAvailable(requireActivity())) {
            val query =
                firestore.collection("animals").orderBy("name").startAfter(startItems).limit(1)
            typeSelected?.let { type ->
                query.whereEqualTo("type", type)
            }
            query.get()
                .addOnSuccessListener {
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

                        meLocation?.let { meLocation ->
                            if (animal.latitude != "Sin" && animal.longitude != "null") {
                                val locationAnimal = Location("animal")

                                locationAnimal.latitude = animal.latitude!!.toDouble()
                                locationAnimal.longitude = animal.longitude!!.toDouble()

                                animal.distance = "${meLocation.distanceTo(locationAnimal)} km"
                            } else {
                                animal.distance = "Sin Ubicación"
                            }
                        } ?: run {
                            animal.distance = "Sin Ubicación"
                        }
                        listAnimal.add(animal)
                    }

                    typeSelected?.let { type ->
                        adapter.submitList(listAnimal.filter { it.type == type }.sortedBy { it.distance })
                    } ?: run {
                        adapter.submitList(listAnimal.sortedBy { it.distance })
                    }

                    adapter.notifyDataSetChanged()
                    binding.shimmerAnimal.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    binding.swipeRefresh.visibility = View.VISIBLE

                }.addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "No se han podido obtener los animales",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Utils.showAlert("Error", "Revisa tu conexion a Internet", requireActivity())
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
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
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                    }

                    meLocation = location

                    for (animal in listAnimal){
                        val locationAnimal = Location("animal")
                        locationAnimal.latitude = animal.latitude!!.toDouble()
                        locationAnimal.longitude = animal.longitude!!.toDouble()
                        animal.distance ="${meLocation?.distanceTo(locationAnimal)} km"
                    }
                    adapter.submitList(listAnimal)
                    adapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Por favor activa la localización",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        requestPermissions(
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