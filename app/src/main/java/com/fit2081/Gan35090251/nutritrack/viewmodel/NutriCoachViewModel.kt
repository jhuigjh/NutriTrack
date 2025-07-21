package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.AppDatabase
import com.fit2081.Gan35090251.nutritrack.data.entity.*
import com.fit2081.Gan35090251.nutritrack.data.network.FruityViceApiService
import com.fit2081.Gan35090251.nutritrack.data.network.GeminiApiService
import com.fit2081.Gan35090251.nutritrack.repository.NutriCoachRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NutriCoachViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NutriCoachRepository
    private val fruityApi: FruityViceApiService

    private val _fruitName = MutableStateFlow("")
    val fruitName: StateFlow<String> = _fruitName

    private val _fruitDetails = MutableStateFlow<FruitInfo?>(null)
    val fruitDetails: StateFlow<FruitInfo?> = _fruitDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _genaiLoading = MutableStateFlow(false)
    val genaiLoading: StateFlow<Boolean> = _genaiLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _genaiError = MutableStateFlow<String?>(null)
    val genaiError: StateFlow<String?> = _genaiError

    private val _motivationalMessage = MutableStateFlow<String?>(null)
    val motivationalMessage: StateFlow<String?> = _motivationalMessage

    private val _allMessages = MutableStateFlow<List<NutriCoach>>(emptyList())
    val allMessages: StateFlow<List<NutriCoach>> = _allMessages

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    val currentPatient: StateFlow<Patient?> = _currentPatient

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    init {
        val database = AppDatabase.getDatabase(application)
        fruityApi = Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruityViceApiService::class.java)

        val geminiApi = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)

        val apiKey = "AIzaSyDaw8VjUUAGyhW3g1h0mS_RNba6WM5vtVY"
        repository = NutriCoachRepository(
            nutriCoachDao = database.nutriCoachDao(),
            patientDao = database.patientDao(),
            geminiApi = geminiApi,
            apiKey = apiKey
        )
    }

    fun toggleDialogVisibility(show: Boolean) {
        _showDialog.value = show
    }

    fun updateFruitName(name: String) {
        _fruitName.value = name
    }

    fun loadPatient(userId: Long) {
        viewModelScope.launch {
            val patient = repository.getPatientByUserId(userId)
            _currentPatient.value = patient
            if (patient != null) {
                loadAllMessages(patient.userId)
            }
        }
    }

    fun searchFruitDetails() {
        val name = fruitName.value.trim().lowercase()
        if (name.isBlank()) {
            _error.value = "Fruit name cannot be empty"
            return
        }

        viewModelScope.launch {
            handleLoading(_isLoading, _error) {
                _fruitDetails.value = fruityApi.getFruitInfoByName(name)
            }
        }
    }

    fun generateAndSaveMotivationalMessage(patient: Patient) {
        viewModelScope.launch {
            handleLoading(_genaiLoading, _genaiError) {
                val prompt = buildPrompt(patient)
                val message = repository.generateMotivationalMessageAndSave(patient, prompt)
                _motivationalMessage.value = message
                loadAllMessages(patient.userId)
            }
        }
    }

    fun loadAllMessages(userId: Long) {
        viewModelScope.launch {
            repository.getMessagesByUser(userId).collect {
                _allMessages.value = it
            }
        }
    }

    private suspend fun <T> handleLoading(
        loadingState: MutableStateFlow<Boolean>,
        errorState: MutableStateFlow<String?>,
        block: suspend () -> T
    ): T? {
        return try {
            loadingState.value = true
            errorState.value = null
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            errorState.value = e.message ?: "An error occurred"
            null
        } finally {
            loadingState.value = false
        }
    }


    private fun buildPrompt(patient: Patient): String {
        return with(patient) {
            """
            Below is a comprehensive nutrition and dietary profile for a patient.

            Patient ID: $userId
            Phone Number: $phoneNumber
            Sex: $sex

            Fruit HEIFA Score: Male=$fruitHeifaScoreMale, Female=$fruitHeifaScoreFemale
            Fruit Serve Size: $fruitServeSize
            Fruit Variations: Score=$fruitVariationsScore, Pome=$fruitPome, Tropical=$fruitTropicalAndSubtropical, Berry=$fruitBerry, Stone=$fruitStone, Citrus=$fruitCitrus, Other=$fruitOther

            Vegetable HEIFA Score: Male=$vegetablesHeifaScoreMale, Female=$vegetablesHeifaScoreFemale
            Vegetable Types: Green=$vegetablesGreen, Red/Orange=$vegetablesRedAndOrange, Cruciferous=$vegetablesCruciferous, Tuber/Bulb=$vegetablesTuberAndBulb, Other=$vegetablesOther, Legumes=$legumes

            Grains and Cereals: HEIFA Male=$grainsAndCerealsHeifaScoreMale, Female=$grainsAndCerealsHeifaScoreFemale, Serve Size=$grainsAndCerealsServeSize, Non-Whole Grains=$grainsAndCerealsNonWholeGrains

            Whole Grains: HEIFA Male=$wholeGrainsHeifaScoreMale, Female=$wholeGrainsHeifaScoreFemale, Serve Size=$wholeGrainsServeSize

            Meat/Alternatives: HEIFA Male=$meatAndAlternativesHeifaScoreMale, Female=$meatAndAlternativesHeifaScoreFemale, Serve Size=$meatAndAlternativesWithLegumesAllocatedServeSize
            Dairy/Alternatives: HEIFA Male=$dairyAndAlternativesHeifaScoreMale, Female=$dairyAndAlternativesHeifaScoreFemale, Serve Size=$dairyAndAlternativesServeSize

            Discretionary: HEIFA Male=$discretionaryHeifaScoreMale, Female=$discretionaryHeifaScoreFemale, Serve Size=$discretionaryServeSize

            Sodium: HEIFA Male=$sodiumHeifaScoreMale, Female=$sodiumHeifaScoreFemale, Mg=$sodiumMgMilligrams
            Alcohol: HEIFA Male=$alcoholHeifaScoreMale, Female=$alcoholHeifaScoreFemale, Drinks=$alcoholStandardDrinks

            Water: HEIFA Male=$waterHeifaScoreMale, Female=$waterHeifaScoreFemale, Water ml=$waterTotalMl, Beverages ml=$beverageTotalMl
            Sugar: HEIFA Male=$sugarHeifaScoreMale, Female=$sugarHeifaScoreFemale, Intake=$sugar
            Fats: Saturated Fat HEIFA Male=$saturatedFatHeifaScoreMale, Female=$saturatedFatHeifaScoreFemale, Intake=$saturatedFat
            Fats: Unsaturated Fat HEIFA Male=$unsaturatedFatHeifaScoreMale, Female=$unsaturatedFatHeifaScoreFemale, Serve Size=$unsaturatedFatServeSize

            Generate a short, personalized motivational message that encourages the user to improve their diet quality—especially fruit intake. (no intro)
            """.trimIndent()
        }
    }
}
