package com.fit2081.Gan35090251.nutritrack.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fit2081.Gan35090251.nutritrack.viewmodel.NutriCoachViewModel

@Composable
fun NutriCoachScreen(
    modifier: Modifier = Modifier,
    viewModel: NutriCoachViewModel = viewModel()
) {
    val context = LocalContext.current

    val fruitName by viewModel.fruitName.collectAsState()
    val fruitDetails by viewModel.fruitDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val genaiLoading by viewModel.genaiLoading.collectAsState()
    val fruitError by viewModel.error.collectAsState()
    val genaiError by viewModel.genaiError.collectAsState()
    val allMessages by viewModel.allMessages.collectAsState()
    val motivationalMessage by viewModel.motivationalMessage.collectAsState()
    val patient by viewModel.currentPatient.collectAsState()

    val showDialog by viewModel.showDialog.collectAsState()

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("current_user_id", null)?.toLongOrNull()
        userId?.let { viewModel.loadPatient(it)
            viewModel.loadAllMessages(it)
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NutriCoach",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ----- CONDITIONAL FRUIT SECTION -----
                val fruitScore = remember(patient) {
                    if (patient?.sex?.equals("Male", true) == true) {
                        patient?.fruitHeifaScoreMale ?: 0.0
                    } else {
                        patient?.fruitHeifaScoreFemale ?: 0.0
                    }
                }

                if (fruitScore < 10.0) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = fruitName,
                            onValueChange = viewModel::updateFruitName,
                            label = { Text("Fruit Name") },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = viewModel::searchFruitDetails,
                            enabled = fruitName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Details", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Details")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (fruitError != null) {
                        Text(fruitError ?: "", color = MaterialTheme.colorScheme.error)
                    }

                    NutrientRow("Family", fruitDetails?.family ?: "None")
                    NutrientRow("Calories", fruitDetails?.nutritions?.calories?.toString() ?: "None")
                    NutrientRow("Fat", fruitDetails?.nutritions?.fat?.toString() ?: "None")
                    NutrientRow("Sugar", fruitDetails?.nutritions?.sugar?.toString() ?: "None")
                    NutrientRow("Carbohydrates", fruitDetails?.nutritions?.carbohydrates?.toString() ?: "None")
                    NutrientRow("Protein", fruitDetails?.nutritions?.protein?.toString() ?: "None")
                } else {
                    Text("Great job! Your fruit intake is optimal.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    AsyncImage(
                        model = "https://picsum.photos/400",
                        contentDescription = "Random motivational image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                // ----- MOTIVATIONAL MESSAGE SECTION -----
                Text(
                    text = "GenAI Motivation",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { patient?.let(viewModel::generateAndSaveMotivationalMessage) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = patient != null
                ) {
                    Text("Generate Motivational Tip")
                }

                motivationalMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp, max = 200.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(12.dp)
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                if (genaiLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }

                genaiError?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.toggleDialogVisibility(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show All Tips")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.toggleDialogVisibility(false) },
                    title = { Text("Saved Motivational Tips") },
                    text = {
                        if (allMessages.isEmpty()) {
                            Text("No tips saved yet.")
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                                items(allMessages) { tip ->
                                    Text("• ${tip.message}", modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.toggleDialogVisibility(false) }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NutrientRow(label: String, value: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(": $value", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
