package com.fit2081.Gan35090251.nutritrack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.ClinicianDashboardViewModel

@Composable
fun ClinicianDashboardScreen(
    viewModel: ClinicianDashboardViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val maleAverage = viewModel.maleAverage
    val femaleAverage = viewModel.femaleAverage
    val insights = viewModel.insights
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Clinician Dashboard",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Male HEIFA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Average HEIFA (Male):", modifier = Modifier.weight(1f))
                    Text(maleAverage?.let { "%.1f".format(it) } ?: "Loading...")
                }
            }

            // Female HEIFA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Average HEIFA (Female):", modifier = Modifier.weight(1f))
                    Text(femaleAverage?.let { "%.1f".format(it) } ?: "Loading...")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Analyze Button
            Button(
                onClick = { viewModel.analyzeDataPatterns() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Find Data Pattern", color = Color.White)
            }

            if (error != null) {
                Text("Error: $error", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            }

            insights.forEach { insight ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = insight,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }
    }
}
