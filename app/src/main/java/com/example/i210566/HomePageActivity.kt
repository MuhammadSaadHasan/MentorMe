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
import com.google.firebase.firestore.auth.User

class HomePageActivity : AppCompatActivity() {

    private lateinit var adapter: HomePageMentorCardAdapter
    private val mentorsList = mutableListOf<MentorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        initRecyclerView()
        loadDataFromFirestore()
        setupBottomNavigationView()
        displayCurrentUserName()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.mentorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = HomePageMentorCardAdapter(mentorsList)
        recyclerView.adapter = adapter
    }

    private fun loadDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("mentors")
            .get()
            .addOnSuccessListener { documents ->
                mentorsList.clear() // Clear the list before adding new data
                for (document in documents) {
                    val mentor = document.toObject(MentorData::class.java)
                    mentor.Documentid = document.id // Ensure your MentorData class has this field
                    mentorsList.add(mentor)
                }
                adapter.notifyDataSetChanged() // Notify the adapter of the data change
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
                R.id.nav_profile -> startActivity(Intent(this, UserProfile::class.java))
            }
            true
        }
    }
}
