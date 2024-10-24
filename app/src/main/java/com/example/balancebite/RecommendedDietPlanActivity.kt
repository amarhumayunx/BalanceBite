package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class DiseaseDietPlan(
    val day: Int = 0,
    val breakfast: String = "",
    val lunch: String = "",
    val dinner: String = "",
    val snack: String = ""
)

class RecommendedDietPlanActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var bmiTextView: TextView
    private lateinit var dietPlanTextView: TextView
    private lateinit var bmiCategoryTextView: TextView

    private var userAge: Int? = null
    private var userWeight: Int? = null
    private var userHeight: Int? = null
    private var userHealthInfo: String = "normal" // Default value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_diet_plan)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Link UI components
        bmiTextView = findViewById(R.id.bmiTextView)
        dietPlanTextView = findViewById(R.id.dietPlanTextView)
        bmiCategoryTextView = findViewById(R.id.bmiCategoryTextView)

        // Fetch user profile data from Firebase
        fetchUserData()
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("Users").child(userId).child("profile")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userAge = dataSnapshot.child("age").getValue(Int::class.java)
                            userWeight = dataSnapshot.child("weight").getValue(Int::class.java)
                            userHeight = dataSnapshot.child("height").getValue(Int::class.java)
                            userHealthInfo = dataSnapshot.child("healthInfo").getValue(String::class.java) ?: "normal"

                            if (userHeight != null && userWeight != null) {
                                calculateBMI()
                            } else {
                                showToast("Please complete your profile with valid height and weight.")
                            }
                        } else {
                            showToast("Profile data not found. Please update your profile.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showToast("Error fetching data: ${error.message}")
                    }
                })
        } else {
            showToast("User not logged in.")
            finish() // Close the activity if no user is logged in
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateBMI() {
        val heightInMeters = userHeight?.let { it.toDouble() / 100 } ?: return
        val weight = userWeight ?: return

        val bmi = weight / (heightInMeters * heightInMeters)
        if (bmi.isFinite()) {
            bmiTextView.text = "Your BMI: %.2f".format(bmi)
            val bmiCategory = getBMICategory(bmi) // Get category
            bmiCategoryTextView.text = "BMI Category: $bmiCategory"

            // Fetch and display the diet plan based on BMI category
            fetchDietPlan(bmiCategory) // Fetch the first day's plan
        } else {
            bmiTextView.text = "Invalid BMI. Please update your profile data."
        }
    }

    private fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "underweight"
            bmi in 18.5..24.9 -> "normal_weight"
            bmi in 25.0..29.9 -> "overweight"
            else -> "obesity"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchDietPlan(disease: String) {
        val database = FirebaseDatabase.getInstance().getReference("disease_diet_plans")

        // Navigate to the specified disease category (BMI category) in the database
        database.child(disease).child("plan")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val dietPlans = StringBuilder()

                    // Loop through each day in the plan and append it to the StringBuilder
                    for (daySnapshot in dataSnapshot.children) {
                        val plan = daySnapshot.getValue(DiseaseDietPlan::class.java)
                        plan?.let {
                            dietPlans.append("""
                            Day ${it.day}:
                            Breakfast: ${it.breakfast}
                            Lunch: ${it.lunch}
                            Dinner: ${it.dinner}
                            Snack: ${it.snack}

                        """.trimIndent())
                        }
                    }

                    // Display all diet plans for the BMI category in the TextView
                    dietPlanTextView.text = dietPlans.toString()
                } else {
                    showToast("No diet plans found for $disease.")
                    dietPlanTextView.text = "No diet plan available."
                }
            }
            .addOnFailureListener { exception ->
                showToast("Error fetching data: ${exception.message}")
            }
    }


    // Helper function to display toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}