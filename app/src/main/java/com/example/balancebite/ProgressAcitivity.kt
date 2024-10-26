package com.example.balancebite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProgressActivity : AppCompatActivity() {

    private lateinit var inputDay: EditText
    private lateinit var inputCalories: EditText
    private lateinit var inputWater: EditText
    private lateinit var inputExerciseTime: EditText
    private lateinit var inputWeight: EditText
    private lateinit var submitProgressButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_activity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        inputDay = findViewById(R.id.inputDay)
        inputCalories = findViewById(R.id.inputCalories)
        inputWater = findViewById(R.id.inputWater)
        inputExerciseTime = findViewById(R.id.inputExerciseTime)
        inputWeight = findViewById(R.id.inputWeight)
        submitProgressButton = findViewById(R.id.submitProgressButton)

        submitProgressButton.setOnClickListener {
            submitProgress()
        }
    }

    private fun submitProgress() {
        val day = inputDay.text.toString()
        val calories = inputCalories.text.toString().toIntOrNull() ?: 0
        val water = inputWater.text.toString().toIntOrNull() ?: 0
        val exerciseTime = inputExerciseTime.text.toString().toIntOrNull() ?: 0
        val weight = inputWeight.text.toString().toDoubleOrNull() ?: 0.0

        // Store the progress information in Firebase
        storeProgressInFirebase(day, calories, water, exerciseTime, weight)
    }

    private fun storeProgressInFirebase(day: String, calories: Int, water: Int, exerciseTime: Int, weight: Double) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseRef = FirebaseDatabase.getInstance().getReference("Users/$userId/Progress")

        // Create a map to store the data
        val progressData = mapOf(
            "Day" to day,
            "Calories" to calories,
            "Water" to water,
            "Exercise Time" to exerciseTime,
            "Weight" to weight
        )

        // Push data to Firebase
        databaseRef.push().setValue(progressData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Progress submitted successfully", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Failed to submit progress", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputs() {
        inputDay.text.clear()
        inputCalories.text.clear()
        inputWater.text.clear()
        inputExerciseTime.text.clear()
        inputWeight.text.clear()
    }

}