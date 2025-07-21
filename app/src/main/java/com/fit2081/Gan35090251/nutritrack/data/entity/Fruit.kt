package com.fit2081.Gan35090251.nutritrack.data.entity

data class FruitInfo(
    val name: String,
    val id: Int,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)
