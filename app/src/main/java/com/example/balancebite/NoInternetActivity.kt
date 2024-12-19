package com.example.balancebite

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class NoInternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_no_internet)

        // Get references to the views
        val noInternetImage = findViewById<ImageView>(R.id.image_on_no_internet)
        val noInternetText = findViewById<TextView>(R.id.no_internet_connection)
        val internetLine = findViewById<TextView>(R.id.internet_line)

        // Load animations
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate)

        // Apply animations to the views
        noInternetImage.startAnimation(fadeInAnim)  // Fade in the no internet image
        noInternetImage.startAnimation(rotateAnim)  // Add rotation to the no internet image
        noInternetText.startAnimation(slideUpAnim)  // Slide up the 'No Internet' text
        internetLine.startAnimation(scaleUpAnim)  // Scale up the internet line text

        // Optional: Add delays for better effect
        noInternetText.postDelayed({
            noInternetText.startAnimation(slideUpAnim)
        }, 1000)  // Add a delay of 1 second before the text slides up

        internetLine.postDelayed({
            internetLine.startAnimation(scaleUpAnim)
        }, 1500)  // Add a delay of 1.5 seconds before the "internet line" text scales up
    }
}
