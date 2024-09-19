package com.example.balancebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DietPlanAdapter(private val dietPlanGroups: List<DietPlanGroup>) : RecyclerView.Adapter<DietPlanAdapter.DietPlanGroupViewHolder>() {

    class DietPlanGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ageGroupTextView: TextView = itemView.findViewById(R.id.tvAgeGroup)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.rvDietPlansInGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietPlanGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diet_plan_group, parent, false)
        return DietPlanGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietPlanGroupViewHolder, position: Int) {
        val dietPlanGroup = dietPlanGroups[position]
        holder.ageGroupTextView.text = dietPlanGroup.ageGroup

        // Set up the inner RecyclerView for diet plans
        val innerAdapter = DietPlanInnerAdapter(dietPlanGroup.dietPlans)
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.adapter = innerAdapter
    }

    override fun getItemCount(): Int {
        return dietPlanGroups.size
    }
}


