package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class MentorProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        val mentorData = intent.getSerializableExtra("EXTRA_MENTOR_DATA") as? MentorData

        val nameTextView = findViewById<TextView>(R.id.NameID)
        val descriptionTextView = findViewById<TextView>(R.id.DescriptionID)
        val profileImageView = findViewById<ImageView>(R.id.imageViewPfp) // Make sure the ID matches with your layout

        nameTextView.text = mentorData?.name
        descriptionTextView.text = mentorData?.description
        Picasso.get().load(mentorData?.imageUrl).into(profileImageView)

        setupButtons(mentorData)
    }


    private fun setupButtons(mentorData: MentorData?) {
        val bookButton = findViewById<AppCompatButton>(R.id.BookButton)
        bookButton.setOnClickListener {
            val intent = Intent(this, BookSession::class.java).apply {
                putExtra("EXTRA_MENTOR_DATA", mentorData)
            }
            startActivity(intent)
        }

        // Initialize other buttons with a generic listener for simplicity
        setButtonClickListener(R.id.dropreviewID, DropReview::class.java, mentorData)
        setButtonClickListener(R.id.joincommunityID, Community::class.java, mentorData)
    }

    private fun setButtonClickListener(buttonId: Int, destinationClass: Class<*>, mentorData: MentorData?) {
        findViewById<AppCompatButton>(buttonId).setOnClickListener {
            val intent = Intent(this, destinationClass).apply {
                putExtra("EXTRA_MENTOR_DATA", mentorData)
            }
            startActivity(intent)
        }
    }
}
