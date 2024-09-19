package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AppSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)

        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)
        val buttonAbout = findViewById<Button>(R.id.buttonAbout)
        val buttonDeleteProfile = findViewById<Button>(R.id.buttonDeleteProfile)

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
            showAboutInfo()
        }

        // Handle "Delete Profile" button click
        buttonDeleteProfile.setOnClickListener {
            deleteProfile()
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

    private fun showAboutInfo() {
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        val developerName = "Humayun Amar" // Or fetch from a resource file

        AlertDialog.Builder(this)
            .setTitle("About This App")
            .setMessage("Developer: $developerName\nVersion: $versionName")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Reference to user data in Firebase Realtime Database
            val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(it.uid)

            // Remove user data from the database
            databaseRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // After user data is removed, delete the user account
                    it.delete().addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            // Notify the user and redirect to login screen
                            Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivityAfterSplashScreen::class.java)
                            startActivity(intent)
                            finish() // Close the current activity
                        } else {
                            // Notify the user of the failure in deleting the account
                            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Notify the user of the failure in deleting the user data
                    Toast.makeText(this, "Failed to delete user data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}