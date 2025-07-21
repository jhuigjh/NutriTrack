package com.fit2081.Gan35090251.nutritrack.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutri_coach")
data class NutriCoach(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String,
    val userId: Long )
