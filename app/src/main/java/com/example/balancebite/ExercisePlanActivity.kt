package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExercisePlanActivity : AppCompatActivity() {

    private lateinit var ageEditText: EditText
    private lateinit var bmiSpinner: Spinner
    private lateinit var fitnessLevelGroup: RadioGroup
    private lateinit var exerciseTypeSpinner: Spinner
    private lateinit var generatePlanButton: Button
    private lateinit var progressBar: LinearProgressIndicator

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_plan)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressIndicator)

        // Initialize UI components
        ageEditText = findViewById(R.id.ageEditText)
        bmiSpinner = findViewById(R.id.bmiSpinner)
        fitnessLevelGroup = findViewById(R.id.fitnessLevelGroup)
        exerciseTypeSpinner = findViewById(R.id.exerciseTypeSpinner)
        generatePlanButton = findViewById(R.id.generatePlanButton)

        // Set up BMI Spinner with a placeholder
        val bmiOptions = arrayOf("Select BMI", "Underweight", "Normal", "Overweight", "Obese")
        val bmiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bmiOptions)
        bmiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bmiSpinner.adapter = bmiAdapter

        // Set up the exercise type spinner with options
        val exerciseTypes = listOf(
            "Cardio",
            "Strength",
            "Flexibility",
            "Low Impact",
            "High Intensity",
            "Recovery"
        )
        val exerciseTypeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            exerciseTypes
        )
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseTypeSpinner.adapter = exerciseTypeAdapter

        // Fetch and display user data from Firebase
        fetchUserData()

        // Set listener to handle fitness level changes
        fitnessLevelGroup.setOnCheckedChangeListener { _, checkedId ->
            val fitnessLevel = findViewById<RadioButton>(checkedId)?.text.toString()
            updateExerciseTypeOptions(fitnessLevel)
        }

        // Generate exercise plan on button click
        generatePlanButton.setOnClickListener {
            generateExercisePlan()
        }
    }

    // Function to fetch user's age, height, and weight from Firebase
    private fun fetchUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userProfileRef = database.child("Users").child(userId).child("profile")

        userProfileRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Fetch age, height, and weight values from Firebase
                    val age = snapshot.child("age").getValue(Int::class.java) ?: 0
                    val height = snapshot.child("height").getValue(Double::class.java) ?: 0.0
                    val weight = snapshot.child("weight").getValue(Double::class.java) ?: 0.0

                    if (height > 0 && weight > 0) {
                        // Calculate BMI
                        val bmi = calculateBMI(weight, height)
                        val bmiCategory = getBMICategory(bmi)

                        // Set the BMI category based on the calculated BMI
                        val bmiIndex = when (bmiCategory) {
                            "Underweight" -> 1
                            "Normal" -> 2
                            "Overweight" -> 3
                            "Obese" -> 4
                            else -> 0 // Default to "Select BMI"
                        }
                        bmiSpinner.setSelection(bmiIndex)
                    } else {
                        bmiSpinner.setSelection(0) // Default to "Select BMI"
                    }

                    // Set the fetched age value
                    ageEditText.setText(age.toString())
                } else {
                    Toast.makeText(this@ExercisePlanActivity, "User profile not found", Toast.LENGTH_SHORT).show()
                    bmiSpinner.setSelection(0) // Default to "Select BMI"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExercisePlanActivity, "Error fetching user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to calculate BMI
    private fun calculateBMI(weight: Double, height: Double): Double {
        return if (height > 0) {
            weight / (height * height)
        } else {
            0.0
        }
    }

    // Function to get BMI category based on calculated BMI
    private fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obese"
        }
    }

    // Function to update available exercise types based on the selected fitness level
    private fun updateExerciseTypeOptions(fitnessLevel: String) {
        val allExerciseTypes = listOf("Cardio", "Strength", "Flexibility", "Low Impact", "High Intensity", "Recovery")
        val filteredExerciseTypes = when {
            fitnessLevel.equals("Beginner", ignoreCase = true) -> listOf("Cardio", "Low Impact", "Recovery")
            fitnessLevel.equals("Intermediate", ignoreCase = true) -> listOf("Cardio", "Strength", "Flexibility", "Low Impact")
            fitnessLevel.equals("Advanced", ignoreCase = true) -> listOf("Strength", "High Intensity")
            else -> allExerciseTypes
        }

        // Update exercise type spinner with filtered options
        val exerciseTypeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filteredExerciseTypes
        )
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseTypeSpinner.adapter = exerciseTypeAdapter

        // Debugging output to check the available options
        println("Filtered exercise types: $filteredExerciseTypes")
    }

    // Function to generate exercise plan based on user inputs
    private fun generateExercisePlan() {
        val age = ageEditText.text.toString().toIntOrNull() // Get age
        val bmi = bmiSpinner.selectedItem.toString() // Get selected BMI
        val fitnessLevelId = fitnessLevelGroup.checkedRadioButtonId // Get selected fitness level
        val fitnessLevel = findViewById<RadioButton>(fitnessLevelId)?.text.toString() // Get fitness level text
        val exerciseType = exerciseTypeSpinner.selectedItem.toString() // Get selected exercise type

        // Validate age input
        if (age == null || age <= 0) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress bar before generating the plan
        progressBar.visibility = View.VISIBLE

        // Create an Intent to pass data
        val intent = Intent(this, ExercisePlanShownActivity::class.java)
        intent.putExtra("age", age)
        intent.putExtra("bmi", bmi)
        intent.putExtra("fitnessLevel", fitnessLevel)
        intent.putExtra("exerciseType", exerciseType)

        // Start the activity and hide the progress bar
        startActivity(intent)
        progressBar.visibility = View.GONE
    }
}