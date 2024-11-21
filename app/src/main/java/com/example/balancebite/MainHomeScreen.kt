package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

@Suppress("DEPRECATION")
class MainHomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var profileImageView: ImageView
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_screen)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase Auth and database reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
        usernameTextView = findViewById(R.id.usernameTextView)

        profileImageView = findViewById(R.id.profileImageView)

        // Fetch and display the username & profile picture
        fetchAndDisplayUsername()
        fetchAndDisplayProfilePicture()

        // Set up section listeners
        setUpSectionListeners()

        // Initialize BottomNavigationView and set up item selection
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.selectedItemId = R.id.no_selection

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            handleBottomNavigation(item)
            true
        }


        val recommendeddietplanText = findViewById<TextView>(R.id.recommendedDietPlanText)
        recommendeddietplanText.setOnClickListener {
            // Start the DietPlanActivity to show BMI and diet plan
            val intent = Intent(this, RecommendedDietPlanActivity::class.java)
            startActivity(intent)
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
                val intent = Intent(this, PlanSelectionActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_chat_bot -> {
                val intent = Intent(this, ChatbotActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileShownActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this, AppSettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun fetchAndDisplayProfilePicture() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            navigateToLogin()
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
                            Glide.with(this@MainHomeScreen)
                                .load(profileUrl)
                                .placeholder(R.drawable.default_profile_picture) // Placeholder for loading
                                .error(R.drawable.default_profile_picture) // Fallback if loading fails
                                .into(profileImageView)
                        } ?: run {
                            Toast.makeText(this@MainHomeScreen, "Profile picture URL not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainHomeScreen, "Profile picture data does not exist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@MainHomeScreen, "Error fetching profile picture: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun fetchAndDisplayUsername() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            return
        }

        // Change the path to point to profile/name
        database.child(userId).child("profile").child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.getValue(String::class.java)
                    name?.let {
                        usernameTextView.text = "Welcome, $name"
                    } ?: run {
                        Toast.makeText(this@MainHomeScreen, "Name not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainHomeScreen, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainHomeScreen, "Error fetching name: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun navigateToLogin() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        if (backPressedOnce) {
            super.onBackPressed() // If back was pressed once, exit the app
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        // Reset the backPressedOnce flag after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.no_selection // Reset selection
    }

}