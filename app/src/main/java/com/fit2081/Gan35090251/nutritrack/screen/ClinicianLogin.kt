package com.fit2081.Gan35090251.nutritrack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fit2081.Gan35090251.nutritrack.viewmodel.ClinicianLoginViewModel

@Composable
fun ClinicianLoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ClinicianLoginViewModel = viewModel()
    ) {
    Surface(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .fillMaxWidth()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Clinician Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = viewModel.clinicianKey.value,
                onValueChange = { viewModel.onKeyChange(it) },
                placeholder = { Text("Clinician Key") },
                label = { Text("Enter your clinician key") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )

            if (viewModel.loginError.value.isNotEmpty()) {
                Text(viewModel.loginError.value, color = Color.Red)
            }

            Button(
                onClick = {
                    viewModel.validateKey {
                        navController.navigate("ClinicianDashboard")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Clinician Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}
