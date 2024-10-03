package com.KAC.vikranthpublications.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var  auth : FirebaseAuth


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.loginButton.setOnClickListener {
            hideKeyboard()
            val emailid = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (emailid.isBlank()){
                showSnackbar("Enter your Email Id")
                return@setOnClickListener
            }

            if (password.isBlank()){
                showSnackbar("Please Enter the Password")
                hideKeyboard()
                return@setOnClickListener
            }
            if (isEmailValid(emailid)) {
                SignIn(emailid, password)
            }
        }


    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    private fun SignIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   val authIntent = Intent(this,MainActivity::class.java)
                    startActivity(authIntent)
                    finish()
                } else {
                    showSnackbar("Enter Valid credentials")
                }
            }
    }

    private fun showSnackbar(message: String) {
        val rootView = findViewById<View>(android.R.id.content) // Root view of the activity
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.WHITE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.purple_500))
            .show()
    }
}