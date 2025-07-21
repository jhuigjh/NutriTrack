package com.fit2081.Gan35090251.nutritrack.repository

import com.fit2081.Gan35090251.nutritrack.data.dao.NutriCoachDao
import com.fit2081.Gan35090251.nutritrack.data.dao.PatientDao
import com.fit2081.Gan35090251.nutritrack.data.entity.GeminiRequest
import com.fit2081.Gan35090251.nutritrack.data.entity.NutriCoach
import com.fit2081.Gan35090251.nutritrack.data.network.GeminiApiService
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import kotlinx.coroutines.flow.Flow

class NutriCoachRepository(
    private val nutriCoachDao: NutriCoachDao,
    private val patientDao: PatientDao,
    private val geminiApi: GeminiApiService,
    private val apiKey: String
) {
    suspend fun generateMotivationalMessageAndSave(patient: Patient, prompt: String): String {
        val request = GeminiRequest(
            contents = listOf(
                GeminiRequest.Content(
                    parts = listOf(GeminiRequest.Part(text = prompt))
                )
            )
        )

        val response = geminiApi.generateContent(apiKey = apiKey, request = request)
        val message = response.candidates
            .firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
            ?: "Keep up your nutrition journey with more fruit!"

        nutriCoachDao.insertMessage(NutriCoach(message = message, userId = patient.userId))

        return message
    }

    suspend fun getPatientByUserId(userId: Long): Patient? {
        return patientDao.getPatientByUserId(userId)
    }

    fun getMessagesByUser(userId: Long): Flow<List<NutriCoach>> {
        return nutriCoachDao.getMessagesByUser(userId)
    }
}
