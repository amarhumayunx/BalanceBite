package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val username = dataSnapshot.getValue(String::class.java)
                    if (username != null) {
                        usernameTextView.text = "Welcome, $username!"
                        Toast.makeText(this@MainHomeScreen, "Username fetched: $username", Toast.LENGTH_SHORT).show()
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
}
