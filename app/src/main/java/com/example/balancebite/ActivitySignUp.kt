package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActivitySignUP : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        applyEntryAnimations()

        binding.buttonOnCardView.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_bounce))
            signUpNewUser()
        }
    }

    private fun signUpNewUser() {
        val email = binding.emailAddressIdOnCardViewForEnterEmail.text.toString().trim()
        val password = binding.passwordIdOnCardViewForEnterPassword.text.toString().trim()
        val username = binding.userNameForEmail.text.toString().trim()

        // Validate user input
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Send verification email
                    sendVerificationEmail()

                    Toast.makeText(
                        this,
                        "Account created. Verification email sent to $email",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Optionally redirect user to login page
                    val intent = Intent(this, LoginPageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Sign-Up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent successfully. Please check your inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send verification email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Get Started Activity", Toast.LENGTH_SHORT).show()
        navigateBackToActivityAfterSplashScreen()
    }

    private fun navigateBackToActivityAfterSplashScreen() {
        val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun applyEntryAnimations() {
        binding.root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        binding.cardView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
    }
}