package com.example.pawfect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityRegisterBinding
import com.example.pawfect.helpers.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        initListeners()

    }

    private fun initListeners() {
        clickOnRegisterButton()
        checkNameError()
        checkPhoneError()
        checkEmailError()
        checkPasswordError()
        checkConfirmPasswordError()
        clickOnBackToolbar()
        clickOnGoogleButton()

    }
    private fun clickOnRegisterButton(){
        binding.registerButton.setOnClickListener {
            if (correctRegisterUserData()) {
                if (Utils.isNetworkAvailable(this)) {
                    auth.createUserWithEmailAndPassword(
                        binding.editEmail.text.toString(),
                        binding.editPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Bienvenido/a", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            showAlert()
                        }
                    }
                }else{
                    Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
                }
            }
        }
    }


    private fun correctRegisterUserData():Boolean{
        if (checkName() && checkPhone() && checkEmail() && checkPassword() && checkConfirmPassword()){
            return true
        }
        return false
    }

    private fun clickOnGoogleButton(){
        binding.googleButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            val signInIntent = googleClient.signInIntent

            googleSignInLauncher.launch(signInIntent)
        }
    }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // El inicio de sesión con Google fue exitoso, maneja la respuesta aquí
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            // ... Realiza las acciones necesarias con la cuenta de Google
        } else {
            Toast.makeText(this,"El inicio de sesión con Google fue cancelado o ocurrió un error", Toast.LENGTH_SHORT).show()
        }
    }
    private fun clickOnBackToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
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
        }else if (binding.editPhone.text.toString().length != 9) {
            binding.inputPhone.error = "Un numero de telefono debe tener 9 caracteres"
        }else {
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

    private fun checkPasswordError() {
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputPassword.error = null
            }
        })
    }


    private fun checkPassword(): Boolean {
        val pattern = Regex("^(?=.*[A-Z])(?=.*\\d).{6,}$")

        if (binding.editPassword.text.isNullOrEmpty()) {
            binding.inputPassword.error = "Este campo no puede quedar vacio"
        } else if (!pattern.matches(binding.editPassword.text.toString())) {
            binding.inputPassword.error =
                "La contraseña debe tener una mayuscula, un numero y minimo 6 caracteres."
        } else {
            binding.inputPassword.error = null
            return true
        }

        return false
    }

    private fun checkConfirmPasswordError() {
        binding.editConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputConfirmPassword.error = null
            }
        })
    }


    private fun checkConfirmPassword(): Boolean {
        if (binding.editConfirmPassword.text.isNullOrEmpty()) {
            binding.inputConfirmPassword.error = "Este campo no puede quedar vacio"
        } else if (!binding.editPassword.text.toString()
                .equals(binding.editConfirmPassword.text.toString())
        ) {
            binding.inputConfirmPassword.error = "Las contraseñas no coinciden."
        } else {
            binding.inputConfirmPassword.error = null
            return true
        }
        return false

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al registrarse")
        builder.setPositiveButton("Cerrar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveUserData(){
/*
        db.collection("users").document(binding.editEmail.text.toString()).set(
            "phone" to
        )
*/

    }

}