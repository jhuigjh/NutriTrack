package com.fit2081.Gan35090251.nutritrack.data.repository

import android.content.Context
import com.fit2081.Gan35090251.nutritrack.data.AppDatabase
import com.fit2081.Gan35090251.nutritrack.data.entity.FoodIntake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodIntakeRepository(context: Context) {

    private val foodIntakeDao = AppDatabase.getDatabase(context).foodIntakeDao()

    suspend fun insertFoodIntake(foodIntake: FoodIntake) {
        withContext(Dispatchers.IO) {
            foodIntakeDao.insertFoodIntake(foodIntake)
        }
    }

    suspend fun getFoodIntakeByUserId(patientUserId: Long): FoodIntake? {
        return withContext(Dispatchers.IO) {
            foodIntakeDao.getFoodIntakeByUserId(patientUserId)
        }
    }

    suspend fun updateFoodIntake(foodIntake: FoodIntake) {
        withContext(Dispatchers.IO) {
            foodIntakeDao.updateFoodIntake(foodIntake)
        }
    }

    suspend fun hasCompletedQuestionnaire(userId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            foodIntakeDao.getFoodIntakeByUserId(userId) != null
        }
    }
}
