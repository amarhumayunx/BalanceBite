package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        // Initialize ProgressBar
        progressBar = binding.progressIndicator
        progressBar.visibility = View.GONE

        // Apply entry animations
        applyEntryAnimations()

        // Check if the user is already logged in
        checkLoginStatus()

        // Set up sign-in button click listener with bounce animation
        binding.buttonOnCardView.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_bounce))
            signInUser()
        }

        // Set up sign-up button click listener with bounce animation
        binding.textViewSignUp.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_bounce))
            navigateToSignUp()
        }
    }

    // Check login status to see if a user is already signed in
    private fun checkLoginStatus() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is already logged in, check user info in the database
            Toast.makeText(this, "User is already logged in", Toast.LENGTH_SHORT).show()
            checkUserInfoAndNavigate()
        } else {
            // No user is logged in, user needs to log in
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show()
        }
    }

    // Sign-in logic for users
    private fun signInUser() {
        val email = binding.emailAddressIdOnCardViewForEnterEmail.text.toString().trim()
        val password = binding.passwordIdOnCardViewForEnterPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign-in successful, navigate based on user data
                    Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                    checkUserInfoAndNavigate()
                } else {
                    // Sign-in failed, show error message
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    handleSignInFailure(errorMessage)
                }
            }
    }

    // Check if the user's personal information exists in the database
    private fun checkUserInfoAndNavigate() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).child("profile").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profile = task.result
                    if (profile.exists()) {
                        // Retrieve the profile information from the database
                        val name = profile.child("name").value as? String
                        val height = profile.child("height").value
                        val weight = profile.child("weight").value
                        val healthInfo = profile.child("healthInfo").value
                        val age = profile.child("age").value
                        val gender = profile.child("gender").value

                        // If all personal information exists, navigate to the dashboard
                        if (name != null && height != null && weight != null && healthInfo != null && gender != null && age != null) {
                            navigateToDashboard(name)
                        } else {
                            // Missing personal information, navigate to UserInfoActivity
                            Toast.makeText(this, "Please complete your profile information.", Toast.LENGTH_SHORT).show()
                            navigateToUserInfoPage()
                        }
                    } else {
                        // No user data exists, navigate to UserInfoActivity
                        Toast.makeText(this, "No user data exists", Toast.LENGTH_SHORT).show()
                        navigateToUserInfoPage()
                    }
                } else {
                    // Handle error if fetching profile data fails
                    Toast.makeText(this, "Error fetching user data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle the case where userId is null (user not logged in)
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Navigate to the dashboard activity
    private fun navigateToDashboard(name: String) {
        val intent = Intent(this, MainHomeScreen::class.java)
        intent.putExtra("username", name)
        startActivity(intent)
        finish()
    }

    // Navigate to the UserInfoActivity if personal data is missing
    private fun navigateToUserInfoPage() {
        val intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Validate email format
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Handle sign-in failures
    private fun handleSignInFailure(errorMessage: String?) {
        Toast.makeText(this, "Sign-In failed: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    // Navigate to sign-up activity
    private fun navigateToSignUp() {
        val intent = Intent(this, ActivitySignUP::class.java)
        startActivity(intent)
        finish()
    }

    // Apply animations for entry and views
    private fun applyEntryAnimations() {
        binding.root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        binding.cardView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
    }

    // Handle back button press
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Sign Up Page", Toast.LENGTH_SHORT).show()
        navigateToSignUp()
    }
}