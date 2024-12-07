package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FruitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruits)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        val fruitList = listOf(
            Fruit("Apple", "A crisp and sweet fruit rich in dietary fiber, vitamin C, and antioxidants, promoting heart health and weight management.", 52, R.drawable.apple),
            Fruit("Banana", "A nutrient-dense tropical fruit high in potassium, providing quick energy and supporting muscle function.", 89, R.drawable.banana),
            Fruit("Orange", "A juicy citrus fruit loaded with vitamin C, aiding immunity and skin health with its refreshing tangy flavor.", 47, R.drawable.orange),
            Fruit("Strawberry", "A bright red berry packed with antioxidants, folate, and vitamin C, promoting skin health and heart function.", 32, R.drawable.strawberry),
            Fruit("Grapes", "Sweet and juicy fruits available in red, green, or black, rich in polyphenols and good for heart health.", 69, R.drawable.grape),
            Fruit("Watermelon", "A hydrating fruit with high water content, lycopene, and vitamins A and C for skin and heart health.", 30, R.drawable.watermelon),
            Fruit("Pineapple", "A tropical fruit with sweet and tangy flavor, rich in bromelain, which supports digestion and reduces inflammation.", 50, R.drawable.pineapple),
            Fruit("Mango", "A luscious tropical fruit known for its vibrant sweetness and high levels of vitamin A, C, and folate.", 60, R.drawable.mango),
            Fruit("Blueberry", "Small berries known for their high antioxidant content and ability to support brain and heart health.", 57, R.drawable.blueberry),
            Fruit("Peach", "A soft and juicy fruit with a sweet flavor, providing vitamin C and beta-carotene for skin and eye health.", 39, R.drawable.peach),
            Fruit("Kiwi", "A small, tart fruit with green flesh, rich in vitamin C, potassium, and fiber for digestive health.", 61, R.drawable.kiwi),
            Fruit("Papaya", "A tropical fruit with orange flesh, known for digestive enzymes like papain and a rich source of vitamins A and C.", 43, R.drawable.papaya),
            Fruit("Lychee", "A small, juicy tropical fruit with a floral aroma and high vitamin C content, supporting immunity.", 66, R.drawable.lychee),
            Fruit("Dragon Fruit", "An exotic fruit with vibrant skin and mildly sweet flesh, packed with fiber and magnesium for overall health.", 50, R.drawable.dragon_fruit),
            Fruit("Pomegranate", "A fruit with juicy, antioxidant-rich arils that support heart health and reduce inflammation.", 83, R.drawable.pomegranate),
            Fruit("Avocado", "A creamy fruit high in heart-healthy monounsaturated fats, potassium, and fiber, promoting brain and heart health.", 160, R.drawable.avocado),
            Fruit("Coconut", "A tropical fruit with rich flesh, offering healthy fats and medium-chain triglycerides for energy and metabolism.", 354, R.drawable.coconut),
            Fruit("Cherry", "A sweet and juicy fruit with anti-inflammatory properties, rich in vitamins A and C for immune and skin health.", 50, R.drawable.cherry),
            Fruit("Guava", "A tropical fruit with a fragrant aroma, rich in dietary fiber, vitamin C, and antioxidants, aiding digestion and immunity.", 68, R.drawable.guava),
            Fruit("Jackfruit", "A large tropical fruit with sweet and fibrous texture, rich in potassium and antioxidants.", 95, R.drawable.jackfruit),
            Fruit("Lemon", "A citrus fruit known for its tangy flavor and high vitamin C content, supporting immunity and alkalizing the body.", 29, R.drawable.lemon),
            Fruit("Lime", "A tart citrus fruit with a high concentration of vitamin C and antioxidants, often used for detox and flavor.", 30, R.drawable.lime),
            Fruit("Plum", "A sweet and juicy purple fruit with vitamins A and C, beneficial for skin and digestive health.", 46, R.drawable.plum),
            Fruit("Raspberry", "A small red berry rich in fiber, antioxidants, and ellagic acid, promoting heart health and anti-aging.", 52, R.drawable.raspberries),
            Fruit("Blackberry", "A nutrient-dense berry with a sweet-tart flavor, packed with vitamins C and K, aiding immunity and bone health.", 43, R.drawable.blackberry),
            Fruit("Cranberry", "A tart, red berry known for its role in urinary tract health and high antioxidant content.", 46, R.drawable.cranberry),
            Fruit("Durian", "A large, spiky tropical fruit with a strong aroma and creamy texture, rich in healthy fats and nutrients.", 147, R.drawable.durian),
            Fruit("Fig", "A sweet fruit with soft flesh and an excellent source of dietary fiber, calcium, and potassium for bone health.", 74, R.drawable.fig),
            Fruit("Gooseberry", "A small, tart fruit rich in vitamin C and antioxidants, supporting skin health and immune function.", 44, R.drawable.gooseberry),
            Fruit("Passion Fruit", "A tropical fruit with sweet-tart flavor, rich in fiber, vitamin A, and antioxidants.", 97, R.drawable.passion_fruit),
            Fruit("Pear", "A soft, juicy fruit with a buttery texture, high in fiber and vitamin C, promoting digestion and immunity.", 57, R.drawable.pear),
            Fruit("Apricot", "A small, sweet fruit with a tangy flavor, rich in vitamin A and beta-carotene for eye health.", 48, R.drawable.apricot),
            Fruit("Cantaloupe", "A juicy melon with orange flesh, high in hydration and beta-carotene for skin and eye health.", 34, R.drawable.cantaloupe),
            Fruit("Starfruit", "A tropical fruit with a unique star shape and tart flavor, rich in vitamin C and antioxidants.", 31, R.drawable.starfruit),
            Fruit("Custard Apple", "A creamy tropical fruit with custard-like texture, high in vitamins B6 and C, promoting brain and immune health.", 94, R.drawable.custard_appel),
            Fruit("Mulberry", "A sweet, dark berry rich in resveratrol, vitamin C, and iron for skin and blood health.", 43, R.drawable.mulberry),
            Fruit("Tangerine", "A smaller citrus fruit with a sweeter taste, high in vitamin C and flavonoids for skin and immune support.", 53, R.drawable.tangerine),
            Fruit("Persimmon", "A vibrant orange fruit with natural sweetness, high in fiber and antioxidants for digestive health.", 70, R.drawable.persimmon),
            Fruit("Quince", "A tart fruit often used in preserves, rich in dietary fiber and antioxidants, supporting digestion.", 57, R.drawable.quince),
            Fruit("Elderberry", "A small, dark fruit with immune-boosting properties and a rich source of antioxidants and vitamin C.", 73, R.drawable.elderberry),
            Fruit("Rambutan", "A tropical fruit with a sweet and juicy flavor, high in vitamin C and beneficial for skin health.", 68, R.drawable.rambutan)
        )


        val recyclerView = findViewById<RecyclerView>(R.id.fruitRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyFruitViewAdapter(fruitList)
    }

}
