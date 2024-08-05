package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.balancebite.databinding.ActivityLoginPageBinding

class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the binding class for activity_sign_in.xml
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is already signed in
        if (auth.currentUser != null) {
            // User is signed in, redirect to DashboardActivity
            navigateToDashboard()
        }

        // Set up sign-in button click listener
        binding.buttonOnCardView.setOnClickListener {
            signInUser()
        }

        // Set up sign-up button click listener
        binding.buttonOnCardView.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun signInUser() {
        val email = binding.emailAddressIdOnCardViewForEnterEmail.text.toString().trim()
        val password = binding.passwordIdOnCardViewForEnterPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to HomeActivity
                    navigateToHome()
                } else {
                    // Sign in failed, check if user needs to be redirected to SignUpActivity
                    handleSignInFailure(task.exception?.message)
                }
            }
    }

    private fun handleSignInFailure(errorMessage: String?) {
        // Display error message
        Toast.makeText(this, "Sign-In failed: $errorMessage", Toast.LENGTH_SHORT).show()

        // Navigate to SignUpActivity if the account does not exist
        if (errorMessage != null && errorMessage.contains("There is no user record corresponding to this identifier")) {
            navigateToSignUp()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainHomeScreen::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the sign-in activity
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, activity_sign_up::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the sign-in activity
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainHomeScreen::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the sign-in activity
    }
}