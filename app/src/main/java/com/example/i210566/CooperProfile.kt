package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class CooperProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooper_profile)

        // Existing code for Drop Review button
        val dropReviewButton = findViewById<AppCompatButton>(R.id.dropreviewID)
        dropReviewButton.setOnClickListener {
            val intent = Intent(this, DropReview::class.java)
            startActivity(intent)
        }

        // Existing code for Book Session button
        val bookSessionButton = findViewById<AppCompatButton>(R.id.ResetButtonID) // Assuming this is your Book Session button ID
        bookSessionButton.setOnClickListener {
            val intent = Intent(this, BookSession::class.java)
            startActivity(intent)
        }

        // Add OnClickListener for the Join Community button
        val joinCommunityButton = findViewById<AppCompatButton>(R.id.joincommunityID)
        joinCommunityButton.setOnClickListener {
            // Create an Intent to start the Community Activity
            val intent = Intent(this, Community::class.java)
            startActivity(intent)
        }
    }
}
