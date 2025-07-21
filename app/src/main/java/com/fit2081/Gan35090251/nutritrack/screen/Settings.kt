package com.fit2081.Gan35090251.nutritrack.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fit2081.Gan35090251.nutritrack.viewmodel.ClinicianDashboardViewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel= viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val patient by viewModel.patient.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "ACCOUNT",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            patient?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "User ID")
                    Spacer(modifier = Modifier.width(18.dp))
                    Text("User ID: ${it.userId}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = "Phone")
                    Spacer(modifier = Modifier.width(18.dp))
                    Text("Phone Number: ${it.phoneNumber}")
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                "OTHER SETTINGS",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Go")
            }

            TextButton(
                onClick = {
                    navController.navigate("ClinicianLogin")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.AccountBox, contentDescription = "Clinician")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clinician Login", modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Go")
            }

            TextButton(
                onClick = {
                    navController.navigate("ChangePassword")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = "Change Password")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change Password", modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Go")
            }
        }
    }
}
