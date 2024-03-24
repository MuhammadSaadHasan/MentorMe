package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var reviewsRecyclerView: RecyclerView
    private val reviewsList = mutableListOf<ReviewData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        initRecyclerView()
        loadDataFromFirestore()
        displayUserBasicInfo()
        setupEditProfileButton()
        setupEditUserInfoButton()
    }

    private fun initRecyclerView() {
        reviewsRecyclerView = findViewById(R.id.MyReviewasRecyclerViewDisplayed) // Adjust ID if needed
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reviewsAdapter = ReviewsAdapter(reviewsList) // Use the initially empty list
        reviewsRecyclerView.adapter = reviewsAdapter
    }

    private fun loadDataFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("reviews").get()
            .addOnSuccessListener { snapshot ->
                reviewsList.clear() // Clear existing data
                for (document in snapshot.documents) {
                    // Add non-null converted ReviewData objects to the list
                    document.toObject(ReviewData::class.java)?.let { reviewData ->
                        reviewsList.add(reviewData)
                    }
                }
                reviewsAdapter.notifyDataSetChanged() // Notify the adapter of data changes
            }
            .addOnFailureListener {
                // Handle the error appropriately
                Toast.makeText(this, "Error loading reviews: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun displayUserBasicInfo() {
        val userNameTextView = findViewById<TextView>(R.id.UserNameID)
        val userProfileImageView = findViewById<ImageView>(R.id.Userpfp)
        val backgroundImageView = findViewById<ImageView>(R.id.ProfileBackGround)

        val currentUser = DataManager.currentUser
        currentUser?.let { user ->
            userNameTextView.text = user.name ?: "No Name"

            // Check if profile photo URL is not null or empty
            if (!user.profilePhoto.isNullOrEmpty()) {
                Picasso.get().load(user.profilePhoto).placeholder(R.drawable.mrbeast).error(R.drawable.numx).fit().centerCrop().into(userProfileImageView)
            } else {
                userProfileImageView.setImageResource(R.drawable.mrbeast) // Use a default image if the URL is null or empty
            }

            // Check if profile cover photo URL is not null or empty
            if (!user.profileCover.isNullOrEmpty()) {
                Picasso.get().load(user.profileCover).placeholder(R.drawable.background).error(R.drawable.numx).fit().centerCrop().into(backgroundImageView)
            } else {
                backgroundImageView.setImageResource(R.drawable.background) // Use a default image if the URL is null or empty
            }
        }
    }


    private fun setupEditProfileButton() {
        val editButton = findViewById<ImageButton>(R.id.EditIconButton)
        editButton.setOnClickListener {
            val intent = Intent(this, ChangePFP::class.java)
            startActivity(intent)

        }
    }

    private fun setupEditUserInfoButton() {
        val editButton = findViewById<ImageButton>(R.id.EditUserInfoButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditUserInfo::class.java)
            startActivity(intent)

        }
    }

}
