package com.fit2081.Gan35090251.nutritrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Gan35090251.nutritrack.data.AppDatabase
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient
import com.fit2081.Gan35090251.nutritrack.data.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val patientRepository = PatientRepository(context)

    fun seedDatabaseIfEmpty() {
        viewModelScope.launch {
            val existingPatients = patientRepository.getAllPatients()
            if (existingPatients.isEmpty()) {
                val patients = parseCsvToPatients("data.csv")
                patientRepository.insertAllPatients(patients)
            }
        }
    }

    private suspend fun parseCsvToPatients(fileName: String): List<Patient> {
        return withContext(Dispatchers.IO) {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val values = line.split(",").map { it.trim() }
                    try {
                        Patient(
                            userId = values[1].toLong(),
                            phoneNumber = values[0],
                            password = "",
                            sex = values[2],
                            heifaTotalScoreMale = values[3].toDouble(),
                            heifaTotalScoreFemale = values[4].toDouble(),
                            discretionaryHeifaScoreMale = values[5].toDouble(),
                            discretionaryHeifaScoreFemale = values[6].toDouble(),
                            discretionaryServeSize = values[7].toDouble(),
                            vegetablesHeifaScoreMale = values[8].toDouble(),
                            vegetablesHeifaScoreFemale = values[9].toDouble(),
                            vegetablesWithLegumesAllocatedServeSize = values[10].toDouble(),
                            legumesAllocatedVegetables = values[11].toDouble(),
                            vegetablesVariationsScore = values[12].toDouble(),
                            vegetablesCruciferous = values[13].toDouble(),
                            vegetablesTuberAndBulb = values[14].toDouble(),
                            vegetablesOther = values[15].toDouble(),
                            legumes = values[16].toDouble(),
                            vegetablesGreen = values[17].toDouble(),
                            vegetablesRedAndOrange = values[18].toDouble(),
                            fruitHeifaScoreMale = values[19].toDouble(),
                            fruitHeifaScoreFemale = values[20].toDouble(),
                            fruitServeSize = values[21].toDouble(),
                            fruitVariationsScore = values[22].toDouble(),
                            fruitPome = values[23].toDouble(),
                            fruitTropicalAndSubtropical = values[24].toDouble(),
                            fruitBerry = values[25].toDouble(),
                            fruitStone = values[26].toDouble(),
                            fruitCitrus = values[27].toDouble(),
                            fruitOther = values[28].toDouble(),
                            grainsAndCerealsHeifaScoreMale = values[29].toDouble(),
                            grainsAndCerealsHeifaScoreFemale = values[30].toDouble(),
                            grainsAndCerealsServeSize = values[31].toDouble(),
                            grainsAndCerealsNonWholeGrains = values[32].toDouble(),
                            wholeGrainsHeifaScoreMale = values[33].toDouble(),
                            wholeGrainsHeifaScoreFemale = values[34].toDouble(),
                            wholeGrainsServeSize = values[35].toDouble(),
                            meatAndAlternativesHeifaScoreMale = values[36].toDouble(),
                            meatAndAlternativesHeifaScoreFemale = values[37].toDouble(),
                            meatAndAlternativesWithLegumesAllocatedServeSize = values[38].toDouble(),
                            legumesAllocatedMeatAndAlternatives = values[39].toDouble(),
                            dairyAndAlternativesHeifaScoreMale = values[40].toDouble(),
                            dairyAndAlternativesHeifaScoreFemale = values[41].toDouble(),
                            dairyAndAlternativesServeSize = values[42].toDouble(),
                            sodiumHeifaScoreMale = values[43].toDouble(),
                            sodiumHeifaScoreFemale = values[44].toDouble(),
                            sodiumMgMilligrams = values[45].toDouble(),
                            alcoholHeifaScoreMale = values[46].toDouble(),
                            alcoholHeifaScoreFemale = values[47].toDouble(),
                            alcoholStandardDrinks = values[48].toDouble(),
                            waterHeifaScoreMale = values[49].toDouble(),
                            waterHeifaScoreFemale = values[50].toDouble(),
                            water = values[51].toDouble(),
                            waterTotalMl = values[52].toDouble(),
                            beverageTotalMl = values[53].toDouble(),
                            sugarHeifaScoreMale = values[54].toDouble(),
                            sugarHeifaScoreFemale = values[55].toDouble(),
                            sugar = values[56].toDouble(),
                            saturatedFatHeifaScoreMale = values[57].toDouble(),
                            saturatedFatHeifaScoreFemale = values[58].toDouble(),
                            saturatedFat = values[59].toDouble(),
                            unsaturatedFatHeifaScoreMale = values[60].toDouble(),
                            unsaturatedFatHeifaScoreFemale = values[61].toDouble(),
                            unsaturatedFatServeSize = values[62].toDouble()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }.toList()
            }
        }
    }
}
