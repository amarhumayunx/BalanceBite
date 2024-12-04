@file:Suppress("DEPRECATION")

package com.example.balancebite

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale
import android.text.SpannableStringBuilder

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

        // Add animations
        addAnimationsToViews()
    }

    private fun addAnimationsToViews() {
        // Fade-in animation for the BMI and Diet Plan TextViews
        val fadeInAnimatorBmi = ObjectAnimator.ofFloat(bmiTextView, "alpha", 0f, 1f)
        fadeInAnimatorBmi.duration = 800
        fadeInAnimatorBmi.start()

        val fadeInAnimatorCategory = ObjectAnimator.ofFloat(bmiCategoryTextView, "alpha", 0f, 1f)
        fadeInAnimatorCategory.duration = 800
        fadeInAnimatorCategory.start()

        val fadeInAnimatorDietPlan = ObjectAnimator.ofFloat(dietPlanTextView, "alpha", 0f, 1f)
        fadeInAnimatorDietPlan.duration = 800
        fadeInAnimatorDietPlan.start()
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
            displayBMI(bmi)
            val bmiCategory = getBMICategory(bmi)
            displayBMICategory(bmi)

            // Fetch and display the diet plan based on BMI category
            fetchDietPlan(bmiCategory)
        } else {
            bmiTextView.text = "Invalid BMI. Please update your profile data."
        }
    }

    private fun displayBMICategory(bmi: Double) {
        val bmiCategoryDisplay = getBMICategoryForDisplay(bmi).lowercase()

        val categoryText = "BMI Category: ${bmiCategoryDisplay.capitalize(Locale.ROOT)}"
        val spannableString = SpannableString(categoryText)

        val color = when (bmiCategoryDisplay) {
            "underweight" -> ContextCompat.getColor(this,R.color.blue)
            "normal weight" -> ContextCompat.getColor(this,R.color.green)
            "overweight" -> ContextCompat.getColor(this,R.color.yellow)
            "obesity" -> ContextCompat.getColor(this,R.color.red)
            else -> ContextCompat.getColor(this,R.color.gray)
        }

        val startIndex = categoryText.indexOf(bmiCategoryDisplay.capitalize(Locale.ROOT))
        val endIndex = startIndex + bmiCategoryDisplay.length

        spannableString.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bmiCategoryTextView.text = spannableString
    }

    private fun displayBMI(bmi: Double) {
        // Create the BMI text
        val bmiText = "Your BMI: %.1f".format(bmi)
        val spannableString = SpannableString(bmiText)

        // Apply black color to the text "Your BMI: "
        spannableString.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0, // Start index for "Your BMI: "
            10, // End index for "Your BMI: "
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Determine the color based on the BMI category
        val bmiColor = when {
            bmi < 18.5 -> ContextCompat.getColor(this, R.color.blue) // Underweight
            bmi < 24.9 -> ContextCompat.getColor(this, R.color.green) // Normal weight
            bmi < 29.9 -> ContextCompat.getColor(this, R.color.yellow) // Overweight
            else -> ContextCompat.getColor(this, R.color.red) // Obesity
        }

        // Apply the determined color to the BMI value
        spannableString.setSpan(
            ForegroundColorSpan(bmiColor),
            10, // Start index of the BMI value
            bmiText.length, // End index (end of the string)
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the formatted text to the TextView
        bmiTextView.text = spannableString
    }

    private fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "underweight"
            bmi in 18.5..24.9 -> "normal_weight"
            bmi in 25.0..29.9 -> "overweight"
            else -> "obesity"
        }
    }

    private fun getBMICategoryForDisplay(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchDietPlan(disease: String) {
        val dietPlanRef = database.child("disease_diet_plans").child(disease).child("plan")

        dietPlanRef.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val dietPlans = SpannableStringBuilder() // Use SpannableStringBuilder

                    // Loop through each day and append the plan
                    for (daySnapshot in dataSnapshot.children) {
                        val plan = daySnapshot.getValue(DiseaseDietPlan::class.java)
                        plan?.let {
                            val dayText = "Day ${it.day}:\n"
                            val breakfastText = "Breakfast: ${it.breakfast}\n"
                            val lunchText = "Lunch: ${it.lunch}\n"
                            val dinnerText = "Dinner: ${it.dinner}\n"
                            val snackText = "Snack: ${it.snack}\n\n"

                            // Set color for "Day" text
                            val coloredDayText = SpannableString(dayText).apply {
                                setSpan(ForegroundColorSpan(ContextCompat.getColor(this@RecommendedDietPlanActivity, R.color.green)),
                                    0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }

                            // Append the text to the SpannableStringBuilder
                            dietPlans.append(coloredDayText)
                            dietPlans.append(breakfastText)
                            dietPlans.append(lunchText)
                            dietPlans.append(dinnerText)
                            dietPlans.append(snackText)
                        }
                    }

                    // Apply a fade-in animation when the diet plan text is displayed
                    dietPlanTextView.text = dietPlans
                    val fadeInAnimator = ObjectAnimator.ofFloat(dietPlanTextView, "alpha", 0f, 1f)
                    fadeInAnimator.duration = 800
                    fadeInAnimator.start()
                } else {
                    showToast("No diet plans found for $disease.")
                    dietPlanTextView.text = "No diet plan available."
                }
            }
            .addOnFailureListener { exception ->
                showToast("Error fetching data: ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}