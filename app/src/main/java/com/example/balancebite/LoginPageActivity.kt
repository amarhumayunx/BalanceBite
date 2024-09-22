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
import com.google.firebase.database.*

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
        progressBar = binding.progressBar
        progressBar.visibility = View.GONE

        // Check if the user is already logged in
        checkLoginStatus()

        // Set up sign-in button click listener
        binding.buttonOnCardView.setOnClickListener {
            signInUser()
        }

        // Set up sign-up button click listener
        binding.textViewSignUp.setOnClickListener {
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
                    // Sign in success, check if the user is an admin
                    checkAdminStatus(auth.currentUser?.uid ?: "")
                } else {
                    // Sign in failed, handle the error
                    handleSignInFailure(task.exception?.message)
                }
            }
    }

    // Check if the user is an admin
    private fun checkAdminStatus(userId: String) {
        val database = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        database.child("email").get().addOnSuccessListener { snapshot ->
            val email = snapshot.value.toString()

            // Check if the email is that of the admin
            if (email == "amarhumayun@outlook.com")
            {
                // Navigate to Admin Dashboard
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Humayun Amar is the admin of this app", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // Navigate to regular user dashboard or handle non-admin case
                checkUserInfoAndNavigate()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
        }
    }

    // Check if the user's personal information exists in the database
    private fun checkUserInfoAndNavigate() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userInfo = task.result
                    if (userInfo.exists()) {
                        // Retrieve the profile information from the database
                        val profile = userInfo.child("profile")

                        val name = profile.child("name").value as? String
                        val height = profile.child("height").value
                        val weight = profile.child("weight").value
                        val healthInfo = profile.child("healthInfo").value
                        val profilePictureUrl = profile.child("profilePictureUrl").value as? String

                        // If all personal information exists, navigate to the dashboard
                        if (name != null && height != null && weight != null && healthInfo != null) {
                            navigateToDashboard(name)
                        } else {
                            // Missing personal information, navigate to UserInfoActivity
                            navigateToUserInfoPage()
                        }
                    } else {
                        // No user data exists, navigate to UserInfoActivity
                        navigateToUserInfoPage()
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
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

    // Handle back button press
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Sign Up Page", Toast.LENGTH_SHORT).show()
        navigateToSignUp()
    }
}