package com.example.i210566

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyPhoneNumberActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_number)

        auth = FirebaseAuth.getInstance()

        val buttonBack: ImageButton = findViewById(R.id.back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
            finish()
        }

        val phoneNumber = intent.getStringExtra("phoneNumber") // Pass the phone number from the previous Activity
        val formattedPhoneNumber = formatPhoneNumberWithCountryCode(phoneNumber)
        startPhoneNumberVerification(formattedPhoneNumber)
    }

    private fun formatPhoneNumberWithCountryCode(phoneNumber: String?): String {
        // Check if the phone number is not null and does not already start with "+92"
        if (phoneNumber != null && !phoneNumber.startsWith("+92")) {
            return "+92$phoneNumber"
        }
        return phoneNumber ?: "" // Return the original number if it's null or already formatted
    }

    private fun startPhoneNumberVerification(phoneNumber: String?) {
        phoneNumber?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                it, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(applicationContext, "Verification failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            this@VerifyPhoneNumberActivity.verificationId = verificationId
            // Save verification ID and resending token so you can use them later
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    Toast.makeText(applicationContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                    // Continue to your app's logged-in experience
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(applicationContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

