package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.entity.FoodIntake
import com.fit2081.Gan35090251.nutritrack.data.repository.FoodIntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodIntakeRepository(application.applicationContext)

    val foodItems = listOf(
        "Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds"
    )

    val personaOptions = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )

    val checkedState = mutableStateListOf<Boolean>().apply {
        repeat(foodItems.size) { add(false) }
    }

    val selectedPersona = mutableStateOf("")
    val biggestMealTime = mutableStateOf("00:00")
    val sleepTime = mutableStateOf("00:00")
    val wakeUpTime = mutableStateOf("00:00")

    private val _dropdownExpanded = MutableStateFlow(false)
    val dropdownExpanded: StateFlow<Boolean> = _dropdownExpanded

    private var currentUserId: Long? = null
    private var foodIntakeId: Long? = null

    fun setChecked(index: Int, isChecked: Boolean) {
        if (index in checkedState.indices) {
            checkedState[index] = isChecked
        }
    }

    fun setSelectedPersona(persona: String) {
        selectedPersona.value = persona
    }

    fun setBiggestMealTime(time: String) {
        biggestMealTime.value = time
    }

    fun setSleepTime(time: String) {
        sleepTime.value = time
    }

    fun setWakeUpTime(time: String) {
        wakeUpTime.value = time
    }

    fun setDropdownExpanded(value: Boolean) {
        _dropdownExpanded.value = value
    }

    fun load(userId: Long) {
        currentUserId = userId
        viewModelScope.launch {
            repository.getFoodIntakeByUserId(userId)?.let { intake ->
                foodIntakeId = intake.id
                selectedPersona.value = intake.persona
                biggestMealTime.value = intake.biggestMealTime
                sleepTime.value = intake.sleepTime
                wakeUpTime.value = intake.wakeUpTime

                checkedState[0] = intake.fruits
                checkedState[1] = intake.vegetables
                checkedState[2] = intake.grains
                checkedState[3] = intake.redMeat
                checkedState[4] = intake.seafood
                checkedState[5] = intake.poultry
                checkedState[6] = intake.fish
                checkedState[7] = intake.eggs
                checkedState[8] = intake.nutsSeeds
            }
        }
    }

    fun save() {
        currentUserId?.let { userId ->
            viewModelScope.launch {
                val entity = FoodIntake(
                    id = foodIntakeId ?: 0L,
                    patientUserId = userId,
                    persona = selectedPersona.value,
                    biggestMealTime = biggestMealTime.value,
                    sleepTime = sleepTime.value,
                    wakeUpTime = wakeUpTime.value,
                    fruits = checkedState[0],
                    vegetables = checkedState[1],
                    grains = checkedState[2],
                    redMeat = checkedState[3],
                    seafood = checkedState[4],
                    poultry = checkedState[5],
                    fish = checkedState[6],
                    eggs = checkedState[7],
                    nutsSeeds = checkedState[8]
                )
                repository.insertFoodIntake(entity)
            }
        }
    }
}
