package com.example.balancebite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VegetableAdapter(private val vegetableList: List<Vegetable>) :
    RecyclerView.Adapter<VegetableAdapter.VegetableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VegetableViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.vegetable_view, parent, false)
        return VegetableViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VegetableViewHolder, position: Int) {
        val vegetable = vegetableList[position]
        holder.nameTextView.text = vegetable.name
        holder.descriptionTextView.text = vegetable.description
        holder.caloriesTextView.text = "${vegetable.calories} kcal"
        holder.imageView.setImageResource(vegetable.imageResource)
    }

    override fun getItemCount() = vegetableList.size

    class VegetableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.vegetableNameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.vegetableDescriptionTextView)
        val caloriesTextView: TextView = itemView.findViewById(R.id.vegetableCaloriesTextView)
        val imageView: ImageView = itemView.findViewById(R.id.vegetableImageView)
    }
}
