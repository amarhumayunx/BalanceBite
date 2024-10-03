package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.balancebite.databinding.ActivityEditUserBinding
import com.google.firebase.database.FirebaseDatabase


class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    private val database = FirebaseDatabase.getInstance().reference.child("Users")
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize ViewBinding
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the userId passed from the previous activity (Admin Dashboard)
        userId = intent.getStringExtra("userId").toString()

        // Load user data from Firebase
        loadUserData()

        // Set click listeners for Save and Delete buttons using binding
        binding.buttonSave.setOnClickListener { saveUserData() }
        binding.buttonDelete.setOnClickListener { deleteUser() }
    }

    // Function to load user data from Firebase
    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        database.child(userId).child("profile").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val name = snapshot.child("name").value as String
                val age = snapshot.child("age").value as Long
                val height = snapshot.child("height").value as Long
                val weight = snapshot.child("weight").value as Long
                val healthInfo = snapshot.child("healthInfo").value as String

                // Populate the EditText fields using binding
                binding.editName.setText(name)
                binding.editAge.setText(age.toString())
                binding.editHeight.setText(height.toString())
                binding.editWeight.setText(weight.toString())
                binding.editHealthInfo.setText(healthInfo)
            } else {
                Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to save the updated user data to Firebase
    private fun saveUserData() {
        val updatedName = binding.editName.text.toString().trim()
        val updatedAge = binding.editAge.text.toString().toIntOrNull()
        val updatedHeight = binding.editHeight.text.toString().toIntOrNull()
        val updatedWeight = binding.editWeight.text.toString().toIntOrNull()
        val updatedHealthInfo = binding.editHealthInfo.text.toString().trim()

        // Validate the inputs before saving
        if (updatedName.isEmpty() || updatedAge == null || updatedHeight == null || updatedWeight == null) {
            Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        // Update user data in Firebase
        val userUpdates = mapOf(
            "name" to updatedName,
            "age" to updatedAge,
            "height" to updatedHeight,
            "weight" to updatedWeight,
            "healthInfo" to updatedHealthInfo
        )

        database.child(userId).child("profile").updateChildren(userUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User data updated successfully.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update user data.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to delete the user profile from Firebase
    private fun deleteUser() {
        database.child(userId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User profile deleted.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to delete user profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
