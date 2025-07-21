package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PatientRepository(application.applicationContext)
    private val _patient = MutableStateFlow<Patient?>(null)
    val patient: StateFlow<Patient?> = _patient

    init {
        loadCurrentPatient()
    }

    private fun loadCurrentPatient() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val userId = prefs.getString("current_user_id", null)?.toLongOrNull()
            if (userId != null) {
                _patient.value = repository.getPatientByUserId(userId)
            }
        }
    }
}
