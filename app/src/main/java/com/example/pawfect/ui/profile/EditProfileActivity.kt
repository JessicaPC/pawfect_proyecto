package com.example.pawfect.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userDocumentRef: DocumentReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)



        initListeners()
    }

    private fun initListeners(){
        clickOnBackToolbar()
        recoverUserData()
        clickOnSaveChangesButton()
    }

    private fun clickOnSaveChangesButton(){
        binding.saveChangesButton.setOnClickListener {
            saveChangesButton()
        }
    }

    private fun clickOnBackToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun recoverUserData(){
        val profileBundle = intent.getBundleExtra("profileBundle")
        val name = profileBundle?.getString("name")
        val phone = profileBundle?.getString("phone")
        val email = profileBundle?.getString("email")
        binding.editName.setText(name)
        binding.editPhone.setText(phone)
        binding.editEmail.setText(email)


    }


    private fun changeUserEmail(email:String){
        auth.currentUser?.updateEmail(email)?.addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Correo cambiado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Se produjo un error al cambiar el correo", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveChangesButton() {
        val name = binding.editName.text.toString()
        val phone = binding.editPhone.text.toString()
        val email = binding.editEmail.text.toString()

        changeUserEmail(email)
        userDocumentRef.update(
            mapOf(
                "name" to name,
                "phone" to phone,
                "email" to email
            )

        ).addOnSuccessListener {
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
        }
    }

private fun getUserPhone(){
    db.collection("users").document(binding.editEmail.text.toString()).get().addOnSuccessListener {

    }
}

}

