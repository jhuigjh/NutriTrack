package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val patientRepository = PatientRepository(context)

    private val _patient = MutableStateFlow<Patient?>(null)
    val patient = _patient.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow("")
    val successMessage = _successMessage.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val userId = prefs.getString("current_user_id", null)?.toLongOrNull()
            if (userId != null) {
                _patient.value = patientRepository.getPatientByUserId(userId)
            }
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _currentPassword.value = value
    }

    fun onNewPasswordChange(value: String) {
        _newPassword.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun changePassword() {
        val currentUser = _patient.value

        if (currentUser == null) {
            _errorMessage.value = "No user loaded."
            _successMessage.value = ""
            return
        }

        when {
            _currentPassword.value != currentUser.password -> {
                _errorMessage.value = "Current password is incorrect."
                _successMessage.value = ""
            }

            _newPassword.value != _confirmPassword.value -> {
                _errorMessage.value = "New passwords do not match."
                _successMessage.value = ""
            }

            else -> {
                viewModelScope.launch {
                    patientRepository.updatePassword(currentUser.userId, _newPassword.value)
                    _successMessage.value = "Password updated successfully!"
                    _errorMessage.value = ""
                    _patient.value = patientRepository.getPatientByUserId(currentUser.userId)

                    _currentPassword.value = ""
                    _newPassword.value = ""
                    _confirmPassword.value = ""
                }
            }
        }
    }
}
