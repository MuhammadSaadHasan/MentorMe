package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class SearchResults : AppCompatActivity() {
    private lateinit var mentorSearchRecyclerView: RecyclerView
    private lateinit var adapter: SearchResultsCardAdapter
    private val mentorsList = mutableListOf<MentorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        setupBottomNavigationView()

        val searchQuery = intent.getStringExtra("SEARCH_QUERY")
        Toast.makeText(this, "THE WORD: $searchQuery", Toast.LENGTH_SHORT).show()

        setupMentorsRecyclerView(searchQuery)
    }

    private fun setupMentorsRecyclerView(searchQuery: String?) {
        mentorSearchRecyclerView = findViewById(R.id.mentorSearchRecyclerView)
        mentorSearchRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultsCardAdapter(this, mentorsList)
        mentorSearchRecyclerView.adapter = adapter
        fetchMentorsData(searchQuery)
    }

    private fun fetchMentorsData(searchQuery: String?) {
        if (searchQuery.isNullOrEmpty()) {
            Toast.makeText(this, "Search query is empty", Toast.LENGTH_LONG).show()
            return
        }

        val firestore = FirebaseFirestore.getInstance()
        val mentorsCollection = firestore.collection("mentors")

        mentorsCollection.whereEqualTo("name", searchQuery)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "No matching mentors found", Toast.LENGTH_LONG).show()
                } else {
                    mentorsList.clear()
                    for (document in result) {
                        val mentor = document.toObject(MentorData::class.java)
                        mentor.Documentid = document.id // Ensure your MentorData class has this field
                        mentorsList.add(mentor)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_LONG).show()
            }
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    // Handle the search navigation if necessary
                }
                R.id.nav_plus -> startActivity(Intent(this, AddNewMentor::class.java))
                R.id.nav_chat -> startActivity(Intent(this, Chats::class.java))
                R.id.nav_home -> {
                    // Navigate home if necessary
                }
            }
            true
        }
    }
}

// You'll also need to define your `MentorData` class and `SearchResultsCardAdapter` correctly.
