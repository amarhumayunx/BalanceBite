package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class MainHomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
        usernameTextView = findViewById(R.id.usernameTextView)

        // Fetch and display the username
        fetchAndDisplayUsername()

        // Set up tap_on_fruits click listener
        val tapOnFruits = findViewById<LinearLayout>(R.id.tap_on_fruits)
        tapOnFruits.setOnClickListener {
            val intent = Intent(this, FruitActivity::class.java)
            startActivity(intent)
        }

        // Set up tap_on_vegetables click listener
        val tapOnVegetables = findViewById<LinearLayout>(R.id.tap_on_vegetables)
        tapOnVegetables.setOnClickListener {
            val intent = Intent(this, VegetableActivity::class.java)
            startActivity(intent)
        }

        // Set up tap_on_supplements click listener
        val tapOnSupplements = findViewById<LinearLayout>(R.id.tap_on_supplements)
        tapOnSupplements.setOnClickListener {
            val intent = Intent(this, SupplementActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAndDisplayUsername() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            return
        }

        // Fetch username from Firebase Realtime Database
        database.child(userId).child("username").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val username = dataSnapshot.getValue(String::class.java)
                    if (username != null) {
                        usernameTextView.text = "Welcome, $username"
                    } else {
                        Toast.makeText(this@MainHomeScreen, "Username not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainHomeScreen, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainHomeScreen, "Error fetching username: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        finish() // Close MainHomeScreen to prevent going back to it
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Login Page", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }
}
