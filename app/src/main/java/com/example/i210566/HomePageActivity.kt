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

    private lateinit var mentorsRecyclerView: RecyclerView
    private lateinit var adapter: MentorAdapter
    private val mentorsList = mutableListOf<MentorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setupBottomNavigationView()

        displayCurrentUserName()
        setupMentorsRecyclerView()
        fetchMentorsData()
    }

    private fun setupMentorsRecyclerView() {
        mentorsRecyclerView = findViewById(R.id.mentorsRecyclerView)
        mentorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MentorAdapter(mentorsList)
        mentorsRecyclerView.adapter = adapter

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
                Toast.makeText(this@HomePageActivity, "Error getting documents: $exception", Toast.LENGTH_LONG).show()
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
