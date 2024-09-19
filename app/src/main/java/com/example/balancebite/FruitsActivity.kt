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
            Fruit("Apple", "A sweet red fruit", 52, R.drawable.apple),
            Fruit("Banana", "A long yellow fruit", 89, R.drawable.banana),
            Fruit("Orange", "A citrus fruit with a tangy taste", 47, R.drawable.orange),
            Fruit("Strawberry", "A juicy red berry", 32, R.drawable.strawberry),
            Fruit("Grapes", "Small and sweet green or red fruits", 69, R.drawable.grape),
            Fruit("Watermelon", "A large green fruit with red flesh", 30, R.drawable.watermelon),
            Fruit("Pineapple", "A tropical fruit with a spiky exterior", 50, R.drawable.pineapple),
            Fruit("Mango", "A sweet and juicy tropical fruit", 60, R.drawable.mango),
            Fruit("Blueberry", "A small, sweet, and tangy blue fruit", 57, R.drawable.blueberry),
            Fruit("Peach", "A soft and fuzzy fruit with a sweet taste", 39, R.drawable.peach),
            Fruit("Kiwi", "A small, brown fruit with green flesh", 61, R.drawable.kiwi),
            Fruit("Papaya", "A tropical fruit with orange flesh", 43, R.drawable.papaya),
            Fruit("Lychee", "A small, sweet, and juicy fruit", 66, R.drawable.lychee),
            Fruit("Dragon Fruit", "A vibrant fruit with white or red flesh", 50, R.drawable.dragon_fruit),
            Fruit("Pomegranate", "A fruit filled with juicy seeds", 83, R.drawable.pomegranate),
            Fruit("Avocado", "A creamy green fruit high in healthy fats", 160, R.drawable.avocado),
            Fruit("Coconut", "A tropical fruit with a hard shell and white flesh", 354, R.drawable.coconut),
            Fruit("Cherry", "A small, round, and sweet red fruit", 50, R.drawable.cherry),
            Fruit("Guava", "A tropical fruit with a sweet and tart flavor", 68, R.drawable.guava),
            Fruit("Jackfruit", "A large tropical fruit with a sweet taste", 95, R.drawable.jackfruit),
            Fruit("Lemon", "A citrus fruit with a tart flavor", 29, R.drawable.lemon),
            Fruit("Lime", "A small, green citrus fruit", 30, R.drawable.lime),
            Fruit("Plum", "A sweet and juicy purple fruit", 46, R.drawable.plum),
            Fruit("Raspberry", "A small, red, and tart berry", 52, R.drawable.raspberries),
            Fruit("Blackberry", "A small, black, and juicy berry", 43, R.drawable.blackberry),
            Fruit("Cranberry", "A tart red berry", 46, R.drawable.cherries),
            Fruit("Durian", "A large, spiky fruit with a strong smell", 147, R.drawable.durian),
            Fruit("Fig", "A sweet fruit with a soft texture", 74, R.drawable.figs),
            Fruit("Gooseberry", "A small, tart, and green fruit", 44, R.drawable.gooseberry),
            Fruit("Passion Fruit", "A tropical fruit with a sweet-tart taste", 97, R.drawable.passion_fruit),
            Fruit("Pear", "A sweet fruit with a juicy texture", 57, R.drawable.pear),
        )


        val recyclerView = findViewById<RecyclerView>(R.id.fruitRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyFruitViewAdapter(fruitList)
    }

}
