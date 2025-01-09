package com.example.balancebite

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION", "PrivatePropertyName")
class MainHomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usernameTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var profileImageView: ImageView
    private lateinit var app_name_on_cardview: TextView
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
        app_name_on_cardview = findViewById(R.id.app_name_on_main_activity)

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

        val usernameTap = findViewById<CardView>(R.id.usernameCardView)
        usernameTap.setOnClickListener{
            // Start The Profile Activity to Show User Profile
            val intent = Intent(this,ProfileShownActivity::class.java)
            startActivity(intent)
        }

        val recommendeddietplanText = findViewById<TextView>(R.id.recommendedDietPlanText)
        recommendeddietplanText.setOnClickListener {
            // Start the DietPlanActivity to show BMI and diet plan
            val intent = Intent(this, RecommendedDietPlanActivity::class.java)
            startActivity(intent)
        }

        // Add animations
        applyEntryAnimations()
        animateSectionCards()
        addPulsingEffectToRecommendedDietPlan()
        addTouchAnimationToSections()
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

        val tapOnRecipes = findViewById<LinearLayout>(R.id.tap_on_recipes)
        tapOnRecipes.setOnClickListener{
            val intent = Intent(this, RecipesActivity::class.java)
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
            R.id.nav_exercise_plan -> {
                val intent = Intent(this, ExercisePlanActivity::class.java)
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

        database.child(userId).child("profile").child("profilePictureUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val profileUrl = dataSnapshot.getValue(String::class.java)
                        profileUrl?.let {
                            Glide.with(this@MainHomeScreen)
                                .load(profileUrl)
                                .placeholder(R.drawable.default_profile_picture)
                                .error(R.drawable.default_profile_picture)
                                .into(profileImageView)
                        }
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

        database.child(userId).child("profile").child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.getValue(String::class.java)
                    name?.let {
                        usernameTextView.text = "Welcome, $name"
                    }
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
            super.onBackPressed()
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.selectedItemId = R.id.no_selection
    }

    private fun applyEntryAnimations() {
        // Username and app name slide in with a fade
        val fadeSlideIn = listOf(usernameTextView, app_name_on_cardview)
        fadeSlideIn.forEach { view ->
            ObjectAnimator.ofFloat(view, "translationY", -200f, 0f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
                duration = 1000
                start()
            }
        }

        // Profile picture with bounce and rotation
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.2f, 1f)
        val rotation = PropertyValuesHolder.ofFloat("rotation", 0f, 360f)
        ObjectAnimator.ofPropertyValuesHolder(profileImageView, scaleX, scaleY, rotation).apply {
            duration = 1200
            start()
        }
    }

    private fun animateSectionCards() {
        val sections = listOf(
            findViewById<LinearLayout>(R.id.tap_on_fruits),
            findViewById<LinearLayout>(R.id.tap_on_vegetables),
            findViewById<LinearLayout>(R.id.tap_on_supplements)
        )
        var delay = 0L

        for (section in sections) {
            section.scaleX = 0.8f
            section.scaleY = 0.8f
            section.alpha = 0f

            ViewCompat.animate(section)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setStartDelay(delay)
                .setDuration(800)
                .start()
            delay += 300
        }
    }

    private fun addPulsingEffectToRecommendedDietPlan() {
        val recommendedDietPlanText = findViewById<TextView>(R.id.recommendedDietPlanText)
        val pulseX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.15f, 1f)
        val pulseY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.15f, 1f)
        val colorChange = ObjectAnimator.ofArgb(
            recommendedDietPlanText,
            "textColor",
            ContextCompat.getColor(this, R.color.white),
            ContextCompat.getColor(this, R.color.light_white)
        )
        colorChange.duration = 1000
        colorChange.repeatCount = ObjectAnimator.INFINITE
        colorChange.repeatMode = ObjectAnimator.REVERSE

        ObjectAnimator.ofPropertyValuesHolder(recommendedDietPlanText, pulseX, pulseY).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        colorChange.start()
    }

    private fun addTouchAnimationToSections() {
        val sections = listOf(
            findViewById<LinearLayout>(R.id.tap_on_fruits),
            findViewById<LinearLayout>(R.id.tap_on_vegetables),
            findViewById<LinearLayout>(R.id.tap_on_supplements)
        )

        for (section in sections) {
            section.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Elevate and darken the background
                        view.animate().scaleX(0.95f).scaleY(0.95f).translationZ(10f).setDuration(100).start()
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Reset scale and elevation
                        view.animate().scaleX(1f).scaleY(1f).translationZ(0f).setDuration(100).start()
                        view.performClick()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}