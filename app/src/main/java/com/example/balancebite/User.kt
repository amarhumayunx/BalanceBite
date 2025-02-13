package com.example.balancebite

// User.kt
data class UserMiniProfiler(
    val age: Int = 0,
    val healthInfo: String = "",
    val height: Int = 0,
    val name: String = "",
    val profilePictureUrl: String = "",
    val weight: Int = 0
)


data class User(
    val userId: String = "",
    val progressId: String = "",
    val email: String = "",
    val profile: UserMiniProfiler = UserMiniProfiler(),
    val progress: ProgressEntry = ProgressEntry()
)