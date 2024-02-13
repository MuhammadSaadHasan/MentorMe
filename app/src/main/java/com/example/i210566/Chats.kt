package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
    }

    // Method to handle RelativeLayout click
    fun redirectToJohnCoopersDMs(view: View) {
        val intent = Intent(this, JohnCoopersDMs::class.java)
        startActivity(intent)
    }
}
