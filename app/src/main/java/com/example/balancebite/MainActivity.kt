package com.example.balancebite

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                checkUserStatusAndNavigate()
            }, 1000
        )
    }

    private fun checkUserStatusAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Check if user data exists in Firebase Database
            val userId = currentUser.uid
            val userRef = database.getReference("Users").child(userId)

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        val userData = task.result.children
                        // Check if essential user data exists
                        val hasRequiredData = userData.any { it.key in listOf("name", "age", "weight", "height", "healthInfo") }
                        if (hasRequiredData) {
                            // User data exists and contains essential information, navigate to the dashboard
                            val intent = Intent(this, MainHomeScreen::class.java)
                            Toast.makeText(this, "Moving to Dashboard", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        } else {
                            // User data exists but is incomplete, navigate to the login activity
                            val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
                            Toast.makeText(this, "Incomplete user data. Please log in again.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    } else {
                        // User data does not exist in the database, navigate to the login activity
                        val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
                        Toast.makeText(this, "No user data found. Please log in.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                } else {
                    // Error occurred while fetching user data
                    Toast.makeText(this, "Error checking user data. Please log in.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
                    startActivity(intent)
                }
                finish() // Close the current activity
            }
        } else {
            // User is not logged in, navigate to the login activity
            val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
            Toast.makeText(this, "Login to your account", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish() // Close the current activity
        }
    }
}