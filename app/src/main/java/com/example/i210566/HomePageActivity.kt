package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        setupBottomNavigationView()
        fetchMentorsData()
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val intent = when (item.itemId) {
                R.id.nav_search -> Intent(this, LetsFindActivity::class.java)
                R.id.nav_plus -> Intent(this, AddNewMentor::class.java)
                R.id.nav_chat -> Intent(this, Chats::class.java)
                R.id.nav_home -> Intent(this, HomePageActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
            true
        }
    }

    private fun fetchMentorsData() {
        val db = FirebaseFirestore.getInstance()

        db.collection("mentors")
            .get()
            .addOnSuccessListener { result ->
                val mentorsList = result.mapNotNull { document ->
                    Mentor(
                        name = document.getString("name") ?: "N/A",
                        description = document.getString("description") ?: "N/A",
                        price = document.getLong("price")?.toString() ?: "N/A",
                        imageUrl = document.getString("imageUrl") ?: "N/A",
                        status = document.getString("status") ?: "N/A"
                    )
                }
                displayMentors(mentorsList)
            }
            .addOnFailureListener { exception ->
                println("Error fetching mentors: ${exception.localizedMessage}")
            }
    }

    private fun displayMentors(mentorsList: List<Mentor>) {
        val mentorsContainer = findViewById<LinearLayout>(R.id.mentorsContainer)
        mentorsContainer.removeAllViews() // Clear previous views
        mentorsList.forEach { mentor ->
            val mentorCardView = layoutInflater.inflate(R.layout.mentor_card_layout, mentorsContainer, false)
            mentorCardView.findViewById<TextView>(R.id.tvMentorName).text = mentor.name
            mentorCardView.findViewById<TextView>(R.id.tvMentorTitle).text = mentor.description
            mentorCardView.findViewById<TextView>(R.id.tvMentorSessionRate).text = mentor.price

            // Load image using a library like Glide or Picasso

            mentorsContainer.addView(mentorCardView)
        }
    }

}

data class Mentor(
    val description: String = "",
    val imageUrl: String = "", // URL to the image
    val name: String = "",
    val price: String = "",
    val status: String = "" // Assuming status is a string like "Available" or "Unavailable"
)

