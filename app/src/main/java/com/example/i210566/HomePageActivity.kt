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
        // TODO: Fetch mentors' data from Firebase and then call displayMentors() with the data
        val mentorsList = listOf(
            Mentor("Saad Man", "UX Designer at Google", "$1500/session", false, "profile_image_url", false) ,

            Mentor("What is love", "Baby dont hurtme", "$6900/session", true, "profile_image_url", true),

            Mentor("What is love", "Baby dont hurtme", "$6900/session", true, "profile_image_url", true)
        // Add more mentors as needed...
        )
        displayMentors(mentorsList)
    }

    private fun displayMentors(mentorsList: List<Mentor>) {
        val mentorsContainer = findViewById<LinearLayout>(R.id.mentorsContainer)
        mentorsList.forEach { mentor ->
            val mentorCardView = layoutInflater.inflate(R.layout.mentor_card_layout, mentorsContainer, false).apply {
                findViewById<TextView>(R.id.tvMentorName).text = mentor.name
                findViewById<TextView>(R.id.tvMentorTitle).text = mentor.title
                findViewById<TextView>(R.id.tvMentorSessionRate).text = mentor.sessionRate
                // ... Other views setup ...
            }

            val heartIcon = mentorCardView.findViewById<ImageView>(R.id.ivHeartIcon)
            updateHeartIcon(heartIcon, mentor.hearted)

            heartIcon.setOnClickListener {
                // Toggle the hearted state and update Firebase
                mentor.hearted = !mentor.hearted
                updateHeartIcon(heartIcon, mentor.hearted)
                // TODO: Update the mentor's hearted state in Firebase
            }

            mentorsContainer.addView(mentorCardView)
        }
    }

    private fun updateHeartIcon(heartIcon: ImageView, isHearted: Boolean) {
        heartIcon.setImageResource(if (isHearted) R.drawable.red_heart else R.drawable.grey_heart)
    }
}

data class Mentor(
    val name: String,
    val title: String,
    val sessionRate: String,
    val isAvailable: Boolean,
    val profileImage: String, // URL to the image
    var hearted: Boolean // To keep track if the mentor is 'hearted'
)
