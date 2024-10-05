package com.example.balancebite

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    // Permission request code for notifications
    private val notificationPermissionCode = 102

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

        // Schedule the notification to repeat every 24 hours
        scheduleProgressNotification()

        // Splash screen delay before navigation
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatusAndNavigate()
        }, 1000)
    }

    private fun scheduleProgressNotification() {
        // Create a PeriodicWorkRequest for ProgressNotificationWorker to run every 24 hours
        val periodicWorkRequest = PeriodicWorkRequestBuilder<ProgressNotificationWorker>(24, TimeUnit.HOURS)
            .build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }


    // Handle the result of permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
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