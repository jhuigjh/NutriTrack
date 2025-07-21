package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {

    private val patientRepository = PatientRepository(application)

    private val _insightsData = MutableStateFlow<Map<String, Float>>(emptyMap())
    val insightsData = _insightsData.asStateFlow()

    private val _totalScore = MutableStateFlow(0f)
    val totalScore = _totalScore.asStateFlow()

    init {
        loadInsights()
    }

    private fun loadInsights() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val userId = prefs.getString("current_user_id", null)?.toLongOrNull()

            if (userId != null) {
                val patient = patientRepository.getPatientByUserId(userId)
                if (patient != null) {
                    val isMale = patient.sex == "Male"

                    fun value(male: Double, female: Double) = if (isMale) male else female

                    _insightsData.value = mapOf(
                        "Discretionary" to value(patient.discretionaryHeifaScoreMale, patient.discretionaryHeifaScoreFemale).toFloat(),
                        "Vegetables" to value(patient.vegetablesHeifaScoreMale, patient.vegetablesHeifaScoreFemale).toFloat(),
                        "Fruits" to value(patient.fruitHeifaScoreMale, patient.fruitHeifaScoreFemale).toFloat(),
                        "Grains & Cereals" to value(patient.grainsAndCerealsHeifaScoreMale, patient.grainsAndCerealsHeifaScoreFemale).toFloat(),
                        "Meat & Alternatives" to value(patient.meatAndAlternativesHeifaScoreMale, patient.meatAndAlternativesHeifaScoreFemale).toFloat(),
                        "Dairy & Alternatives" to value(patient.dairyAndAlternativesHeifaScoreMale, patient.dairyAndAlternativesHeifaScoreFemale).toFloat(),
                        "Sodium" to value(patient.sodiumHeifaScoreMale, patient.sodiumHeifaScoreFemale).toFloat(),
                        "Alcohol" to value(patient.alcoholHeifaScoreMale, patient.alcoholHeifaScoreFemale).toFloat(),
                        "Water" to value(patient.waterHeifaScoreMale, patient.waterHeifaScoreFemale).toFloat(),
                        "Added Sugars" to value(patient.sugarHeifaScoreMale, patient.sugarHeifaScoreFemale).toFloat(),
                        "Unsaturated Fat" to value(patient.unsaturatedFatHeifaScoreMale, patient.unsaturatedFatHeifaScoreFemale).toFloat(),
                    )

                    _totalScore.value = value(patient.heifaTotalScoreMale, patient.heifaTotalScoreFemale).toFloat()
                }
            }
        }
    }
}
