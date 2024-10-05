package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ContactUsActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Reference to the email TextView
        val emailTextView = findViewById<TextView>(R.id.email_address)

        // Set OnClickListener for the email TextView
        emailTextView.setOnClickListener {
            // Call the function to send the email
            sendEmail()
        }
    }

    // Function to send the email
    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail()
    {
        // Create an Intent to send an email
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("amarhumayun@outlook.com")) // recipient email
            putExtra(Intent.EXTRA_SUBJECT, "Any Problem?") // subject
            putExtra(Intent.EXTRA_TEXT, "Hello, I have some problem...") // body (optional)
        }

        // Verify that there is an email app to handle the intent
        if (emailIntent.resolveActivity(packageManager) != null)
        {
            startActivity(emailIntent) // Launch the email client
        }
        else
        {
            // Inform the user that no email app is available
            Toast.makeText(this, "No email app available", Toast.LENGTH_SHORT).show()
        }
    }
}