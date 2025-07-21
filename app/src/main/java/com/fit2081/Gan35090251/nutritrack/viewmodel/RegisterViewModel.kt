package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PatientRepository(application.applicationContext)

    var selectedUserId by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var userDataMap by mutableStateOf<Map<String, Patient>>(emptyMap())
        private set

    var isDropdownExpanded by mutableStateOf(false)
        private set

    val userIdList: List<String>
        get() = userDataMap.keys.toList()

    fun updateUserId(value: String) {
        selectedUserId = value
    }

    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updateConfirmPassword(value: String) {
        confirmPassword = value
    }

    fun toggleDropdownExpanded() {
        isDropdownExpanded = !isDropdownExpanded
    }

    fun dismissDropdown() {
        isDropdownExpanded = false
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            val allPatients = repository.getAllPatients()
            userDataMap = allPatients.associateBy { it.userId.toString() }
        }
    }

    fun isUserAlreadyRegistered(): Boolean {
        return userDataMap[selectedUserId]?.password?.isNotBlank() == true
    }

    fun registerPatient(
        onRegistered: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userIdLong = selectedUserId.toLongOrNull()
        if (userIdLong == null) {
            onFailure("User ID must be a number.")
            return
        }
        if (phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            onFailure("All fields must be filled.")
            return
        }
        if (password != confirmPassword) {
            onFailure("Passwords do not match.")
            return
        }

        viewModelScope.launch {
            val existing = repository.getPatientByUserId(userIdLong)

            when {
                existing == null -> {
                    onFailure("User ID not found.")
                }
                existing.password.isNotBlank() -> {
                    onFailure("This user is already registered.")
                }
                existing.phoneNumber != phoneNumber -> {
                    onFailure("Phone number does not match our records.")
                }
                else -> {
                    repository.updatePassword(userIdLong, password)
                    onRegistered()
                }
            }
        }
    }
}
