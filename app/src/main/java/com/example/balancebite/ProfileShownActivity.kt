package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileShownActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var nameTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var healthInfoTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var bmiTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_shown)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        // Initialize TextViews
        nameTextView = findViewById(R.id.usernameActualTextView)
        heightTextView = findViewById(R.id.heightTextView)
        weightTextView = findViewById(R.id.weightTextView)
        genderTextView = findViewById(R.id.genderTextView)
        healthInfoTextView = findViewById(R.id.healthInfoTextView)
        ageTextView = findViewById(R.id.ageTextView)
        bmiTextView = findViewById(R.id.bodymassindexTextView)

        // Fetch and display user information
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Access the nested 'profile' node for the user
        database.child(userId).child("profile").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: "Name not available"
                    val age = dataSnapshot.child("age").getValue(Int::class.java)?.toString() ?: "Age not available"
                    val gender = dataSnapshot.child("gender").getValue(String::class.java) ?: "Gender not available"
                    val heightCm = dataSnapshot.child("height").getValue(Int::class.java)
                    val weightKg = dataSnapshot.child("weight").getValue(Int::class.java)
                    val healthInfo = dataSnapshot.child("healthInfo").getValue(String::class.java) ?: "Health Info not available"

                    // Set the data to TextViews
                    nameTextView.text = "Name: $name"
                    ageTextView.text = "Age: $age"
                    genderTextView.text = "Gender: $gender"
                    heightTextView.text = "Height: ${heightCm ?: "N/A"} cm"
                    weightTextView.text = "Weight: ${weightKg ?: "N/A"} kg"
                    healthInfoTextView.text = "Health Info: $healthInfo"

                    // Calculate and display BMI if height and weight are available
                    if (heightCm != null && weightKg != null) {
                        val heightMeters = heightCm / 100.0  // Convert cm to meters
                        val bmi = calculateBMI(weightKg, heightMeters)
                        bmiTextView.text = "BMI: %.2f".format(bmi)

                        // Save BMI to Firebase Database
                        saveBMIToDatabase(userId, bmi)
                    } else {
                        bmiTextView.text = "BMI: Not available"
                    }
                } else {
                    Toast.makeText(this@ProfileShownActivity, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ProfileShownActivity, "Error fetching data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to save BMI to Firebase
    private fun saveBMIToDatabase(userId: String, bmi: Double) {
        database.child(userId).child("profile").child("bmi").setValue(bmi)
            .addOnSuccessListener {
                print("BMI saved successfully")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving BMI: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // BMI Calculation Function
    private fun calculateBMI(weight: Int, height: Double): Double {
        return weight / (height * height)
    }
}