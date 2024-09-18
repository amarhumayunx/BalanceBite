package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.balancebite.databinding.ActivityMainHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainHomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
        usernameTextView = findViewById(R.id.usernameTextView)

        // Fetch and display the username
        fetchAndDisplayUsername()

        // Set up listeners for different sections
        setUpSectionListeners()

        // Initialize BottomNavigationView and set up item selection
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            handleBottomNavigation(item)
            true
        }
    }

    private fun setUpSectionListeners() {
        val tapOnFruits = findViewById<LinearLayout>(R.id.tap_on_fruits)
        tapOnFruits.setOnClickListener {
            val intent = Intent(this, FruitActivity::class.java)
            startActivity(intent)
        }

        val tapOnVegetables = findViewById<LinearLayout>(R.id.tap_on_vegetables)
        tapOnVegetables.setOnClickListener {
            val intent = Intent(this, VegetableActivity::class.java)
            startActivity(intent)
        }

        val tapOnSupplements = findViewById<LinearLayout>(R.id.tap_on_supplements)
        tapOnSupplements.setOnClickListener {
            val intent = Intent(this, SupplementActivity::class.java)
            startActivity(intent)
        }

        val readNowButton: Button = findViewById(R.id.readNowButton)
        readNowButton.setOnClickListener {
            val intent = Intent(this, FastFoodActivity::class.java)
            startActivity(intent)
        }

        val viewNowButton: Button = findViewById(R.id.viewNowButton)
        viewNowButton.setOnClickListener {
            val intent = Intent(this, ProgressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleBottomNavigation(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_diet_plan -> {
                val intent = Intent(this, DietPlansActivity::class.java)
                startActivity(intent)
            }
            /*R.id.nav_chat_bot -> {
                val intent = Intent(this, ChatBotActivity::class.java)
                startActivity(intent)
            }*/
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileShownActivity::class.java)
                startActivity(intent)
            }
            /*R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }*/
            else -> {
                Toast.makeText(this, "Unknown option selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAndDisplayUsername() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            return
        }

        database.child(userId).child("username").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val username = dataSnapshot.getValue(String::class.java)
                    username?.let {
                        usernameTextView.text = "Welcome, $username"
                    } ?: run {
                        Toast.makeText(this@MainHomeScreen, "Username not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainHomeScreen, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainHomeScreen, "Error fetching username: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Back to Login Page", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }
}
