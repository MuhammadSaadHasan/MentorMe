package com.example.i210566

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    // Declare FirebaseAuth instance
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        val emailInput = findViewById<EditText>(R.id.EmailInputID)
        val passwordInput = findViewById<EditText>(R.id.PasswordInputID)
        val loginButton = findViewById<TextView>(R.id.LoginButtonID)
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)
        val forgotPasswordID = findViewById<TextView>(R.id.ForgotPasswordID)

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

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Once the user is signed in, retrieve their user data from Firestore
                        val currentUser = auth.currentUser
                        currentUser?.let { user ->
                            firestore.collection("users").document(user.uid).get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        // Create a UserData object with the retrieved data
                                        val userData = document.toObject(UserData::class.java)

                                        // Store the UserData object in DataManager
                                        DataManager.currentUser = userData

                                        // Now you can use DataManager.currentUser in your app
                                        Toast.makeText(baseContext,
                                            "Authentication successful.\nWelcome ${DataManager.currentUser?.name}",
                                            Toast.LENGTH_LONG).show()

                                        // Navigate to the HomePageActivity
                                        startActivity(Intent(this, HomePageActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(baseContext,
                                            "Authentication successful but user data not found.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext,
                                        "Authentication successful but failed to load user data.",
                                        Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }



}
