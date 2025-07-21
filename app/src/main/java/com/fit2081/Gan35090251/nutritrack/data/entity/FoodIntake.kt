package com.fit2081.Gan35090251.nutritrack.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "FoodIntake",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["patientUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val patientUserId: Long,

    val persona: String,
    val biggestMealTime: String,
    val sleepTime: String,
    val wakeUpTime: String,
    val fruits: Boolean,
    val vegetables: Boolean,
    val grains: Boolean,
    val redMeat: Boolean,
    val seafood: Boolean,
    val poultry: Boolean,
    val fish: Boolean,
    val eggs: Boolean,
    val nutsSeeds: Boolean
)
