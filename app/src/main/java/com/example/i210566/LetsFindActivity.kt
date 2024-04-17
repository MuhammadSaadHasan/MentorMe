package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class LetsFindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lets_find)

        setupBottomNavigationView()

        setupSearchBar()
    }





    private fun setupSearchBar() {
        val searchBar = findViewById<EditText>(R.id.searchBarID)
        searchBar.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(textView.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        val intent = Intent(this, SearchResults::class.java).apply {
            putExtra("SEARCH_QUERY", query)
        }
        startActivity(intent)
    }













    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_plus -> startActivity(Intent(this, AddNewMentor::class.java))
                R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
                R.id.nav_home -> {} // Current activity, do nothing
            }
            true
        }
    }




}
