package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        initRecyclerView()
        loadDataFromFirestore() // Make sure to call this method to load data

        setupBottomNavigationView()
        displayCurrentUserName()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.mentorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        // The adapter will be set after data is loaded from Firestore
    }

    private fun loadDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("mentors") // Replace "mentors" with your actual collection name
            .get()
            .addOnSuccessListener { documents ->
                val mentorsList = ArrayList<MentorData>() // Use MentorData
                for (document in documents) {
                    val mentor = document.toObject(MentorData::class.java)
                    mentorsList.add(mentor)
                }
                val adapter = HomePageMentorCardAdapter(mentorsList)
                findViewById<RecyclerView>(R.id.mentorsRecyclerView).adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayCurrentUserName() {
        val userNameTextView = findViewById<TextView>(R.id.HelloUserName)
        val userName = DataManager.currentUser?.name ?: "User"
        userNameTextView.text = userName
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> startActivity(Intent(this, LetsFindActivity::class.java))
                R.id.nav_plus -> startActivity(Intent(this, AddNewMentor::class.java))
                R.id.nav_chat -> startActivity(Intent(this, Chats::class.java))
                R.id.nav_home -> {} // Current activity, do nothing
            }
            true
        }
    }
}
