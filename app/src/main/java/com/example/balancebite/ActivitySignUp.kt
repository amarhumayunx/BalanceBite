package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class ActivitySignUP : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Set up the Sign-Up button click listener
        binding.buttonOnCardView.setOnClickListener {
            signUpNewUser()
        }
    }

    // Function to sign up a new user
    private fun signUpNewUser() {
        val email = binding.emailAddressIdOnCardViewForEnterEmail.text.toString().trim()
        val password = binding.passwordIdOnCardViewForEnterPassword.text.toString().trim()
        val username = binding.userNameForEmail.text.toString().trim()

        // Validate user input
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the email format is valid
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        // Show the ProgressBar and toast message
        binding.progressBar.visibility = View.VISIBLE
        Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show()

        // Create a new user with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE // Hide ProgressBar when done
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Save the user information in Firebase Realtime Database
                        saveBasicUserInfo(userId, username, email)
                    } else {
                        Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleSignUpFailure(task.exception)
                }
            }
    }

    // Function to save basic user information to Firebase Realtime Database
    private fun saveBasicUserInfo(userId: String, username: String, email: String) {
        val database = FirebaseDatabase.getInstance().getReference("Users")

        val userProfile = mapOf(
            "username" to username,
            "email" to email
        )

        database.child(userId).child("profile").setValue(userProfile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign-Up successful! Please enter your personal information.", Toast.LENGTH_SHORT).show()
                    // Move to UserInfoActivity to enter additional details
                    val intent = Intent(this, UserInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save user profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Function to handle sign-up failure
    private fun handleSignUpFailure(exception: Exception?) {
        if (exception != null) {
            if (exception.message?.contains("Email address is already in use") == true) {
                Toast.makeText(this, "Email already in use. Redirecting to sign-in page...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginPageActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Sign-Up failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate the email format
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to navigate back to the activity after splash screen
    private fun navigateBackToActivityAfterSplashScreen() {
        val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
        startActivity(intent)
        finish()
    }

    // Handle the back button press to navigate back to the activity after splash screen
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Get Started Activity", Toast.LENGTH_SHORT).show()
        navigateBackToActivityAfterSplashScreen()
    }
}