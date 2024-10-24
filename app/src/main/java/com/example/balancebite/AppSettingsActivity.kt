package com.example.balancebite

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)
        val buttonAbout = findViewById<Button>(R.id.buttonAbout)
        val buttonDeleteProfile = findViewById<Button>(R.id.buttonDeleteProfile)
        val buttonShowProgress = findViewById<Button>(R.id.buttonShowProgress)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        profileImageView = findViewById(R.id.profile_image)
        val buttonFeedback = findViewById<Button>(R.id.buttonFeedback)
        val buttonContactUs = findViewById<Button>(R.id.contactusbutton)

        // Load profile picture
        loadProfilePicture()

        // Handle "Change Password" button click
        buttonChangePassword.setOnClickListener {
            val newPassword = editTextPassword.text.toString()
            if (newPassword.isNotEmpty()) {
                changePassword(newPassword)
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "About" button click
        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }

        // Handle "Delete Profile" button click
        buttonDeleteProfile.setOnClickListener {
            deleteProfile()
        }

        // Handle "Show Progress Information" button click
        buttonShowProgress.setOnClickListener {
            val intent = Intent(this, TrackProgressInfoShowActivity::class.java)
            startActivity(intent)
        }

        // Handle "Logout" button click
        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to Login Activity
            val intent = Intent(this, LoginPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Handle "Feedback" button click
        buttonFeedback.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // Handle "Contact Us" button click
        buttonContactUs.setOnClickListener{
            val intent = Intent(this,ContactUsActivity::class.java)
            startActivity(intent)
        }

    }


    // Load profile picture from Firebase Realtime Database
    private fun loadProfilePicture() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(it.uid).child("profile").child("profilePictureUrl")
            databaseRef.get().addOnSuccessListener { snapshot ->
                val profilePictureUrl = snapshot.getValue(String::class.java)
                Log.d("ProfilePicture", "Retrieved URL: $profilePictureUrl") // Log the URL
                if (!profilePictureUrl.isNullOrEmpty()) {
                    Picasso.get()
                        .load(profilePictureUrl)
                        .transform(CircularImageTransformation()) // Apply circular transformation
                        .into(profileImageView)
                } else {
                    Log.e("ProfilePicture", "No profile picture URL found")
                }
            }.addOnFailureListener { e ->
                Log.e("ProfilePicture", "Failed to load profile picture: ${e.message}")
            }
        } ?: run {
            Log.e("ProfilePicture", "No user is currently logged in")
        }
    }

    private fun changePassword(newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun deleteProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(it.uid)

            // First, remove user data from the Firebase Realtime Database
            databaseRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If database data removal is successful, delete the Firebase Authentication account
                    it.delete().addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            // Account deletion success
                            Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()

                            // Redirect to sign-up activity after account deletion
                            val intent = Intent(this, ActivitySignUP::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
                            startActivity(intent)
                            finish()
                        } else {
                            // Handle failure to delete the Firebase Authentication account
                            Log.e("DeleteProfile", "Failed to delete account: ${deleteTask.exception?.message}")
                            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle failure to delete the data from the Firebase Realtime Database
                    Log.e("DeleteProfile", "Failed to delete user data: ${task.exception?.message}")
                    Toast.makeText(this, "Failed to delete user data", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            // If no user is logged in, notify and return
            Toast.makeText(this, "No user is currently logged in", Toast.LENGTH_SHORT).show()
        }
    }

}

// CircularImageTransformation.kt
class CircularImageTransformation : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = source.width.coerceAtMost(source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        paint.shader = shader

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}
