package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle navigation item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_about -> {
                    Toast.makeText(this, "About Application", Toast.LENGTH_SHORT).show()
                    // Handle navigation to About section
                }
                R.id.nav_user_info -> {
                    Toast.makeText(this, "User Information", Toast.LENGTH_SHORT).show()
                    // Navigate to User Information
                    val intent = Intent(this, UserInfoActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_diet_plans -> {
                    Toast.makeText(this, "Diet Plans", Toast.LENGTH_SHORT).show()
                    // Navigate to Diet Plans
                    val intent = Intent(this, DietPlansActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_health_exercises -> {
                    Toast.makeText(this, "Health Exercises", Toast.LENGTH_SHORT).show()
                    // Navigate to Health Exercises
                    val intent = Intent(this, HealthExercisesActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers() // Close drawer after item is selected
            true
        }

        // Delayed check for user status
        Handler(Looper.getMainLooper()).postDelayed(
            {
                checkUserStatusAndNavigate()
            }, 2000
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkUserStatusAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate to the dashboard
            val intent = Intent(this, MainHomeScreen::class.java)
            Toast.makeText(this, "Move to Dashboard", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        } else {
            // User is not logged in, navigate to the login activity
            val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
            Toast.makeText(this, "login to your account", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        finish() // Close the current activity
    }
}
