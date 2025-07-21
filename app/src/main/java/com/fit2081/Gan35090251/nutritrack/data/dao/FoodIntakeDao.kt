package com.fit2081.Gan35090251.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fit2081.Gan35090251.nutritrack.data.entity.FoodIntake

@Dao
interface FoodIntakeDao {
    @Query("SELECT * FROM FoodIntake WHERE patientUserId = :patientUserId LIMIT 1")
    suspend fun getFoodIntakeByUserId(patientUserId: Long): FoodIntake?

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)
}
