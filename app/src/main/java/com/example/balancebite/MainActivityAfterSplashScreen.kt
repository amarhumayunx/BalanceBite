package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivityMainAfterSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class MainActivityAfterSplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainAfterSplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize the binding variable using the layout inflater
        binding = ActivityMainAfterSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Add animations to elements
        animateElements()

        // Check if the user is already signed in
        if (auth.currentUser != null) {
            // User is signed in, navigate to the DashboardActivity
            navigateToDashboard()
            Toast.makeText(this, "Signed In Successfully!", Toast.LENGTH_SHORT).show()
        } else {
            // Set up button click listeners to navigate to LoginPageActivity
            binding.buttonGetStarted.setOnClickListener {
                navigateToLoginPage()
            }

            binding.textViewSignUp.setOnClickListener {
                navigateToSignUpPage()
            }
        }
    }

    private fun animateElements() {
        // Animate the app name text
        val appNameAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        binding.appNameOnMainActivity.startAnimation(appNameAnimation)

        // Animate the image
        val imageAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        binding.imageOnActivityAfterSplashScreen.startAnimation(imageAnimation)

        // Animate the description text
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.textViewActivityAfterSplashScreen.startAnimation(textAnimation)

        // Animate the "Get Started" button
        val buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        binding.buttonGetStarted.startAnimation(buttonAnimation)

        // Animate the sign-up text
        val signUpTextAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.textViewSignUp.startAnimation(signUpTextAnimation)
    }

    private fun navigateToSignUpPage() {
        val intent = Intent(this, ActivitySignUP::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Apply animation
        finish()
    }

    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Apply animation
        finish()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainHomeScreen::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Apply animation
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Exiting App", Toast.LENGTH_SHORT).show()
        finishAffinity() // Finish all activities and exit the app
    }
}