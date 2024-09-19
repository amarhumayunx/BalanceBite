package com.example.balancebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DietPlanInnerAdapter(private val dietPlans: List<DietPlan>) : RecyclerView.Adapter<DietPlanInnerAdapter.DietPlanViewHolder>() {

    class DietPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.tvDay)
        val mealsTextView: TextView = itemView.findViewById(R.id.tvMeals)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diet_plan, parent, false)
        return DietPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietPlanViewHolder, position: Int) {
        val dietPlan = dietPlans[position]
        holder.dayTextView.text = dietPlan.day
        holder.mealsTextView.text = dietPlan.meals
    }

    override fun getItemCount(): Int {
        return dietPlans.size
    }
}
