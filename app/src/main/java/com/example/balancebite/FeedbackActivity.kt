package com.example.balancebite

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FeedbackActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        val submitButton: Button = findViewById(R.id.submit_feedback_button)
        val feedbackInput: EditText = findViewById(R.id.feedback_input)
        val ratingBar: RatingBar = findViewById(R.id.rating_bar)

        // Get the current signed-in user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set the listener for the Submit button
            submitButton.setOnClickListener {
                val feedbackText = feedbackInput.text.toString()
                val rating = ratingBar.rating

                if (feedbackText.isNotEmpty()) {
                    // Submit the feedback for the current user
                    submitFeedback(currentUser.uid, feedbackText, rating)
                } else {
                    Toast.makeText(this, "Please provide feedback", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // If no user is logged in, display a message or redirect to login
            Toast.makeText(this, "Please sign in to provide feedback", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitFeedback(userId: String, feedback: String, rating: Float) {
        // Get Firebase Database reference
        val database = FirebaseDatabase.getInstance().reference

        // Create a unique key for each feedback entry
        val feedbackId = database.push().key

        // Structure the feedback data
        val feedbackData = hashMapOf(
            "feedback" to feedback,
            "rating" to rating
        )

        // Save the feedback under the user's unique ID in the database
        feedbackId?.let {
            database.child("Users").child(userId).child("feedback").child(it).setValue(feedbackData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
