package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class UserProfile(
    val name: String = "",
    val age: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val healthInfo: String = ""
)

class UserInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHealthInfo: EditText
    private lateinit var btnSubmit: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Initialize views
        etName = findViewById(R.id.user_name_on_user_info)
        etAge = findViewById(R.id.user_age_on_user_info)
        etHeight = findViewById(R.id.user_height_on_user_info)
        etWeight = findViewById(R.id.user_weight_on_user_info)
        etHealthInfo = findViewById(R.id.user_health_info_on_user_info)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            saveUserInfo()
        }
    }

    private fun saveUserInfo() {
        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim().toIntOrNull()
        val height = etHeight.text.toString().trim().toDoubleOrNull()
        val weight = etWeight.text.toString().trim().toDoubleOrNull()
        val healthInfo = etHealthInfo.text.toString().trim()

        // Check for incomplete information
        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }

        if (age == null || age <= 0) {
            etAge.error = "Please enter a valid age"
            return
        }

        if (height == null || height <= 0.0) {
            etHeight.error = "Please enter a valid height"
            return
        }

        if (weight == null || weight <= 0.0) {
            etWeight.error = "Please enter a valid weight"
            return
        }

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userProfile = UserProfile(name, age, height, weight, healthInfo)

            // Store user profile data in Firebase Realtime Database
            database.child(userId).child("profile").setValue(userProfile).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to the main home screen after saving
                    val intent = Intent(this, MainHomeScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}