package com.example.balancebite

data class DietPlanGroup(
    val ageGroup: String,
    val dietPlans: List<DietPlan>
)

data class DietPlan(
    val day: String,
    val meals: String
) {
    val snack2: String = ""
    val snack: String = ""
    val dinner: String = ""
    val lunch: String = ""
    val breakfast: String = ""
}

