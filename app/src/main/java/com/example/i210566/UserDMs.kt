package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserDMs : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dms)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.MessageInput)
        sendButton = findViewById(R.id.SendButton)
        messageAdapter = MessageAdapter(messages)
        chatRecyclerView.adapter = messageAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            sendMessage()
        }

        listenForMessages()
        setupBottomNavigation()
    }

    private fun sendMessage() {
        val text = messageInput.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(this, "Cannot send an empty message", Toast.LENGTH_SHORT).show()
            return
        }

        val fromId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val toId = "RECEIVER_USER_ID" // The ID of the user you are chatting with

        val message = Message(text = text, fromId = fromId, toId = toId)
        FirebaseFirestore.getInstance().collection("messages")
            .add(message)
            .addOnSuccessListener {
                messageInput.text.clear()
                messages.add(message)
                messageAdapter.notifyItemInserted(messages.size - 1)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun listenForMessages() {
        // Assuming you have a chats collection and each chat has messages subcollection
        val fromId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val toId = "RECEIVER_USER_ID" // The ID of the user you are chatting with

        // Update the path according to your Firestore structure
        FirebaseFirestore.getInstance()
            .collection("chats")
            .document("CHAT_ID") // The ID of the chat document
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    Toast.makeText(this, "Error fetching messages: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val messagesList = snapshot.toObjects(Message::class.java)
                messages.clear()
                messages.addAll(messagesList)
                messageAdapter.notifyDataSetChanged() // Refresh the adapter
            }
    }





    private fun setupBottomNavigation()
    {
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