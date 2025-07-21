package com.fit2081.Gan35090251.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.Gan35090251.nutritrack.data.entity.NutriCoach
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(coach: NutriCoach)

    @Query("SELECT * FROM nutri_coach WHERE userId = :userId ORDER BY id DESC")
    fun getMessagesByUser(userId: Long): Flow<List<NutriCoach>>
}
