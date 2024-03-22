package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class MentorProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        val mentorName = intent.getStringExtra("EXTRA_MENTOR_NAME")


        setupButtons()
    }



    private fun setupButtons() {
        setButtonClickListener(R.id.dropreviewID, DropReview::class.java)
        setButtonClickListener(R.id.BookButton, BookSession::class.java)
        setButtonClickListener(R.id.joincommunityID, Community::class.java)
    }

    private fun setButtonClickListener(buttonId: Int, destinationClass: Class<*>) {
        findViewById<AppCompatButton>(buttonId).setOnClickListener {
            startActivity(Intent(this, destinationClass))
        }
    }




}

