package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SupplementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplements)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        val supplementList = listOf(
            Supplement("Protein Powder", "A premium protein source to aid in muscle growth and repair", 120, R.drawable.protein),
            Supplement("Vitamin C", "Essential for collagen production, immune defense, and antioxidant protection", 0, R.drawable.vitamin),
            Supplement("Omega-3", "Provides EPA and DHA fatty acids to promote cardiovascular and brain health", 9, R.drawable.omega),
            Supplement("Creatine Monohydrate", "Enhances strength, endurance, and muscle performance", 0, R.drawable.creatine),
            Supplement("Multivitamins", "A comprehensive blend of vitamins and minerals for overall health", 0, R.drawable.multivitamin),
            Supplement("Calcium", "Supports bone density and muscle function, particularly for aging individuals", 40, R.drawable.calcium),
            Supplement("Magnesium Glycinate", "Promotes muscle relaxation and energy metabolism", 0, R.drawable.magnesium),
            Supplement("Probiotics", "Maintains healthy gut flora for improved digestion and immunity", 0, R.drawable.probiotic),
            Supplement("Vitamin D3", "Boosts calcium absorption for bone health and enhances immunity", 0, R.drawable.vitamin_d),
            Supplement("BCAA", "Leucine, isoleucine, and valine for faster muscle recovery and reduced soreness", 0, R.drawable.sport),
            Supplement("Zinc", "Essential for immune health, wound healing, and DNA synthesis", 0, R.drawable.zinc),
            Supplement("Iron Bisglycinate", "Highly bioavailable form of iron for anemia and energy support", 0, R.drawable.iron),
            Supplement("CoQ10 (Ubiquinol)", "Improves mitochondrial energy production and supports cardiovascular health", 0, R.drawable.q10),
            Supplement("Turmeric Curcumin", "Potent anti-inflammatory with added black pepper for absorption", 0, R.drawable.turmeric),
            Supplement("Green Tea Extract", "Rich in polyphenols for metabolism, heart health, and antioxidant protection", 0, R.drawable.green_tea),
            Supplement("Hydrolyzed Collagen", "Promotes skin hydration, elasticity, and joint comfort", 0, R.drawable.collagen),
            Supplement("Ginkgo Biloba", "Improves cognitive function, memory retention, and circulation", 0, R.drawable.ginkgo_biloba),
            Supplement("Ashwagandha Root Extract", "Adaptogen that reduces stress and enhances overall vitality", 0, R.drawable.ashwagandha),
            Supplement("Fish Oil (Triple Strength)", "Concentrated omega-3 for heart, brain, and joint support", 9, R.drawable.fish_oil),
            Supplement("Whey Isolate", "Fast-absorbing protein with low fat and carbohydrates for post-workout recovery", 110, R.drawable.whey_protein),
            Supplement("L-Glutamine", "Supports immune function and accelerates muscle recovery post-exercise", 0, R.drawable.supplement_tab),
            Supplement("Melatonin", "A natural hormone to regulate sleep-wake cycles and improve sleep quality", 0, R.drawable.melatonin),
            Supplement("Caffeine Anhydrous", "A fast-acting stimulant to enhance focus and energy", 0, R.drawable.coffee_bag),
            Supplement("Elderberry Syrup", "Provides antioxidant support to combat seasonal illnesses", 20, R.drawable.elderberry),
            Supplement("Biotin (Vitamin B7)", "Essential for healthy hair, skin, and nails", 0, R.drawable.supplement_tab),
            Supplement("Kelp Powder", "Rich in iodine to support thyroid function and metabolism", 0, R.drawable.supplement_tab),
            Supplement("Resveratrol", "A polyphenol found in red grapes to support heart health and longevity", 0, R.drawable.supplement_tab),
            Supplement("Spirulina Powder", "A nutrient-rich superfood with protein, vitamins, and antioxidants", 25, R.drawable.spirulina),
            Supplement("Chlorella", "A green algae supplement high in chlorophyll for detoxification and energy", 20, R.drawable.chlorella_vulgaris),
            Supplement("L-Carnitine Tartrate", "Facilitates fat metabolism and enhances endurance", 0, R.drawable.supplement_tab),
            Supplement("Hyaluronic Acid", "A key molecule for skin hydration and joint lubrication", 0, R.drawable.hyaluronic_acid),
            Supplement("Saw Palmetto", "Supports prostate health and hormonal balance in men", 0, R.drawable.supplement_tab),
            Supplement("Milk Thistle Extract", "Contains silymarin for liver detoxification and antioxidant support", 0, R.drawable.milk_thistle),
            Supplement("Garlic Extract", "Promotes cardiovascular health and boosts immune defenses", 0, R.drawable.garlic),
            Supplement("Ginseng (Panax)", "Enhances energy, mental clarity, and physical endurance", 0, R.drawable.ginseng),
            Supplement("Rhodiola Rosea", "Adaptogen that reduces fatigue and improves exercise performance", 0, R.drawable.supplement_tab),
            Supplement("Acai Berry", "A super fruit loaded with antioxidants for anti-aging and heart health", 30, R.drawable.acai),
            Supplement("Chromium Picolinate", "Regulates blood sugar levels and improves insulin sensitivity", 0, R.drawable.chromium),
            Supplement("Folic Acid (Vitamin B9)", "Critical for DNA synthesis and prenatal development", 0, R.drawable.folic_acid),
            Supplement("Niacin (Vitamin B3)", "Supports cholesterol balance and healthy skin", 0, R.drawable.supplement_tab),
            Supplement("Potassium Citrate", "Maintains fluid balance and prevents muscle cramps", 0, R.drawable.potassium),
            Supplement("Selenium", "Protects cells from oxidative stress and supports thyroid health", 0, R.drawable.selenium),
            Supplement("L-Theanine", "Promotes relaxation and reduces stress without causing drowsiness", 0, R.drawable.supplement_tab),
            Supplement("Alpha LiPo Acid", "Universal antioxidant for nerve health and glucose metabolism", 0, R.drawable.supplement_tab),
            Supplement("Peppermint Oil", "Eases digestive discomfort and promotes a sense of calm", 0, R.drawable.peppermint),
            Supplement("Antihistamine", "A powerful antioxidant that supports skin, eye, and joint health", 0, R.drawable.supplement_tab),
            Supplement("Bone Broth Protein", "Rich in collagen and amino acids for gut, skin, and joint health", 40, R.drawable.supplement_tab)
        )


        val recyclerView = findViewById<RecyclerView>(R.id.supplementRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SupplementAdapter(supplementList)
    }
}
