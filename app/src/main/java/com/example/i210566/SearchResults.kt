package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class SearchResults : AppCompatActivity() {
    private lateinit var mentorSearchRecyclerView: RecyclerView
    private lateinit var adapter: SearchMentorAdapter
    private val mentorsList = mutableListOf<MentorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        setupBottomNavigationView()

        Toast.makeText(this, "Reached", Toast.LENGTH_LONG).show()

        setupMentorsRecyclerView()
        fetchMentorsData()


    }



    private fun setupMentorsRecyclerView() {
        mentorSearchRecyclerView = findViewById(R.id.mentorSearchRecyclerView)
        mentorSearchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = SearchMentorAdapter(mentorsList)
        mentorSearchRecyclerView.adapter = adapter

    }

    private fun fetchMentorsData() {
        val firestore = FirebaseFirestore.getInstance()
        val mentorsCollection = firestore.collection("mentors")

        mentorsCollection.get()
            .addOnSuccessListener { result ->
                mentorsList.clear()
                for (document in result) {
                    val mentor = document.toObject(MentorData::class.java)
                    mentorsList.add(mentor)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@SearchResults, "Error getting documents: $exception", Toast.LENGTH_LONG).show()
            }

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