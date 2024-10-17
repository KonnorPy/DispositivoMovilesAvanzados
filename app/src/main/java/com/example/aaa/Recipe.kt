package com.example.aaa

data class Recipe(
    val name: String,
    val preparationTime: String,
    val ingredients: List<String>,
    val calories: Int,
    val imageUrl: String
)
