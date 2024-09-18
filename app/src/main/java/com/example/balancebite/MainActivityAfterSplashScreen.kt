package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.balancebite.databinding.ActivityMainAfterSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class MainActivityAfterSplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainAfterSplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the binding variable using the layout inflater
        binding = ActivityMainAfterSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if the user is already signed in
        if (auth.currentUser != null)
        {
            // User is signed in, navigate to the DashboardActivity
            navigateToDashboard()
            Toast.makeText(this, "Signed In Successfully!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            // Set up button click listeners to navigate to LoginPageActivity
            binding.buttonGetStarted.setOnClickListener {
                navigateToLoginPage()
            }

            binding.textViewSignUp.setOnClickListener {
                navigateToSignUpPage()
            }
        }
    }

    private fun navigateToSignUpPage(){
        val intent = Intent(this,activity_sign_up::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the current activity
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainHomeScreen::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the splash screen activity
    }

    // Override onBackPressed to shut down the app
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Exiting App",Toast.LENGTH_SHORT).show()
        // Finish the current activity and exit the app
        finishAffinity()
    }
}
