package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val patientRepository = PatientRepository(application)

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _score = MutableStateFlow(0f)
    val score: StateFlow<Float> = _score

    fun loadUserData(context: Context) {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val userId = prefs.getString("current_user_id", null)?.toLongOrNull()
            if (userId != null) {
                val patient = patientRepository.getPatientByUserId(userId)
                patient?.let {
                    _userName.value = "${it.userId}"
                    _score.value = if (it.sex == "Male") it.heifaTotalScoreMale.toFloat()
                    else it.heifaTotalScoreFemale.toFloat()
                }
            }
        }
    }
}
