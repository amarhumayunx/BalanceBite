@file:Suppress("DEPRECATION")

package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import java.util.Locale

class ExercisePlanShownActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_plan_shown)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize UI components
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val bmiTextView: TextView = findViewById(R.id.bmiTextView)
        val fitnessLevelTextView: TextView = findViewById(R.id.fitnessLevelTextView)
        val exerciseTypeTextView: TextView = findViewById(R.id.exerciseTypeTextView)

        // Get data from Intent
        val age = intent.getIntExtra("age", 0)
        val bmi = intent.getStringExtra("bmi")
        val fitnessLevel = intent.getStringExtra("fitnessLevel")
        val exerciseType = intent.getStringExtra("exerciseType")

        // Populate data in TextViews
        ageTextView.text = "Age: $age"
        bmiTextView.text = "BMI: $bmi"
        fitnessLevelTextView.text = "Fitness Level: $fitnessLevel"
        exerciseTypeTextView.text = "Exercise Type: $exerciseType"

        // Fetch the exercise plan from Firebase based on the exercise type and fitness level
        fetchExercisePlan(exerciseType, fitnessLevel)
    }

    // Function to fetch exercise plan from Firebase
    private fun fetchExercisePlan(exerciseType: String?, fitnessLevel: String?) {
        if (exerciseType == null) {
            Toast.makeText(this, "No exercise type selected", Toast.LENGTH_SHORT).show()
            return
        }

        // Accessing Firebase data for the specific exercise type
        val exerciseRef = database.child("exercises").child(exerciseType.lowercase(Locale.ROOT))

        exerciseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Initialize a string to hold exercise details
                    val exerciseDetails = StringBuilder()

                    // Loop through the exercises in the selected category
                    for (exerciseSnapshot in snapshot.children) {
                        // Get the exercise name and clean it up (replace underscores with spaces and capitalize each word)
                        val name = exerciseSnapshot.key?.replace("_", " ")?.split(" ")?.joinToString(" ") { it.capitalize(Locale.ROOT) }
                        val level = exerciseSnapshot.child("level").getValue(String::class.java)
                        val duration = exerciseSnapshot.child("duration").getValue(String::class.java)
                        val calories = exerciseSnapshot.child("calories").getValue(Int::class.java)
                        val sets = exerciseSnapshot.child("sets").getValue(Int::class.java)
                        val reps = exerciseSnapshot.child("reps").getValue(Int::class.java)

                        // Filter exercises based on the user's fitness level (if provided)
                        if (fitnessLevel != null && level != null && (level == fitnessLevel || level == "all")) {
                            // Add the exercise details to the string builder
                            exerciseDetails.append("Exercise: $name\n")
                            exerciseDetails.append("Level: $level\n")
                            if (duration != null) exerciseDetails.append("Duration: $duration\n")
                            if (calories != null) exerciseDetails.append("Calories Burned: $calories\n")
                            if (sets != null) exerciseDetails.append("Sets: $sets\n")
                            if (reps != null) exerciseDetails.append("Reps: $reps\n")
                            exerciseDetails.append("\n")
                        }
                    }

                    // Display the exercise plan details
                    val planDescriptionTextView: TextView = findViewById(R.id.planDescriptionTextView)
                    if (exerciseDetails.isNotEmpty()) {
                        planDescriptionTextView.text = exerciseDetails.toString()
                    } else {
                        planDescriptionTextView.text = "No exercises available for the selected preferences."
                    }
                } else {
                    // Handle the case where no exercise plan exists for the selected type
                    Toast.makeText(this@ExercisePlanShownActivity, "No exercise plan found for this type", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors during fetching data
                Toast.makeText(this@ExercisePlanShownActivity, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}