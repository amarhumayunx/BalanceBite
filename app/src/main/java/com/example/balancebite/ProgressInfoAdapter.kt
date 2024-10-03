package com.example.balancebite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProgressAdapter(private val progressList: List<ProgressEntry>) : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.progressDay)
        val caloriesTextView: TextView = itemView.findViewById(R.id.progressCalories)
        val waterTextView: TextView = itemView.findViewById(R.id.progressWater)
        val exerciseTimeTextView: TextView = itemView.findViewById(R.id.progressExercise)
        val weightTextView: TextView = itemView.findViewById(R.id.progressWeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track_progress_info, parent, false)
        return ProgressViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val progressEntry = progressList[position]
        holder.dayTextView.text = "Day: ${progressEntry.day}"
        holder.caloriesTextView.text = "Calories: ${progressEntry.calories}"
        holder.waterTextView.text = "Water: ${progressEntry.water} glasses"
        holder.exerciseTimeTextView.text = "Exercise Time: ${progressEntry.exerciseTime} min"
        holder.weightTextView.text = "Weight: ${progressEntry.weight} kg"
    }

    override fun getItemCount(): Int {
        return progressList.size
    }
}

