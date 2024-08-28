package com.example.balancebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyFruitViewAdapter(private val fruits: List<Fruit>) :
    RecyclerView.Adapter<MyFruitViewAdapter.FruitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_view, parent, false)
        return FruitViewHolder(view)
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        val fruit = fruits[position]
        holder.bind(fruit)
    }

    override fun getItemCount(): Int {
        return fruits.size
    }

    class FruitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fruitName = itemView.findViewById<TextView>(R.id.fruitName)
        private val fruitDescription = itemView.findViewById<TextView>(R.id.fruitDescription)
        private val fruitCalories = itemView.findViewById<TextView>(R.id.fruitCalories)
        private val fruitImage = itemView.findViewById<ImageView>(R.id.fruitImage)

        fun bind(fruit: Fruit) {
            fruitName.text = fruit.name
            fruitDescription.text = fruit.description
            fruitCalories.text = "${fruit.calories} kcal"
            fruitImage.setImageResource(fruit.imageResource)
        }
    }
}

