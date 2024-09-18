package com.example.balancebite
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileShownActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var nameTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var healthInfoTextView: TextView
    private lateinit var ageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_shown)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        // Initialize TextViews
        nameTextView = findViewById(R.id.usernameTextView)
        heightTextView = findViewById(R.id.heightTextView)
        weightTextView = findViewById(R.id.weightTextView)
        healthInfoTextView = findViewById(R.id.healthInfoTextView)
        ageTextView = findViewById(R.id.ageTextView) // Make sure you have this TextView in your layout

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
                    val height = dataSnapshot.child("height").getValue(Int::class.java)?.toString() ?: "Height not available"
                    val weight = dataSnapshot.child("weight").getValue(Int::class.java)?.toString() ?: "Weight not available"
                    val healthInfo = dataSnapshot.child("healthInfo").getValue(String::class.java) ?: "Health Info not available"

                    // Set the data to TextViews
                    nameTextView.text = "Name: $name"
                    ageTextView.text = "Age: $age"
                    heightTextView.text = "Height: $height"
                    weightTextView.text = "Weight: $weight"
                    healthInfoTextView.text = "Health Info: $healthInfo"
                } else {
                    Toast.makeText(this@ProfileShownActivity, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ProfileShownActivity, "Error fetching data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
