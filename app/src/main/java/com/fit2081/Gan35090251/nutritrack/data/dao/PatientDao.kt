package com.fit2081.Gan35090251.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient

@Dao
interface PatientDao {

    @Query("SELECT * FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getPatientByUserId(userId: Long): Patient?

    @Query("SELECT * FROM Patient")
    suspend fun getAllPatients(): List<Patient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<Patient>)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Query("UPDATE Patient SET password = :newPassword WHERE userId = :userId")
    suspend fun updatePassword(userId: Long, newPassword: String)

    @Query("SELECT AVG(heifaTotalScoreMale) FROM patient WHERE sex = 'Male'")
    suspend fun getAverageHeifaScoreMale(): Double?

    @Query("SELECT AVG(heifaTotalScoreFemale) FROM patient WHERE sex = 'Female'")
    suspend fun getAverageHeifaScoreFemale(): Double?

}
