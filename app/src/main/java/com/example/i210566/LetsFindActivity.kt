package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class LetsFindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lets_find)

        setupBottomNavigationView()


        // Find the EditText by its ID
//        val searchBar = findViewById<EditText>(R.id.searchBarID)
//
//        searchBar.setOnEditorActionListener { v, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                // Display a Toast with the search term entered
//
//
//                true // Return true to indicate you've handled the event
//            } else {
//                false // Return false to let Android handle the event as it normally would
//            }
//        }

    }


    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> startActivity(Intent(this, SearchResults::class.java))
                R.id.nav_plus -> startActivity(Intent(this, AddNewMentor::class.java))
                R.id.nav_chat -> startActivity(Intent(this, Chats::class.java))
                R.id.nav_home -> {} // Current activity, do nothing
            }
            true
        }
    }




}
