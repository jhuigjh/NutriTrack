package com.fit2081.Gan35090251.nutritrack.screen

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.QuestionnaireViewModel
import com.fit2081.Gan35090251.nutritrack.ui.theme.NutriTrackTheme
import java.util.*

class Questionnaire : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodIntakeQuestionnaire(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = {
                            startActivity(Intent(this@Questionnaire, MainActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoodIntakeQuestionnaire(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: QuestionnaireViewModel = viewModel()
) {
    val context = LocalContext.current

    val checkedState by remember { derivedStateOf { viewModel.checkedState } }
    val selectedPersona by viewModel.selectedPersona
    val biggestMealTime by viewModel.biggestMealTime
    val sleepTime by viewModel.sleepTime
    val wakeUpTime by viewModel.wakeUpTime
    val expanded by viewModel.dropdownExpanded.collectAsState()

    val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
    val currentUserId = prefs.getString("current_user_id", null)?.toLongOrNull()

    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            viewModel.load(it)
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "Food Intake Questionnaire",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Tick all the food categories you can eat", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            FlowRow(modifier = Modifier.fillMaxWidth(), maxItemsInEachRow = 3) {
                viewModel.foodItems.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(125.dp)
                    ) {
                        Checkbox(
                            checked = checkedState.getOrElse(index) { false },
                            onCheckedChange = { viewModel.setChecked(index, it) }
                        )
                        Text(item, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Your Persona", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "People can be broadly classified into 6 different types based on their eating preferences. " +
                        "Click on each button below to find out the different types, and select the type that best fits you!"
            )

            Spacer(modifier = Modifier.height(4.dp))
            PersonaSelectionScreen(viewModel.personaOptions)

            Spacer(modifier = Modifier.height(12.dp))
            Text("Which persona best fits you?", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { viewModel.setDropdownExpanded(!expanded) }
            ) {
                TextField(
                    value = selectedPersona,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Your Persona") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    viewModel.setDropdownExpanded(false)
                }) {
                    viewModel.personaOptions.forEach { persona ->
                        DropdownMenuItem(
                            text = { Text(persona) },
                            onClick = {
                                viewModel.setSelectedPersona(persona)
                                viewModel.setDropdownExpanded(false)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Timings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TimeQuestionRow("What time of day approx, do you normally eat your biggest meal?", biggestMealTime) {
                viewModel.setBiggestMealTime(it)
            }
            TimeQuestionRow("What time of day approx, do you go to sleep at night?", sleepTime) {
                viewModel.setSleepTime(it)
            }
            TimeQuestionRow("What time of day approx, do you wake up in the morning?", wakeUpTime) {
                viewModel.setWakeUpTime(it)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                currentUserId?.let {
                    viewModel.save()
                    Toast.makeText(context, "Save Successful", Toast.LENGTH_LONG).show()
                    (context as? ComponentActivity)?.startActivity(Intent(context, HomeScreen::class.java))
                } ?: Toast.makeText(context, "Please log in to save your preferences.", Toast.LENGTH_LONG).show()
            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun TimeQuestionRow(questionText: String, time: String, onTimeSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(questionText, fontSize = 12.sp, modifier = Modifier.weight(1f))
        TimePickerButton(time, onTimeSelected)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun TimePickerButton(time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = remember {
        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val amPm = if (selectedHour >= 12) "PM" else "AM"
            val hour12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            onTimeSelected(String.format("%02d:%02d %s", hour12, selectedMinute, amPm))
        }, hour, minute, false)
    }

    OutlinedButton(onClick = { timePickerDialog.show() }) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
            contentDescription = "Clock Icon"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = time)
    }
}
