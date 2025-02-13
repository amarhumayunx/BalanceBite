package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TrackProgressInfoShowActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var progressRecyclerView: RecyclerView
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var progressList: MutableList<ProgressEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_progress_info_show)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        progressRecyclerView = findViewById(R.id.progressRecyclerView)
        progressRecyclerView.layoutManager = LinearLayoutManager(this)
        progressList = mutableListOf()
        progressAdapter = ProgressAdapter(progressList)
        progressRecyclerView.adapter = progressAdapter

        // Initialize Firebase Database reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database = FirebaseDatabase.getInstance().reference.child("Users").child(userId).child("Progress")
            fetchProgressData()
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchProgressData() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressList.clear()

                // Loop through the "Progress" node
                for (snapshot in dataSnapshot.children) {
                    val calories = snapshot.child("Calories").getValue(Int::class.java) ?: 0
                    val day = snapshot.child("Day").getValue(String::class.java) ?: ""
                    val exerciseTime = snapshot.child("Exercise Time").getValue(Int::class.java) ?: 0
                    val water = snapshot.child("Water").getValue(Int::class.java) ?: 0
                    val weight = snapshot.child("Weight").getValue(Int::class.java) ?: 0

                    // Create a ProgressEntry object from the fetched data
                    val progressEntry = ProgressEntry(calories, day, exerciseTime, water, weight)

                    // Add to list
                    progressList.add(progressEntry)
                }

                // Notify the adapter that data has changed
                progressAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@TrackProgressInfoShowActivity, "Failed to load progress data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}