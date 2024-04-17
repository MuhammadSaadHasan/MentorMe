package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

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

    fun redirectToChatActivity(view: View) {
        val intent = Intent(this, ChatActivity::class.java)
        val receiverUid = "OFtspC5cW4qrUS45gNYf" // Saad's UID from Firebase
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        Toast.makeText(this, senderUid, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, receiverUid, Toast.LENGTH_SHORT).show()

        //

        intent.putExtra("name", "Saad")
        intent.putExtra("uid", receiverUid)

        // Create chat rooms identifiers
        val senderRoom = senderUid + receiverUid
        val receiverRoom = receiverUid + senderUid

        intent.putExtra("senderRoom", senderRoom)
        intent.putExtra("receiverRoom", receiverRoom)
        startActivity(intent)
    }

}
