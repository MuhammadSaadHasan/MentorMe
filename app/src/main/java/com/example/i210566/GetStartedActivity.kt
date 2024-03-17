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
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "contact" to contactNumber,
            "country" to country,
            "city" to city,
            "profilePhoto" to "", // Start with empty string or default URL
            "profileCover" to "", // Start with empty string or default URL
            "heartedMentors" to listOf<String>()
            // Any other fields you want to initialize
        )

        // Save the user document
        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("GetStartedActivity", "User details saved successfully")
                // Initialize subcollections if needed. Here, we simply log success.
                initializeSubcollections(userId, listOf("chats", "bookings", "notifications", "reviews"))
                // Proceed to next activity or flow
            }
            .addOnFailureListener { e ->
                Log.e("GetStartedActivity", "Error writing document", e)
            }
    }

    private fun initializeSubcollections(userId: String, subcollections: List<String>) {
        subcollections.forEach { subcollection ->
            val initialData = when (subcollection) {
                "chats", "bookings", "notifications" -> hashMapOf("placeholder" to true)
                "reviews" -> hashMapOf("placeholder" to true) // For example, adjust based on your data structure
                else -> hashMapOf("placeholder" to true)
            }
            firestore.collection("users").document(userId)
                .collection(subcollection)
                .document("initial") // Using "initial" as a placeholder document
                .set(initialData)
                .addOnSuccessListener {
                    Log.d("GetStartedActivity", "Subcollection $subcollection initialized for $userId")
                }
                .addOnFailureListener { e ->
                    Log.e("GetStartedActivity", "Error initializing $subcollection for $userId", e)
                }
        }
    }

}
