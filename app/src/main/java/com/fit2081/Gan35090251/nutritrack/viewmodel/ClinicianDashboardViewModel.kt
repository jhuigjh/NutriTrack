package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.GeminiRequest
import com.fit2081.Gan35090251.nutritrack.data.entity.GeminiResponse
import com.fit2081.Gan35090251.nutritrack.data.network.GeminiApiService
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClinicianDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val patientRepository = PatientRepository(application)

    private val geminiApi = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeminiApiService::class.java)

    private val apiKey = "AIzaSyDaw8VjUUAGyhW3g1h0mS_RNba6WM5vtVY"

    var maleAverage by mutableStateOf<Float?>(null)
        private set

    var femaleAverage by mutableStateOf<Float?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var insights by mutableStateOf(listOf<String>())
        private set

    init {
        fetchAverages()
    }

    private fun fetchAverages() {
        viewModelScope.launch {
            try {
                maleAverage = patientRepository.getAverageHeifaScoreMale()?.toFloat()
                femaleAverage = patientRepository.getAverageHeifaScoreFemale()?.toFloat()
            } catch (e: Exception) {
                errorMessage = "Error loading averages: ${e.localizedMessage}"
            }
        }
    }

    fun analyzeDataPatterns() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val prompt = "Based on HEIFA score data grouped by gender, generate exactly 3 concise and insightful bullet points showing patterns or trends. Each bullet point should be on its own line. (No introduction)"

                val request = GeminiRequest(
                    contents = listOf(
                        GeminiRequest.Content(
                            parts = listOf(GeminiRequest.Part(text = prompt))
                        )
                    )
                )

                val response: GeminiResponse = geminiApi.generateContent(apiKey = apiKey, request = request)

                val generatedText = response.candidates
                    .firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text
                    ?.trim()
                    ?: "No insights could be generated."

                insights = generatedText
                    .split("\n")
                    .map { it.trimStart('•', '-', ' ', '\t', '1', '2', '3', '.', ')') }
                    .filter { it.isNotBlank() }
                    .take(3)

            } catch (e: Exception) {
                errorMessage = "Error analyzing data: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
