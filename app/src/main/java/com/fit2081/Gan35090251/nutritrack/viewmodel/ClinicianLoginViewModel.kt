package com.fit2081.Gan35090251.nutritrack.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ClinicianLoginViewModel : ViewModel() {
    var clinicianKey = mutableStateOf("")
    var loginError = mutableStateOf("")

    private val validKey = "dollar-entry-apples"

    fun onKeyChange(newKey: String) {
        clinicianKey.value = newKey
    }

    fun validateKey(onSuccess: () -> Unit) {
        if (clinicianKey.value == validKey) {
            loginError.value = ""
            onSuccess()
        } else {
            loginError.value = "Invalid key. Please try again."
        }
    }
}
