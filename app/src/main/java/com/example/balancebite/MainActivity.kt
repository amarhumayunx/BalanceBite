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

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    // Variable to track back press time
    private var backPressedOnce = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // Splash screen delay before navigation
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatusAndNavigate()
        }, 1000)
    }

    // Check if the user is authenticated and has the required data in Firebase
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
                        val hasRequiredData = userData.any { it.key in listOf("name", "age", "weight", "height", "healthInfo") }
                        if (hasRequiredData) {
                            // User data exists and is complete, navigate to the dashboard
                            navigateToActivity(MainHomeScreen::class.java, "Moving to Dashboard")
                        } else {
                            // User data is incomplete, navigate to the login activity
                            navigateToActivity(MainActivityAfterSplashScreen::class.java, "Incomplete user data. Please log in again.")
                        }
                    } else {
                        // No user data exists, navigate to the login activity
                        navigateToActivity(MainActivityAfterSplashScreen::class.java, "No user data found. Please log in.")
                    }
                } else {
                    // Error occurred while fetching user data
                    showToast("Error checking user data: ${task.exception?.message}")
                    navigateToActivity(MainActivityAfterSplashScreen::class.java)
                }
            }
        } else {
            // User is not logged in, navigate to the login activity
            navigateToActivity(MainActivityAfterSplashScreen::class.java, "Please log in to your account")
        }
    }

    // Helper method to navigate to an activity with a toast message
    private fun navigateToActivity(activityClass: Class<*>, message: String? = null) {
        message?.let { showToast(it) }
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Close the current activity
    }

    // Helper method to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Override onBackPressed method to handle double press to exit
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed() // If back was pressed once, exit the app
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        // Reset the backPressedOnce flag after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }
}