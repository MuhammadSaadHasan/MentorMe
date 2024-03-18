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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_number)

        /*auth = FirebaseAuth.getInstance()

        val buttonBack: ImageButton = findViewById(R.id.back)
        buttonBack.setOnClickListener {
            finish() // Just finish current activity, no need to create new intent for previous activity
        }

        val phoneNumber = intent.getStringExtra("phoneNumber") ?: run {
            Toast.makeText(this, "Phone number is missing.", Toast.LENGTH_SHORT).show()
            finish() // Finish activity if phone number is missing
            return
        }
        val formattedPhoneNumber = formatPhoneNumberWithCountryCode(phoneNumber)
        if (formattedPhoneNumber.isNotBlank()) {
            startPhoneNumberVerification(formattedPhoneNumber)
        } else {
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun formatPhoneNumberWithCountryCode(phoneNumber: String?): String {
        return if (phoneNumber != null && !phoneNumber.startsWith("+92")) {
            "+92$phoneNumber"
        } else {
            phoneNumber ?: ""
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(applicationContext, "Verification failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Intent to a new Activity where the user can enter the verification code
            /*val intent = Intent(this@VerifyPhoneNumberActivity, CodeVerificationActivity::class.java)
            intent.putExtra("verificationId", verificationId)
            startActivity(intent)*/
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                    // Navigate to main part of your application
                } else {
                    Toast.makeText(applicationContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
