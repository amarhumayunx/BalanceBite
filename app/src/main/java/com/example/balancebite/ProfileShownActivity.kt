package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
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
    private lateinit var profileImageView: ImageView
    private lateinit var cardView: CardView
    private lateinit var waveOne: ImageView
    private lateinit var waveTwo: ImageView

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
        profileImageView = findViewById(R.id.profileImageView)
        cardView = findViewById(R.id.cardview_profile_info)
        waveOne = findViewById(R.id.wave_one)
        waveTwo = findViewById(R.id.wave_two)

        // Fetch and display user information
        fetchUserInfo()
        fetchAndDisplayProfilePicture()

        // Apply Animations
        applyAnimations()
    }

    private fun fetchAndDisplayProfilePicture() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Change the path to point to profile/profilePictureUrl
        database.child(userId).child("profile").child("profilePictureUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val profileUrl = dataSnapshot.getValue(String::class.java)
                        profileUrl?.let {
                            // Use Glide to load the image into the ImageView
                            Glide.with(this@ProfileShownActivity)
                                .load(profileUrl)
                                .placeholder(R.drawable.default_profile_picture) // Placeholder for loading
                                .error(R.drawable.default_profile_picture) // Fallback if loading fails
                                .into(profileImageView)
                        } ?: run {
                            Toast.makeText(this@ProfileShownActivity, "Profile picture URL not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfileShownActivity, "Profile picture data does not exist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ProfileShownActivity, "Error fetching profile picture: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
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

    // Function to apply animations
    private fun applyAnimations() {
        // Apply Slide-In Animation to CardView
        cardView.translationY = 500f // Start from off-screen (bottom)
        cardView.animate()
            .translationY(0f) // Move to original position
            .setDuration(1000)
            .setStartDelay(200)
            .start()

        // Apply Fade-In Animation to Wave One
        waveOne.alpha = 0f
        waveOne.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(500)
            .start()

        // Apply Fade-In Animation to Wave Two
        waveTwo.alpha = 0f
        waveTwo.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(700)
            .start()
    }
}