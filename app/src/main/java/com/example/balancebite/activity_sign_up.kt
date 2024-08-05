package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.balancebite.databinding.ActivitySignUpBinding

class activity_sign_up : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the binding class for activity_sign_up.xml
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up button click listener to sign up a new user
        binding.buttonOnCardView.setOnClickListener {
            signUpNewUser()
        }
    }

    private fun signUpNewUser() {
        val email = binding.emailAddressIdOnCardViewForEnterEmail.text.toString().trim()
        val password = binding.passwordIdOnCardViewForEnterPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new user with the provided email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    Toast.makeText(this, "Sign-Up successful! Redirecting to Home Page...", Toast.LENGTH_SHORT).show()

                    // Redirect to SignInActivity
                    val intent = Intent(this, MainHomeScreen::class.java)
                    startActivity(intent)
                    finish() // Close the sign-up activity
                } else {
                    // If sign-up fails, check the exception to determine if it's due to an existing email
                    handleSignUpFailure(task.exception?.message)
                }
            }
    }

    private fun handleSignUpFailure(errorMessage: String?) {
        // Display error message
        Toast.makeText(this, "Sign-Up failed: $errorMessage", Toast.LENGTH_SHORT).show()

        // Check if the failure is due to an existing email and redirect to Sign-In Activity
        if (errorMessage != null && errorMessage.contains("The email address is already in use by another account")) {
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
            finish() // Close the sign-up activity
        }
    }
}
