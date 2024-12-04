package com.example.balancebite

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FastFoodActivity : AppCompatActivity() {

    private lateinit var prosTextView: TextView
    private lateinit var consTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_food)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize the TextViews
        prosTextView = findViewById(R.id.prosTextView)
        consTextView = findViewById(R.id.consTextView)

        // Load dynamic content for pros and cons
        loadFastFoodContent()
    }

    private fun loadFastFoodContent() {
        // Expanded Pros of Fast Food with Icons
        val pros = listOf(
            "✔️ Fast and convenient meals, especially during busy schedules.",
            "✔️ Affordable and accessible, offering quick snacks and meals for those on a budget.",
            "✔️ Widely available across the globe, including airports, malls, and rest stops.",
            "✔️ Consistency in taste and quality across different outlets.",
            "✔️ Variety of options, including burgers, pizzas, wraps, and salads.",
            "✔️ Special meal deals and offers make it attractive to families and groups.",
            "✔️ Some fast food chains now offer healthier alternatives, like salads and smoothies.",
            "✔️ Great for social gatherings or celebrations when cooking isn't feasible.",
            "✔️ Drive-thru options save time and are useful during travel or when on the go.",
            "✔️ Food delivery services allow fast food to be enjoyed at home conveniently."
        )

        // Expanded Cons of Fast Food with Icons
        val cons = listOf(
            "❌ High in unhealthy fats, sugar, and calories, contributing to weight gain.",
            "❌ Low nutritional value compared to homemade or fresh meals.",
            "❌ Frequent consumption may lead to chronic health issues like obesity, diabetes, and heart disease.",
            "❌ Often contains high levels of sodium, which can cause hypertension over time.",
            "❌ Addictive nature of certain foods, such as sugary beverages and fried items.",
            "❌ May lead to poor eating habits, especially in children, if not moderated.",
            "❌ Environmental concerns related to excessive packaging and waste.",
            "❌ Negative impact on local cuisines and culinary traditions in some regions.",
            "❌ Ethical concerns regarding fast food companies' treatment of employees and sourcing practices.",
            "❌ Increased reliance on fast food reduces the practice of home cooking, leading to loss of culinary skills."
        )

        // Display the pros dynamically with icons
        prosTextView.text = pros.joinToString(separator = "\n\n")

        // Display the cons dynamically with icons
        consTextView.text = cons.joinToString(separator = "\n\n")
    }


}
