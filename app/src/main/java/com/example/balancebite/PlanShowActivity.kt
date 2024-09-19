package com.example.balancebite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PlanSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_show)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val btnDietPlans: Button = findViewById(R.id.btnDietPlans)
        val btnExercises: Button = findViewById(R.id.btnExercises)

        btnDietPlans.setOnClickListener {
            val intent = Intent(this, DietPlanActivity::class.java)
            startActivity(intent)
        }

        btnExercises.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
    }
}