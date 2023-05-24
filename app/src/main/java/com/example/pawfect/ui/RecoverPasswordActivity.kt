package com.example.pawfect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.pawfect.R
import com.example.pawfect.databinding.ActivityRecoverPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecoverPasswordBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater,null,false)

        initListeners()

        setContentView(binding.root)
    }

    private fun initListeners(){
        clickOnBackToolbar()
        checkEmailError()
        clickOnSendChangesButton()
    }


    private fun clickOnSendChangesButton(){
        binding.sendChangesButton.setOnClickListener {
            if (checkEmail()){
                auth.sendPasswordResetEmail(binding.editEmail.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Correo enviado", Toast.LENGTH_SHORT).show()
                        binding.pendingEmailAnimation.visibility = View.INVISIBLE
                        binding.emailSentAnimation.visibility = View.VISIBLE
                    }else{
                        handleFailedEmail(task.exception)
                    }
                }
            }
        }
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


    private fun handleFailedEmail(exception: Exception?) {
            if (exception is FirebaseAuthInvalidUserException){
                binding.inputEmail.error = "Ese email no existe en la base de datos"
            }
    }

    private fun clickOnBackToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }



}