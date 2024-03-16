package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class JohnCoopersDMs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_john_coopers_dms)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            var intent: Intent? = null
            when (item.itemId) {
                R.id.nav_search -> intent = Intent(this, LetsFindActivity::class.java)
                R.id.nav_plus -> intent = Intent(this, AddNewMentor::class.java)
                R.id.nav_chat -> intent = Intent(this, Chats::class.java)
                R.id.nav_home -> intent = Intent(this, HomePageActivity::class.java)
            }
            intent?.let {
                startActivity(it)
                true
            } ?: false
        }

    }
}