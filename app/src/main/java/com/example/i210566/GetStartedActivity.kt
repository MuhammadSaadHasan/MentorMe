package com.example.i210566
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class GetStartedActivity : AppCompatActivity() {

    // Declare FirebaseAuth and FirebaseFirestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameInput = findViewById<EditText>(R.id.NameInputID)
        val emailInput = findViewById<EditText>(R.id.EmailInputID)
        val contactNumberInput = findViewById<EditText>(R.id.ContactNumberInputID)
        val countrySpinner = findViewById<Spinner>(R.id.CountrySpinnerID)
        val citySpinner = findViewById<Spinner>(R.id.CitySpinnerID)
        val passwordInput = findViewById<EditText>(R.id.PasswordInputID)
        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        val textViewLogIn = findViewById<TextView>(R.id.textViewSignUp)

        // Handle Sign Up Button click
        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val contactNumber = contactNumberInput.text.toString().trim()
            val country = countrySpinner.selectedItem.toString()
            val city = citySpinner.selectedItem.toString()
            val password = passwordInput.text.toString().trim()

            // Perform input validation here if needed
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                createUser(email, password, name, contactNumber, country, city)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Log In TextView click
        textViewLogIn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun createUser(email: String, password: String, name: String, contactNumber: String, country: String, city: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        // Store additional details in Firestore
                        storeUserDetails(it.uid, name, email, contactNumber, country, city)
                    }
                    // Navigate to VerifyPhoneNumberActivity or other activity as per your flow
                    val intent = Intent(this, VerifyPhoneNumberActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception?.localizedMessage ?: "Sign up failed"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeUserDetails(userId: String, name: String, email: String, contactNumber: String, country: String, city: String) {
        val userDetails = hashMapOf(
            "name" to name,
            "email" to email,
            "contactNumber" to contactNumber,
            "country" to country,
            "city" to city
        )

        firestore.collection("users")
            .document(userId)
            .set(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "User details saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("GetStartedActivity", "Failed to save user details", e)
                Toast.makeText(this, "Failed to save user details: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

}
