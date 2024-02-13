package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class CooperProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooper_profile)

        // Find the button by its ID
        val dropReviewButton = findViewById<AppCompatButton>(R.id.dropreviewID)

        // Set an OnClickListener to the button
        dropReviewButton.setOnClickListener {
            // Create an Intent to start the DropReview Activity
            val intent = Intent(this, DropReview::class.java)
            startActivity(intent)
        }
    }
}
