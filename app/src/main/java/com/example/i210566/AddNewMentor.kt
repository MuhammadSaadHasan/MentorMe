package com.example.i210566

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddNewMentor : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var uploadPhotoButton: ImageButton
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var uploadButton: Button
    private lateinit var progressBar: ProgressBar


    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_mentor)

        // Initialize Firebase and UI components as before
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Correctly bind UI components
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton)
        uploadButton = findViewById(R.id.UploadButton)
        nameEditText = findViewById(R.id.NameInputID)
        descriptionEditText = findViewById(R.id.DescriptionInputID)
        priceEditText = findViewById(R.id.priceInputID)
        statusSpinner = findViewById(R.id.status)
        progressBar = findViewById(R.id.progressBar)

        // Set onClickListener for the photo upload button correctly
        uploadPhotoButton.setOnClickListener {
            pickImageFromGallery()
        }

        // Set onClickListener for the upload button
        uploadButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString()
            val status = statusSpinner.selectedItem.toString()
            if (photoUri != null) {
                progressBar.visibility = View.VISIBLE // Show the progress bar before starting the upload
                uploadImageToStorage(name, description, price, status)
            } else {
                Toast.makeText(this, "Please select a photo.", Toast.LENGTH_SHORT).show()
            }
        }

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



    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun uploadImageToStorage(name: String, description: String, price: String, status: String) {
        val filename = UUID.randomUUID().toString()
        val ref = storage.getReference("/images/$filename")

        photoUri?.let {
            ref.putFile(it).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    saveMentorToFirestore(name, description, price, status, uri.toString())
                }
            }
                .addOnFailureListener {
                    // Hide ProgressBar and show error message
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveMentorToFirestore(name: String, description: String, price: String, status: String, imageUrl: String) {
        val mentor = hashMapOf(
            "name" to name,
            "description" to description,
            "price" to price.toDouble(),
            "status" to status,
            "imageUrl" to imageUrl
        )

        firestore.collection("mentors").add(mentor).addOnSuccessListener {
            // Hide ProgressBar, show success message, and navigate to HomePageActivity
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Mentor added successfully!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }
            .addOnFailureListener {
                // Hide ProgressBar and show error message
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to add mentor: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            // Here you can update an ImageView with the selected image if needed
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}





