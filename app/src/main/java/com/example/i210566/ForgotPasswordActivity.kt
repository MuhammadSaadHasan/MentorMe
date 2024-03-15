package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val buttonBack: ImageButton = findViewById(R.id.back2)
        buttonBack.setOnClickListener {
            // Simplify the navigation to go back.
            onBackPressed()
        }

        val emailInput: EditText = findViewById(R.id.EmailInputID)
        val sendButton: TextView = findViewById(R.id.SendButtonID)
        val loginLabel: TextView = findViewById(R.id.LoginLabelID)

        sendButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_LONG).show()
            }
        }

        loginLabel.setOnClickListener {
            // Redirect back to MainActivity (Login Page) without creating a new Intent
            finish()
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
