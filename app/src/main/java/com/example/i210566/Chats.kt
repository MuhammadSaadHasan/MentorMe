package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    val intent = Intent(this, LetsFindActivity::class.java)
                    startActivity(intent)
                    true
                }


                R.id.nav_plus -> {
                    val intent = Intent(this, AddNewMentor::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_chat -> {
                    val intent = Intent(this, Chats::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_home -> {
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    true
                }


                else -> false
            }
        }


    }

    // Method to handle RelativeLayout click
    fun redirectToJohnCoopersDMs(view: View) {
        val intent = Intent(this, UserDMs::class.java)
        startActivity(intent)
    }
}
