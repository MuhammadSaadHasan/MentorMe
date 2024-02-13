package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    val intent = Intent(this, LetsFindActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Add OnClickListener to the John Cooper FrameLayout
        val frameLayoutJohnCooper = findViewById<FrameLayout>(R.id.frameLayoutJohnCooper)
        frameLayoutJohnCooper.setOnClickListener {
            val intent = Intent(this, CooperProfile::class.java)
            startActivity(intent)
        }
    }
}
