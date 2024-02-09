package com.example.i210566

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSignUpPrompt = findViewById<TextView>(R.id.tvSignUpPrompt)
        val spannableString = SpannableString("Don’t have an account? Sign Up")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Redirect to GetStartedActivity when "Sign Up" is clicked
                val intent = Intent(this@MainActivity, GetStartedActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true  // Set to true if you want underline
                ds.color = ContextCompat.getColor(applicationContext, R.color.white)  // Use your color
                ds.typeface = Typeface.create(ds.typeface, Typeface.BOLD)  // Make the text bold
            }

        }

        val signUpStartIndex = spannableString.indexOf("Sign Up")
        spannableString.setSpan(clickableSpan, signUpStartIndex, signUpStartIndex + "Sign Up".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvSignUpPrompt.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()  // This makes the link clickable
            highlightColor = getColor(android.R.color.transparent)  // Optional: change the highlight color on click
        }
    }
}
