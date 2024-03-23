package com.example.i210566

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val currentUser = DataManager.currentUser

        val userNameTextView = findViewById<TextView>(R.id.UserNameID)
        val userProfileImageView = findViewById<ImageView>(R.id.Userpfp)
        val backgroundImageView = findViewById<ImageView>(R.id.ProfileBackGround)

        // Inside the onCreate method of UserProfile

        currentUser?.let { user ->
            userNameTextView.text = user.name

            // Load profile picture or default image
            val profilePhotoPath = user.profilePhoto
            if (!profilePhotoPath.isNullOrEmpty()) {
                Picasso.get()
                    .load(profilePhotoPath)
                    .placeholder(R.drawable.mrbeast) // Default image if user.profilePhoto is null or empty
                    .error(R.drawable.numx) // Image to display on error
                    .into(userProfileImageView)
            } else {
                userProfileImageView.setImageResource(R.drawable.mrbeast) // Default image
            }

            // Similarly for the background image
            val profileBackgroundPath = user.profileCover
            if (!profileBackgroundPath.isNullOrEmpty()) {
                Picasso.get()
                    .load(profileBackgroundPath)
                    .placeholder(R.drawable.background) // Default background if user.profileCover is null or empty
                    .error(R.drawable.numx) // Background to display on error
                    .into(backgroundImageView)
            } else {
                backgroundImageView.setImageResource(R.drawable.background) // Default background
            }
        }

    }
}
