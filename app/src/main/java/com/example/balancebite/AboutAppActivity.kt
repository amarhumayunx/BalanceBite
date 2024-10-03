package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AboutAppActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val versionTextView: TextView = findViewById(R.id.TextView_for_app_version)

        // Get the version name and version code from the PackageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName

        // Set the version name and version code to the TextView
        versionTextView.text = "Version: $versionName "
    }
}