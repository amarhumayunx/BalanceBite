package com.example.balancebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ageGroup: TextView = itemView.findViewById(R.id.tvAgeGroup)
        val activities: TextView = itemView.findViewById(R.id.tvActivities)
        val recommendedTime: TextView = itemView.findViewById(R.id.tvRecommendedTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.ageGroup.text = exercise.ageGroup
        holder.activities.text = exercise.activities
        holder.recommendedTime.text = exercise.recommendedTime
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}