package com.example.balancebite

data class DietPlanGroup(
    val ageGroup: String,
    val dietPlans: List<DietPlan>
)

data class DietPlan(
    val day: String,
    val meals: String
)

