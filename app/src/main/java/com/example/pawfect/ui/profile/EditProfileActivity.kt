package com.example.pawfect.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawfect.databinding.ActivityEditProfileBinding
import com.example.pawfect.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userDocumentRef: DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        recoverUserData()
        initListeners()
    }

    private fun initListeners() {
        clickOnBackToolbar()
        clickOnSaveChangesButton()
        checkEmailError()
        checkPhoneError()
        checkNameError()
    }

    private fun clickOnSaveChangesButton() {
        binding.saveChangesButton.setOnClickListener {
            updateEmail()
        }
    }

    private fun clickOnBackToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun recoverUserData() {
        val profileBundle = intent.getBundleExtra("profileBundle")
        val name = profileBundle?.getString("name")
        val phone = profileBundle?.getString("phone")
        val email = profileBundle?.getString("email")
        binding.editName.setText(name)
        binding.editPhone.setText(phone)
        binding.editEmail.setText(email)
    }

    private fun updateEmail() {
        if (checkFields()) {
            if (Utils.isNetworkAvailable(this)) {
                binding.progressBarLoader.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );
                val user = auth.currentUser
                val email = binding.editEmail.text.toString()
                user?.updateEmail(email)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveChangesButton()
                    } else {
                        binding.progressBarLoader.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT)
                            .show()
                    }
                }?.addOnFailureListener {
                    binding.progressBarLoader.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
                }
            } else {
                Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
            }
        }
    }

    private fun saveChangesButton() {
        val name = binding.editName.text.toString()
        val phone = binding.editPhone.text.toString()
        val email = binding.editEmail.text.toString()
        val userId = auth.currentUser?.uid // Obtén el ID del usuario actualmente autenticado

        if (userId != null) {
            userDocumentRef = db.collection("users").document(userId)
            userDocumentRef.update(
                mapOf(
                    "name" to name,
                    "phone" to phone,
                    "email" to email
                )
            ).addOnSuccessListener {
                binding.progressBarLoader.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
                setResult(
                    RESULT_OK,
                    Intent()
                        .putExtra("name", name)
                        .putExtra("phone", phone)
                        .putExtra("email", email)
                )
                finish()
            }.addOnFailureListener {
                binding.progressBarLoader.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun checkFields(): Boolean {
        if (checkName() && checkPhone() && checkEmail()) {
            return true
        }
        return false
    }


    private fun checkNameError() {
        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputName.error = null
            }
        })
    }

    private fun checkName(): Boolean {
        if (binding.editName.text.isNullOrEmpty()) {
            binding.inputName.error = "Este campo no puede quedar vacio"
        } else {
            binding.inputName.error = null
            return true
        }

        return false
    }


    private fun checkPhoneError() {
        binding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputPhone.error = null
            }
        })
    }

    private fun checkPhone(): Boolean {
        val numberPattern = "\\d+".toRegex()
        if (binding.editPhone.text.isNullOrEmpty()) {
            binding.inputPhone.error = "Este campo no puede quedar vacio"
        } else if (!numberPattern.matches(binding.editPhone.text.toString())) {
            binding.inputPhone.error = "Introduce solo numeros"
        } else if (binding.editPhone.text.toString().length != 9) {
            binding.inputPhone.error = "Un numero de telefono debe tener 9 caracteres"
        } else {
            binding.inputPhone.error = null
            return true
        }

        return false
    }


    private fun checkEmailError() {
        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputEmail.error = null
            }
        })
    }

    private fun checkEmail(): Boolean {
        if (binding.editEmail.text.isNullOrEmpty()) {
            binding.inputEmail.error = "Este campo no puede quedar vacio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editEmail.text.toString()).matches()) {
            binding.inputEmail.error = "Email no Valido"
        } else {
            binding.inputEmail.error = null
            return true
        }

        return false
    }

}

