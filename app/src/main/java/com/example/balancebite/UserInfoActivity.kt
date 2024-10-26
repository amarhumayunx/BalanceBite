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
    val email: String = "",
    val age: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val bmi: Double = 0.0,
    val gender: String = "",
    val healthInfo: String = "",
    val profilePictureUrl: String = ""
)

@Suppress("DEPRECATION")
class UserInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHealthInfo: EditText
    private lateinit var etGender: EditText
    private lateinit var btnSubmit: Button
    private lateinit var profileImageView: CircleImageView
    private lateinit var btnUploadProfilePic: Button
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
        etGender = findViewById(R.id.user_gender_on_user_info)
        etHeight = findViewById(R.id.user_height_on_user_info)
        etWeight = findViewById(R.id.user_weight_on_user_info)
        etHealthInfo = findViewById(R.id.user_health_info_on_user_info)
        btnSubmit = findViewById(R.id.btnSubmit)
        profileImageView = findViewById(R.id.profile_image)
        btnUploadProfilePic = findViewById(R.id.btnUploadProfilePic)

        // Submit button listener
        btnSubmit.setOnClickListener { saveUserInfo() }

        // Change profile picture listener
        btnUploadProfilePic.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImageRequest)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == pickImageRequest) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri) // Display selected image
        }
    }

    private fun saveUserInfo() {
        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim().toIntOrNull()
        val height = etHeight.text.toString().trim().toDoubleOrNull()
        val weight = etWeight.text.toString().trim().toDoubleOrNull()
        val gender = etGender.text.toString().trim()
        val healthInfo = etHealthInfo.text.toString().trim()

        // Validate inputs
        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }
        if (age == null || age <= 0) {
            etAge.error = "Enter a valid age"
            return
        }
        if (height == null || height <= 0) {
            etHeight.error = "Enter a valid height"
            return
        }
        if (weight == null || weight <= 0) {
            etWeight.error = "Enter a valid weight"
            return
        }
        if (gender.isEmpty()) {
            etGender.error = "Enter your gender"
            return
        }

        val userId = auth.currentUser?.uid
        val email = auth.currentUser?.email

        if (userId != null && email != null) {
            if (imageUri != null) {
                uploadProfilePicture(userId, email, name, age, height, weight, gender, healthInfo)
            } else {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadProfilePicture(
        userId: String,
        email: String,
        name: String,
        age: Int,
        height: Double,
        weight: Double,
        gender: String,
        healthInfo: String
    ) {
        val storageReference = FirebaseStorage.getInstance().getReference("profile_pictures/$userId.jpg")
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val profilePictureUrl = uri.toString()
                    saveUserProfileToDatabase(email, userId, name, age, height, weight, gender, healthInfo, profilePictureUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload picture: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserProfileToDatabase(
        email: String,
        userId: String,
        name: String,
        age: Int,
        height: Double,
        weight: Double,
        gender: String,
        healthInfo: String,
        profilePictureUrl: String
    ) {
        val userProfile = mapOf(
            "email" to email,  // Include email in the profile
            "name" to name,
            "age" to age,
            "height" to height,
            "weight" to weight,
            "gender" to gender,
            "healthInfo" to healthInfo,
            "profilePictureUrl" to profilePictureUrl
        )

        // Use updateChildren to avoid overwriting existing data
        database.child(userId).child("profile").updateChildren(userProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainHomeScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
