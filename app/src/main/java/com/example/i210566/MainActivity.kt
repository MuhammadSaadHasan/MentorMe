package com.example.i210566

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        val emailInput = findViewById<EditText>(R.id.EmailInputID)
        val passwordInput = findViewById<EditText>(R.id.PasswordInputID)
        val loginButton = findViewById<TextView>(R.id.LoginButtonID)
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)
        val forgotPasswordID = findViewById<TextView>(R.id.ForgotPasswordID)

        // Check if user is already logged in
        checkLoggedInState()

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            loginUser(email, password)
        }

        textViewSignUp.setOnClickListener {
            startActivity(Intent(this, GetStartedActivity::class.java))
        }

        forgotPasswordID.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }



    private fun checkLoggedInState() {
        if (auth.currentUser != null) {
            // User is logged in, navigate to the HomePageActivity
            navigateToHomePage()
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveUserDataLocally()
                        navigateToHomePage()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserDataLocally() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userData = document.toObject(UserData::class.java)
                        userData?.let {
                            with(sharedPreferences.edit()) {
                                putString("userName", userData.name)
                                putString("userEmail", userData.email)
                                apply()
                            }
                            Toast.makeText(this,
                                "Authentication successful.\nWelcome ${userData.name}",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    private fun navigateToHomePage() {
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }



}
