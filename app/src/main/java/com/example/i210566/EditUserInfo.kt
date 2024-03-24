package com.example.i210566

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditUserInfo : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var contactNumberInput: EditText
    private lateinit var countrySpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)

        // Initialize fields
        nameInput = findViewById(R.id.NameInputID)
        emailInput = findViewById(R.id.EmailInputID)
        contactNumberInput = findViewById(R.id.ContactNumberInputID)
        countrySpinner = findViewById(R.id.CountrySpinnerID)
        citySpinner = findViewById(R.id.CitySpinnerID)
        updateButton = findViewById(R.id.UpdateButton)

        // Set onClickListener for the update button
        updateButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val contactNumber = contactNumberInput.text.toString().trim()
            val country = countrySpinner.selectedItem.toString()
            val city = citySpinner.selectedItem.toString()

            // Validate input here if needed

            // Get current user ID
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            // ... rest of your code

            userId?.let { uid ->
                // Create a map for the data to update
                val updates: Map<String, Any> = mapOf(
                    "name" to name,
                    "email" to email,
                    "contact" to contactNumber,
                    "country" to country,
                    "city" to city
                )

                // Get an instance of Firestore
                val db = FirebaseFirestore.getInstance()
                // Update the user document
                db.collection("users").document(uid)
                    .update(updates as Map<String, Any>) // Explicit cast here
                    .addOnSuccessListener {
                        Toast.makeText(this, "User info updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to update user info: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }
}
