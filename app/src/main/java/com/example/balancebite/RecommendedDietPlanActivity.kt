package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RecommendedDietPlanActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var bmiTextView: TextView
    private lateinit var dietPlanTextView: TextView
    private lateinit var bmiCategoryTextView: TextView

    private var userAge: Int? = null
    private var userWeight: Int? = null
    private var userHeight: Int? = null
    private lateinit var userHealthInfo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_diet_plan)

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
                            // Retrieve user profile data
                            userAge = dataSnapshot.child("age").getValue(Int::class.java)
                            userWeight = dataSnapshot.child("weight").getValue(Int::class.java)
                            userHeight = dataSnapshot.child("height").getValue(Int::class.java)
                            userHealthInfo = dataSnapshot.child("healthInfo").getValue(String::class.java) ?: "normal"

                            // Validate user data and calculate BMI if applicable
                            if (userHeight != null && userWeight != null)
                            {
                                calculateBMI()
                            }
                            else
                            {
                                showToast("Please complete your profile with valid height and weight.")
                            }
                        }
                        else
                        {
                            showToast("Profile data not found. Please update your profile.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showToast("Error fetching data: ${error.message}")
                    }
                })
        }
        else
        {
            showToast("User not logged in.")
            finish() // Close the activity if no user is logged in
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateBMI() {
        val heightInMeters = userHeight!!.toDouble() / 100
        val bmi = userWeight!!.toDouble() / (heightInMeters * heightInMeters)

        if (bmi.isFinite()) {
            bmiTextView.text = "Your BMI: %.2f".format(bmi)
            val bmiCategory = getBMICategory(bmi)
            bmiCategoryTextView.text = "BMI Category: $bmiCategory"
            fetchDietPlan(bmiCategory)
        } else {
            bmiTextView.text = "Invalid BMI. Please update your profile data."
        }
    }

    private fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    private fun fetchDietPlan(bmiCategory: String)
    {
        val ageGroup = getAgeGroup(userAge)

        if (userHealthInfo == "normal")
        {
            // Fetch normal diet plan for the specific age group
            database.child("dietPlans").child(ageGroup)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            displayDietPlan(dataSnapshot)
                        }
                        else
                        {
                            showToast("Diet plan not found for your age group.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError)
                    {
                        showToast("Error fetching diet plan: ${error.message}")
                    }
                })
        }
        else
        {
            // Fetch diet plan based on health condition
            database.child("diseases_diet_plan").child(userHealthInfo).child("plan")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            displayDietPlan(dataSnapshot)
                        }
                        else
                        {
                            showToast("Diet plan not found for your health condition.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError)
                    {
                        showToast("Error fetching diet plan: ${error.message}")
                    }
                })
        }
    }

    private fun getAgeGroup(age: Int?): String {
        return when (age) {
            in 1..3 -> "1to3Years"
            in 4..6 -> "3to6Years"
            in 7..12 -> "6to12Years"
            in 13..18 -> "12to18Years"
            in 19..24 -> "18to24Years"
            in 25..34 -> "24to34Years"
            in 35..44 -> "34to44Years"
            in 45..54 -> "44to54Years"
            else -> "55YearsandAbove"
        }
    }

    private fun displayDietPlan(dataSnapshot: DataSnapshot) {
        val dietPlan = StringBuilder()
        for (day in 1..7) {
            val dayPlan = dataSnapshot.child("Day$day").getValue(String::class.java)
            dietPlan.append("Day $day: ${dayPlan ?: "Not available"}\n")
        }
        dietPlanTextView.text = dietPlan.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}