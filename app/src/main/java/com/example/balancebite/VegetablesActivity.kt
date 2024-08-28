package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VegetableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vegetables)

        val vegetableList = listOf(
            Vegetable("Carrot", "A crunchy orange vegetable", 41, R.drawable.carrots),
            Vegetable("Broccoli", "A green vegetable rich in fiber", 55, R.drawable.broccoli),
            Vegetable("Spinach", "A leafy green vegetable", 23, R.drawable.spinach),
            Vegetable("Tomato", "A red, juicy fruit often treated as a vegetable", 18, R.drawable.tomato),
            Vegetable("Potato", "A starchy tuber that is a staple food", 77, R.drawable.potato),
            Vegetable("Cucumber", "A cool, crunchy vegetable often used in salads", 16, R.drawable.cucumber),
            Vegetable("Bell Pepper", "A sweet and colorful vegetable", 20, R.drawable.bell_pepper),
            Vegetable("Onion", "A pungent vegetable used in various cuisines", 40, R.drawable.onions),
            Vegetable("Garlic", "A strong-flavored vegetable used for seasoning", 149, R.drawable.garlics),
            Vegetable("Lettuce", "A leafy vegetable often used in salads", 15, R.drawable.lettuce),
            Vegetable("Cabbage", "A leafy green, white, or purple vegetable", 25, R.drawable.cabbage),
            Vegetable("Zucchini", "A summer squash with a mild flavor", 17, R.drawable.zucchini),
            Vegetable("Eggplant", "A purple vegetable also known as aubergine", 25, R.drawable.eggplant),
            Vegetable("Pumpkin", "A large, orange squash often used in soups and pies", 26, R.drawable.pumpkin),
            Vegetable("Celery", "A crunchy, fibrous vegetable often used in salads and soups", 14, R.drawable.celery),
            Vegetable("Cauliflower", "A white, fibrous vegetable similar to broccoli", 25, R.drawable.cauliflower),
            Vegetable("Brussels Sprouts", "A small, leafy green vegetable resembling mini cabbages", 43, R.drawable.brussel_sprouts),
            Vegetable("Peas", "Small green seeds that are sweet and starchy", 81, R.drawable.pea),
            Vegetable("Sweet Corn", "A starchy vegetable with kernels on a cob", 86, R.drawable.fresh),
            Vegetable("Mushroom", "A fleshy fungus often used in cooking", 22, R.drawable.mushrooms),
            Vegetable("Kale", "A leafy green vegetable rich in vitamins", 49, R.drawable.mustard_greens),
            Vegetable("Asparagus", "A long, green vegetable with a distinct flavor", 20, R.drawable.asparagus),
            Vegetable("Beetroot", "A deep red or purple root vegetable", 43, R.drawable.beetroot),
            Vegetable("Radish", "A spicy, crunchy root vegetable", 16, R.drawable.radish),
            Vegetable("Turnip", "A root vegetable often used in soups and stews", 28, R.drawable.turnip),
            Vegetable("Leek", "A vegetable related to onions with a milder flavor", 61, R.drawable.leek),
            Vegetable("Ginger", "A spicy root used for flavoring and medicinal purposes", 80, R.drawable.ginger),
            Vegetable("Chili Pepper", "A spicy vegetable used to add heat to dishes", 40, R.drawable.chili),
            Vegetable("Parsnip", "A root vegetable similar to carrots, but sweeter", 75, R.drawable.parsnip)
        )


        val recyclerView = findViewById<RecyclerView>(R.id.vegetableRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VegetableAdapter(vegetableList)
    }
}
