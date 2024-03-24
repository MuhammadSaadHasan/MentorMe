package com.example.i210566

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ChangePFP : AppCompatActivity() {
    private var imageUri: Uri? = null
    private val pickImageRequest = 1
    private val pickBackgroundRequest = 2 // Unique request code for background image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pfp)

        val changePFPButton: Button = findViewById(R.id.ChangePFPButton)
        changePFPButton.setOnClickListener {
            openFileChooser(pickImageRequest)
        }

        val changeBackgroundButton: Button = findViewById(R.id.ChangeBackgroundButton)
        changeBackgroundButton.setOnClickListener {
            openFileChooser(pickBackgroundRequest)
        }
    }

    private fun openFileChooser(requestCode: Int) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            if (requestCode == pickImageRequest) {
                uploadImageToFirebase(imageUri!!, "profilePhoto")
            } else if (requestCode == pickBackgroundRequest) {
                uploadImageToFirebase(imageUri!!, "profileCover")
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, field: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val path = if (field == "profilePhoto") "uploads/$userId/profilePhoto" else "uploads/$userId/profileCover"
        val fileReference = FirebaseStorage.getInstance().reference.child(path)

        fileReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL from the task result
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()

                    // Create a map to update the profile picture or background URL
                    val updates = hashMapOf<String, Any>(field to downloadUri.toString())

                    // Update the Firestore database
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(this, "${field.replace("_", " ").capitalize()} updated in Firestore", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to update ${field.replace("_", " ")} in Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
