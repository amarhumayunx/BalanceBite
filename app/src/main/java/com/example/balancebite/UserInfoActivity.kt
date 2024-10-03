package com.example.balancebite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

data class UserProfile(
    val name: String = "",
    val age: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val healthInfo: String = "",
    val profilePictureUrl: String = "" // Add a field for the profile picture URL
)

@Suppress("DEPRECATION")
class UserInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHealthInfo: EditText
    private lateinit var btnSubmit: Button
    private lateinit var profileImageView: CircleImageView
    private lateinit var btnChangeProfilePic: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val pickImageRequest = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Initialize views
        etName = findViewById(R.id.user_name_on_user_info)
        etAge = findViewById(R.id.user_age_on_user_info)
        etHeight = findViewById(R.id.user_height_on_user_info)
        etWeight = findViewById(R.id.user_weight_on_user_info)
        etHealthInfo = findViewById(R.id.user_health_info_on_user_info)
        btnSubmit = findViewById(R.id.btnSubmit)
        profileImageView = findViewById(R.id.profile_image)
        btnChangeProfilePic = findViewById(R.id.btnChangeProfilePic)

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            saveUserInfo()
        }

        // Set click listener for changing profile picture
        btnChangeProfilePic.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImageRequest)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == pickImageRequest) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri) // Display selected image in CircleImageView
        }
    }

    private fun saveUserInfo() {
        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim().toIntOrNull()
        val height = etHeight.text.toString().trim().toDoubleOrNull()
        val weight = etWeight.text.toString().trim().toDoubleOrNull()
        val healthInfo = etHealthInfo.text.toString().trim()

        // Check for incomplete information
        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }

        if (age == null || age <= 0) {
            etAge.error = "Please enter a valid age"
            return
        }

        if (height == null || height <= 0.0) {
            etHeight.error = "Please enter a valid height"
            return
        }

        if (weight == null || weight <= 0.0) {
            etWeight.error = "Please enter a valid weight"
            return
        }

        val userId = auth.currentUser?.uid

        if (userId != null) {
            if (imageUri != null) {
                uploadProfilePicture(userId, name, age, height, weight, healthInfo)
            } else {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadProfilePicture(userId: String, name: String, age: Int, height: Double, weight: Double, healthInfo: String) {
        val storageReference = FirebaseStorage.getInstance().getReference("profile_pictures/$userId.jpg")
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val profilePictureUrl = uri.toString()
                    saveUserProfileToDatabase(userId, name, age, height, weight, healthInfo, profilePictureUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserProfileToDatabase(userId: String, name: String, age: Int, height: Double, weight: Double, healthInfo: String, profilePictureUrl: String) {
        val userProfile = UserProfile(name, age, height, weight, healthInfo, profilePictureUrl)

        // Store user profile data in Firebase Realtime Database
        database.child(userId).child("profile").setValue(userProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                // Navigate to the main home screen after saving
                val intent = Intent(this, MainHomeScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}