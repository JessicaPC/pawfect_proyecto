package com.example.pawfect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityLoginBinding
import com.example.pawfect.helpers.Utils
import com.example.pawfect.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        // Tema splash
        //Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        isUserLogged()
        initListeners()


    }

    private fun isUserLogged(){
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun initListeners() {
        clickOnGoogleButton()
        clickOnLoginButton()
        checkEmailError()
        checkPasswordError()
        clickOnRecoverPassword()
        clickOnRegister()
    }


    private fun clickOnGoogleButton(){
        binding.googleButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                saveUserDataGoogle()
                                startActivity(Intent(this, MainActivity::class.java))
                            }else{
                                Toast.makeText(this, "Error registro", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }catch(e: ApiException){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUserDataGoogle() {
        val user = auth.currentUser
        val userUID = user?.uid

        db.collection("users").document(userUID.toString()).set(
            hashMapOf(
                "uid" to userUID,
                "name" to user?.displayName,
                "phone" to "Sin numero movil",
                "email" to user?.email
            )
        )
    }

    private fun clickOnLoginButton() {
        binding.loginButton.setOnClickListener {
            if (Utils.isNetworkAvailable(this)) {
                login()
            } else {
                Utils.showAlert("Error", "Revisa tu conexion a Internet", this)
            }
        }
    }

    private fun login (){
        if (checkEmail() && checkPassword()) {
            auth.signInWithEmailAndPassword(
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    handleFailedSignIn(it.exception)
                }
            }
        }
    }

    private fun handleFailedSignIn(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> handleInvalidUser()
            is FirebaseAuthInvalidCredentialsException -> handleInvalidCredentials()

        }
    }

    private fun handleInvalidUser() {
        binding.inputEmail.error = "El usuario con ese e-mail NO EXISTE"
    }

    private fun handleInvalidCredentials() {
        binding.inputPassword.error = "El usuario con ese e-mail con esa contraseña es INVALIDO"

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


        if (binding.editPassword.text.isNullOrEmpty()) {
            binding.inputPassword.error = "Este campo no puede quedar vacio"
        } else {
            binding.inputPassword.error = null
            return true
        }

        return false
    }


    private fun clickOnRegister() {
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun clickOnRecoverPassword(){
        binding.recoverPassword.setOnClickListener{
            startActivity(Intent(this, RecoverPasswordActivity::class.java))
        }
    }


}