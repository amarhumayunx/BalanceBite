package com.example.balancebite

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val versionTextView: TextView = findViewById(R.id.TextView_for_app_version)

        try {
            // Get the version name from the PackageManager
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName

            // Set the version name to the TextView
            versionTextView.text = getString(R.string.version_text, versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            // Handle the exception, maybe log or show a default message
            versionTextView.text = getString(R.string.version_not_found)
        }
    }
}