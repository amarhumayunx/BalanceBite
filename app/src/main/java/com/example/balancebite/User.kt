package com.example.balancebite

// User.kt
data class User(
    val userId: String? = null,
    val name: String? = null,
    val age: String = null.toString(),
    val height: Int? = null,
    val weight: Int? = null,
    val email: String? = null
)

