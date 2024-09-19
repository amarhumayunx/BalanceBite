package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        // Inflate the binding class for activity_sign_in.xml
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize ProgressBar
        progressBar = binding.progressBar
        progressBar.visibility = View.GONE

        // Set up sign-in button click listener
        binding.buttonOnCardView.setOnClickListener {
            signInUser()
        }

        // Set up sign-up button click listener
        binding.textViewSignUp.setOnClickListener {
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

        // Check if the email format is valid
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        // Show ProgressBar
        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Hide ProgressBar
                progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    // Sign in success, navigate to DashboardActivity
                    navigateToDashboard()
                } else {
                    // Sign in failed, check if user needs to be redirected to SignUpActivity
                    handleSignInFailure(task.exception?.message)
                }
            }
    }

    // Function to check if the email format is valid
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun handleSignInFailure(errorMessage: String?) {
        // Display error message
        Toast.makeText(this, "Sign-In failed: $errorMessage", Toast.LENGTH_SHORT).show()

        // Navigate to SignUpActivity if the account does not exist
        if (errorMessage != null) {
            when {
                errorMessage.contains("There is no user record corresponding to this identifier") -> {
                    Toast.makeText(this, "No account found. Redirecting to Sign-Up page...", Toast.LENGTH_SHORT).show()
                    navigateToSignUp()
                }
                errorMessage.contains("The password is invalid") -> {
                    Toast.makeText(this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show()
                }
                errorMessage.contains("The email address is already in use") -> {
                    Toast.makeText(this, "Email already in use. Please try signing in.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Sign-In failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainHomeScreen::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the sign-in activity
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, activity_sign_up::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the sign-in activity
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Sign Up Page", Toast.LENGTH_SHORT).show()
        navigateToSignUp()
    }
}