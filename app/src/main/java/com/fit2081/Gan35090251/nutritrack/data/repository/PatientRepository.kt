package com.fit2081.Gan35090251.nutritrack.data.repository

import android.content.Context
import com.fit2081.Gan35090251.nutritrack.data.AppDatabase
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient

class PatientRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val patientDao = db.patientDao()


    suspend fun insertAllPatients(patients: List<Patient>) {
        patientDao.insertPatients(patients)
    }

    suspend fun getPatientByUserId(userId: Long): Patient? {
        return patientDao.getPatientByUserId(userId)
    }

    suspend fun getAllPatients(): List<Patient> {
        return patientDao.getAllPatients()
    }

    suspend fun updatePassword(userId: Long, newPassword: String) {
        patientDao.updatePassword(userId, newPassword)
    }

    suspend fun getAverageHeifaScoreMale(): Double? {
        return patientDao.getAverageHeifaScoreMale()
    }

    suspend fun getAverageHeifaScoreFemale(): Double? {
        return patientDao.getAverageHeifaScoreFemale()
    }
}
