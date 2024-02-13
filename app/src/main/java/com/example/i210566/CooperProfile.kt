package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class CooperProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooper_profile)

        // Find the Drop Review button by its ID and set an OnClickListener
        val dropReviewButton = findViewById<AppCompatButton>(R.id.dropreviewID)
        dropReviewButton.setOnClickListener {
            // Create an Intent to start the DropReview Activity
            val intent = Intent(this, DropReview::class.java)
            startActivity(intent)
        }

        // Find the Book Session button by its ID and set an OnClickListener
        val bookSessionButton = findViewById<AppCompatButton>(R.id.ResetButtonID) // Assuming this is your Book Session button ID
        bookSessionButton.setOnClickListener {
            // Create an Intent to start the BookSession Activity
            val intent = Intent(this, BookSession::class.java)
            startActivity(intent)
        }
    }
}
