package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class ProgressActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var totalCaloriesText: TextView
    private lateinit var stepsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_acitivity)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        // Initialize TextViews
        totalCaloriesText = findViewById(R.id.totalCaloriesText)
        stepsText = findViewById(R.id.stepsText)

        // Fetch and display progress
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        fetchProgress(today)
    }

    private fun fetchProgress(date: String) {
        val userId = auth.currentUser?.uid ?: return

        database.child(userId).child("progress").child(date)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val totalCalories = dataSnapshot.child("totalCalories").getValue(Int::class.java) ?: 0
                    val steps = dataSnapshot.child("steps").getValue(Int::class.java) ?: 0

                    totalCaloriesText.text = "Total Calories: $totalCalories"
                    stepsText.text = "Steps: $steps"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ProgressActivity, "Failed to fetch progress", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
