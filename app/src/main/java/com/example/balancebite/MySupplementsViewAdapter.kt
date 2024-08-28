package com.example.balancebite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SupplementAdapter(private val supplementList: List<Supplement>) :
    RecyclerView.Adapter<SupplementAdapter.SupplementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplementViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.supplements_view, parent, false)
        return SupplementViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SupplementViewHolder, position: Int) {
        val currentSupplement = supplementList[position]
        holder.supplementName.text = currentSupplement.name
        holder.supplementDescription.text = currentSupplement.description
        holder.supplementCalories.text = "Calories: ${currentSupplement.calories}"
        holder.supplementImage.setImageResource(currentSupplement.imageResource)
    }

    override fun getItemCount() = supplementList.size

    class SupplementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val supplementName: TextView = itemView.findViewById(R.id.supplementName)
        val supplementDescription: TextView = itemView.findViewById(R.id.supplementDescription)
        val supplementCalories: TextView = itemView.findViewById(R.id.supplementCalories)
        val supplementImage: ImageView = itemView.findViewById(R.id.supplementImage)
    }
}
