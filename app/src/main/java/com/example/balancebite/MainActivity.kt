package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                checkUserStatusAndNavigate()
            }, 2000
        )
    }

    private fun checkUserStatusAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate to the dashboard
            val intent = Intent(this, MainHomeScreen::class.java)
            Toast.makeText(this, "Move to Dashboard", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        else {
            // User is not logged in, navigate to the login activity
            val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
            Toast.makeText(this, "login to your account", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        finish() // Close the current activity
    }
}