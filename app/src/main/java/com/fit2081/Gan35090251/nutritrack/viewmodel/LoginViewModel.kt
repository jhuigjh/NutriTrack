// LoginViewModel.kt
package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import com.fit2081.Gan35090251.nutritrack.data.repository.FoodIntakeRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val patientRepository = PatientRepository(application.applicationContext)
    private val foodIntakeRepository = FoodIntakeRepository(application.applicationContext)

    var isBottomSheetVisible by mutableStateOf(false)
        private set

    var selectedOption by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isDropdownExpanded by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf<Boolean?>(null)
        private set

    var userDataMap by mutableStateOf<Map<String, Patient>>(emptyMap())
        private set

    val userIdList: List<String>
        get() = userDataMap.keys.toList()

    fun updateDropdownExpanded(expanded: Boolean) {
        isDropdownExpanded = expanded
    }

    fun toggleBottomSheet() {
        isBottomSheetVisible = !isBottomSheetVisible
    }

    fun updateSelectedOption(option: String) {
        selectedOption = option
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            val allPatients = patientRepository.getAllPatients()
            userDataMap = allPatients.associateBy { it.userId.toString() }
        }
    }

    fun validateLogin(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        onUnregistered: () -> Unit
    ) {
        viewModelScope.launch {
            val userIdLong = selectedOption.toLongOrNull()

            if (userIdLong == null) {
                loginSuccess = false
                onFailure()
                return@launch
            }

            val patient = patientRepository.getPatientByUserId(userIdLong)

            when {
                patient == null -> {
                    loginSuccess = false
                    onFailure()
                }
                patient.password.isNullOrBlank() -> {
                    loginSuccess = false
                    onUnregistered()
                }
                patient.password == password -> {
                    saveLoggedInUser(context, patient.userId.toString())
                    loginSuccess = true
                    onSuccess()
                }
                else -> {
                    loginSuccess = false
                    onFailure()
                }
            }
        }
    }

    suspend fun hasCompletedQuestionnaire(userId: Long): Boolean {
        return foodIntakeRepository.hasCompletedQuestionnaire(userId)
    }

    private fun saveLoggedInUser(context: Context, userId: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("current_user_id", userId).apply()
    }
}
