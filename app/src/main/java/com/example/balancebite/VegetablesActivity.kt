package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VegetableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vegetables)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        val vegetableList = listOf(
            Vegetable("Carrot", "A vibrant orange root vegetable, rich in beta-carotene, vitamin A, and dietary fiber, promoting good vision, skin health, and immune support.", 41, R.drawable.carrots),
            Vegetable("Broccoli", "A nutrient-dense cruciferous vegetable containing vitamins C, K, and folate, alongside phytonutrients with anti-inflammatory and cancer-preventive benefits.", 55, R.drawable.broccoli),
            Vegetable("Spinach", "A versatile dark leafy green packed with iron, calcium, and powerful antioxidants, supporting muscle function and bone strength.", 23, R.drawable.spinach),
            Vegetable("Tomato", "A juicy and tangy fruit-vegetable hybrid rich in lycopene, vitamin C, and potassium, known for promoting heart health and skin elasticity.", 18, R.drawable.tomato),
            Vegetable("Potato", "A starchy tuber providing complex carbohydrates, potassium, and vitamin B6, ideal for sustained energy and muscle function.", 77, R.drawable.potato),
            Vegetable("Cucumber", "A hydrating vegetable with high water content, silica, and vitamin K, beneficial for skin health and hydration.", 16, R.drawable.cucumber),
            Vegetable("Bell Pepper", "A colorful vegetable loaded with vitamin C, carotenoids, and fiber, enhancing immune function and eye health.", 20, R.drawable.bell_pepper),
            Vegetable("Onion", "A flavorful vegetable with sulfur compounds and antioxidants, promoting cardiovascular health and supporting the immune system.", 40, R.drawable.onions),
            Vegetable("Garlic", "A pungent bulb with allicin, known for its antimicrobial, heart-protective, and immune-boosting properties.", 149, R.drawable.garlics),
            Vegetable("Lettuce", "A crisp leafy vegetable low in calories, rich in vitamin K, and a refreshing base for salads.", 15, R.drawable.lettuce),
            Vegetable("Cabbage", "A cruciferous vegetable rich in vitamin C, fiber, and glucosinolates, promoting digestive health and reducing inflammation.", 25, R.drawable.cabbage),
            Vegetable("Zucchini", "A light summer squash packed with vitamin C, manganese, and antioxidants, supporting hydration and healthy digestion.", 17, R.drawable.zucchini),
            Vegetable("Eggplant", "A glossy purple vegetable high in fiber and anthocyanins, promoting heart health and reducing oxidative stress.", 25, R.drawable.eggplant),
            Vegetable("Pumpkin", "A nutrient-rich orange squash high in beta-carotene, vitamin A, and antioxidants, supporting vision and immunity.", 26, R.drawable.pumpkin),
            Vegetable("Celery", "A crunchy, low-calorie vegetable containing antioxidants, vitamins K and C, and essential electrolytes for hydration.", 14, R.drawable.celery),
            Vegetable("Cauliflower", "A fiber-rich cruciferous vegetable high in choline and vitamin C, promoting brain and digestive health.", 25, R.drawable.cauliflower),
            Vegetable("Brussels Sprouts", "A miniature cabbage-like vegetable loaded with vitamin K, folate, and antioxidants, supporting bone health and reducing inflammation.", 43, R.drawable.brussel_sprouts),
            Vegetable("Peas", "A sweet and starchy legume rich in plant-based protein, fiber, and vitamin C, aiding digestion and muscle repair.", 81, R.drawable.pea),
            Vegetable("Sweet Corn", "A naturally sweet starchy vegetable providing dietary fiber, B vitamins, and energy for active lifestyles.", 86, R.drawable.fresh),
            Vegetable("Mushroom", "A nutrient-rich fungus high in selenium, vitamin D, and B vitamins, supporting immune function and reducing oxidative stress.", 22, R.drawable.mushrooms),
            Vegetable("Kale", "A superfood packed with vitamin A, calcium, and antioxidants, promoting detoxification, vision health, and strong bones.", 49, R.drawable.mustard_greens),
            Vegetable("Asparagus", "A spring vegetable loaded with folate, vitamin K, and fiber, supporting healthy digestion and pregnancy.", 20, R.drawable.asparagus),
            Vegetable("Beetroot", "A vibrant red root rich in nitrates, vitamin C, and fiber, known for boosting stamina and supporting heart health.", 43, R.drawable.beetroot),
            Vegetable("Radish", "A crunchy and spicy root vegetable high in vitamin C and water content, aiding detoxification and hydration.", 16, R.drawable.radish),
            Vegetable("Turnip", "A mildly sweet root vegetable with high levels of vitamin C and potassium, promoting skin health and hydration.", 28, R.drawable.turnip),
            Vegetable("Leek", "A mild-flavored vegetable related to onions, rich in vitamin K and folate, supporting heart health and bone strength.", 61, R.drawable.leek),
            Vegetable("Ginger", "A spicy root known for its high gingerol content, offering anti-inflammatory and nausea-relieving benefits.", 80, R.drawable.ginger),
            Vegetable("Chili Pepper", "A spicy vegetable containing capsaicin, which boosts metabolism, reduces appetite, and enhances circulation.", 40, R.drawable.chili),
            Vegetable("Parsnip", "A sweet root vegetable rich in soluble fiber, vitamin C, and potassium, supporting heart health and digestive function.", 75, R.drawable.parsnip)
        )


        val recyclerView = findViewById<RecyclerView>(R.id.vegetableRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VegetableAdapter(vegetableList)
    }
}
