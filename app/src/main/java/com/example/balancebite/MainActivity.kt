package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
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

        // Load the fade-in animation for the layout and logo
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeInLogoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Find the ImageView for the logo and ConstraintLayout for the main layout
        val logoImageView = findViewById<ImageView>(R.id.image_one)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        val logoSlideInAnim = AnimationUtils.loadAnimation(this, R.anim.logo_slide_in)

        // Apply the animation to the logo image
        logoImageView.startAnimation(logoSlideInAnim)


        // Start the fade-in animation on the logo
        logoImageView.startAnimation(fadeInLogoAnim)

        // Optionally animate the whole layout (main layout)
        mainLayout.startAnimation(fadeInAnim)

        // If necessary, request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // Splash screen delay before navigation
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatusAndNavigate()
        }, 1000)
    }

    // Function to check internet connectivity
    @SuppressLint("ObsoleteSdkInt")
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    // Check if the user is authenticated and has the required data in Firebase
    private fun checkUserStatusAndNavigate() {
        if (!isInternetAvailable()) {
            val intent = Intent(this, NoInternetActivity::class.java)
            startActivity(intent)
            finish()
            return // Exit the method if there's no internet
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Check if user data exists in Firebase Database
            val userId = currentUser.uid
            val userRef = database.getReference("Users").child(userId).child("profile")

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        val userData = task.result
                        // Check for all required data fields in the profile node
                        val hasAllRequiredData = listOf("name", "age", "weight", "height", "healthInfo").all { key ->
                            userData.child(key).exists()
                        }

                        if (hasAllRequiredData) {
                            // User data exists and is complete, navigate to the dashboard with a slide-in effect
                            navigateToActivityWithSlide(MainHomeScreen::class.java, "Moving to Dashboard")
                        } else {
                            // User data is incomplete, navigate to the login activity with a slide-in effect
                            navigateToActivityWithSlide(MainActivityAfterSplashScreen::class.java, "Incomplete user data. Please log in again.")
                        }
                    } else {
                        // No user data exists, navigate to the login activity with a slide-in effect
                        navigateToActivityWithSlide(MainActivityAfterSplashScreen::class.java, "No user data found. Please log in.")
                    }
                } else {
                    // Error occurred while fetching user data
                    showToast("Error checking user data: ${task.exception?.message}")
                    navigateToActivityWithSlide(MainActivityAfterSplashScreen::class.java)
                }
            }
        } else {
            // User is not logged in, navigate to the login activity with a slide-in effect
            navigateToActivityWithSlide(MainActivityAfterSplashScreen::class.java, "Please log in to your account")
        }
    }

    // Helper method to navigate to an activity with a slide-in animation
    private fun navigateToActivityWithSlide(activityClass: Class<*>, message: String? = null) {
        message?.let { showToast(it) }
        val intent = Intent(this, activityClass)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Slide animation
        finish() // Close the current activity
    }

    // Helper method to show a toast message with a fade-in animation
    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.view?.let {
            // Apply a fade-in animation to the toast
            ViewCompat.animate(it).alpha(0f).alpha(1f).setDuration(500).start()
        }
        toast.show()
    }

    // Override onBackPressed method to handle double press to exit with an animation
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed() // If back was pressed once, exit the app
            return
        }

        this.backPressedOnce = true
        showToast("Press back again to exit")

        // Reset the backPressedOnce flag after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }
}