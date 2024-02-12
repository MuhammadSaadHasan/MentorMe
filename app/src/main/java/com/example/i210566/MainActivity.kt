package com.example.i210566

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Redirect from sign up to GetStartedActivity
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)
        textViewSignUp.setOnClickListener {
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
        }


        //Redirect from forgot password to ForgotPasswordActivity
        val ForgotPasswordID = findViewById<TextView>(R.id.ForgotPasswordID)
        ForgotPasswordID.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


        //Redirect from forgot password to ForgotPasswordActivity
        val LoginButtonID = findViewById<TextView>(R.id.LoginButtonID)
        LoginButtonID.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }






    }
}
