package com.example.balancebite

import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RecipesActivity : AppCompatActivity() {

    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitleView: TextView
    private lateinit var recipeIngredientsView: TextView
    private lateinit var recipePreparationView: TextView
    private lateinit var nextRecipeButton: Button

    private lateinit var database: DatabaseReference
    private var recipeList: List<Map<String, String>> = mutableListOf()
    private var currentRecipeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)

        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        // Initialize views
        recipeImageView = findViewById(R.id.recipeImage)
        recipeTitleView = findViewById(R.id.recipeTitle)
        recipeIngredientsView = findViewById(R.id.recipeIngredients)
        recipePreparationView = findViewById(R.id.recipePreparation)
        nextRecipeButton = findViewById(R.id.nextRecipeButton)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference.child("recipe")

        // Fetch recipes from Firebase
        fetchRecipes()

        // Button to show next recipe
        nextRecipeButton.setOnClickListener {
            currentRecipeIndex++
            if (currentRecipeIndex < recipeList.size) {
                loadRecipeWithAnimation(recipeList[currentRecipeIndex])
            } else {
                currentRecipeIndex = 0  // Reset to the first recipe if the end is reached
                loadRecipeWithAnimation(recipeList[currentRecipeIndex])
            }
        }
    }

    private fun fetchRecipes() {
        database.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val recipes = snapshot.children.mapNotNull { childSnapshot ->
                    val recipeData = childSnapshot.value as? Map<String, String>
                    recipeData?.toMutableMap()?.apply {
                        this["title"] = childSnapshot.key ?: "Untitled Recipe"
                    }
                }
                recipeList = recipes
                // Load the first recipe
                if (recipeList.isNotEmpty()) {
                    loadRecipeWithAnimation(recipeList[currentRecipeIndex])
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseData", "Error fetching recipes", exception)
        }
    }

    private fun loadRecipeWithAnimation(recipe: Map<String, String>) {
        val title = recipe["title"] ?: "Untitled Recipe"
        val imageUrl = recipe["image"] ?: ""
        val ingredients = recipe["ingredients"] ?: "No ingredients found"
        val preparation = recipe["preparation"] ?: "No preparation steps found"

        // Set title
        recipeTitleView.text = title

        // Display image using Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_image)
            .into(recipeImageView)

        // Set ingredients and preparation
        recipeIngredientsView.text = ingredients
        recipePreparationView.text = preparation

        // Apply fade-in animation
        applyFadeInAnimation(recipeImageView)
        applyFadeInAnimation(recipeTitleView)
        applyFadeInAnimation(recipeIngredientsView)
        applyFadeInAnimation(recipePreparationView)

        // Apply slide-in animation to the button
        applySlideInAnimation(nextRecipeButton)
    }

    private fun applyFadeInAnimation(view: android.view.View) {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        view.startAnimation(fadeIn)
    }

    private fun applySlideInAnimation(view: android.view.View) {
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        view.startAnimation(slideIn)
    }
}