package com.example.i210566

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DropReview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val reviewEditText = findViewById<EditText>(R.id.EmailInputID)
        val submitButton = findViewById<Button>(R.id.ResetButtonID)

        val mentorData = intent.getSerializableExtra("EXTRA_MENTOR_DATA") as? MentorData
        if (mentorData == null) {
            Toast.makeText(this, "Mentor data is not available.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val experience = reviewEditText.text.toString()
            if (experience.isEmpty()) {
                Toast.makeText(this, "Please enter your experience.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val review = hashMapOf(
                "mentorName" to mentorData.name,
                "experience" to experience,
                "rating" to rating
            )

            // Assuming the user is logged in and you want to store the review under the user's document
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "You must be logged in to submit a review.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("reviews").add(review)
                .addOnSuccessListener {
                    Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after submitting
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to submit feedback: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
