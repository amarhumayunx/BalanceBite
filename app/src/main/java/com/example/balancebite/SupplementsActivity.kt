package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SupplementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplements)

        val supplementList = listOf(
            Supplement("Protein Powder", "A supplement for muscle growth", 120, R.drawable.protein),
            Supplement("Vitamin C", "A supplement for immune support", 0, R.drawable.vitamin),
            Supplement("Omega-3", "A supplement for heart health", 9, R.drawable.omega),
            Supplement("Creatine", "A supplement for muscle strength and performance", 0, R.drawable.creatine),
            Supplement("Multivitamins", "A combination of essential vitamins and minerals", 0, R.drawable.multivitamin),
            Supplement("Calcium", "A supplement for bone health", 40, R.drawable.calcium),
            Supplement("Magnesium", "A supplement for muscle and nerve function", 0, R.drawable.magnesium),
            Supplement("Probiotics", "A supplement for gut health", 0, R.drawable.probiotic),
            Supplement("Vitamin D", "A supplement for bone health and immune support", 0, R.drawable.vitamin_d),
            Supplement("BCAA", "Branched-Chain Amino Acids for muscle recovery", 0, R.drawable.sport),
            Supplement("Zinc", "A supplement for immune function", 0, R.drawable.zinc),
            Supplement("Iron", "A supplement for blood health", 0, R.drawable.iron),
            Supplement("CoQ10", "A supplement for heart health", 0, R.drawable.q10),
            Supplement("Turmeric", "A supplement for anti-inflammatory support", 0, R.drawable.turmeric),
            Supplement("Green Tea Extract", "A supplement for antioxidant support", 0, R.drawable.green_tea),
            Supplement("Collagen", "A supplement for skin and joint health", 0, R.drawable.collagen),
            Supplement("Ginkgo Biloba", "A supplement for cognitive support", 0, R.drawable.ginkgo_biloba),
            Supplement("Ashwagandha", "A supplement for stress and anxiety relief", 0, R.drawable.ashwagandha)
            // Add more supplements as needed
        )


        val recyclerView = findViewById<RecyclerView>(R.id.supplementRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SupplementAdapter(supplementList)
    }
}
