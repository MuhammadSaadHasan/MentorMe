package com.example.i210566

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookSession : AppCompatActivity() {
    private lateinit var mentorData: MentorData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_session)

        // Fetch the mentorData from the intent
        mentorData = intent.getSerializableExtra("EXTRA_MENTOR_DATA") as? MentorData
            ?: throw IllegalStateException("No MentorData passed to BookSession")

        displayMentorData(mentorData)
        setupBookingButton()
    }

    private fun displayMentorData(mentorData: MentorData) {
        // Set the mentor's name
        val mentorNameTextView = findViewById<TextView>(R.id.MentorNameID)
        mentorNameTextView.text = mentorData.name

        // Set the session price, making sure to convert the numeric value to a String
        val sessionPriceTextView = findViewById<TextView>(R.id.SessionPriceID)
        sessionPriceTextView.text = mentorData.price?.toString() ?: "N/A"

        // Set the mentor's profile picture if available
        val mentorPicImageView = findViewById<ImageView>(R.id.MentorPicID)
        Picasso.get().load(mentorData.imageUrl).placeholder(R.drawable.p1).error(R.drawable.numx).into(mentorPicImageView)


        Toast.makeText(this, mentorData.name, Toast.LENGTH_SHORT).show()
    }

    private fun setupBookingButton() {
        val bookButton = findViewById<AppCompatButton>(R.id.BookAnappointment)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val timeSlotsGroup = findViewById<RadioGroup>(R.id.timeSlotsGroup)

        var selectedDate: String? = null

        // Listener for calendar date change
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Note that month is zero-based, so we need to add 1.
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = format.format(Date(year - 1900, month, dayOfMonth))
        }

        bookButton.setOnClickListener {
            // Get the selected time slot text
            val selectedTimeSlotRadioButtonId = timeSlotsGroup.checkedRadioButtonId
            val selectedTimeSlot = if (selectedTimeSlotRadioButtonId != -1) {
                findViewById<RadioButton>(selectedTimeSlotRadioButtonId).text.toString()
            } else {
                Toast.makeText(this, "Please select a time slot.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate == null) {
                Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mentorDocumentId = mentorData.Documentid ?: "Unknown Mentor ID"

            createBooking(selectedDate!!, selectedTimeSlot, mentorDocumentId)
        }
    }



    private fun createBooking(date: String, time: String, mentorId: String) {
        val bookingInfo = hashMapOf(
            "date" to date,
            "time" to time,
            "mentorDocumentId" to mentorId
        )

        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User must be logged in to book an appointment")

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .collection("bookings").add(bookingInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show()

                // Redirect to the homepage
                val homeIntent = Intent(this, HomePageActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish() // Close the current activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to book appointment: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
