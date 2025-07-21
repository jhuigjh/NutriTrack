package com.fit2081.Gan35090251.nutritrack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = viewModel()
) {
    val currentPassword = viewModel.currentPassword.collectAsState()
    val newPassword = viewModel.newPassword.collectAsState()
    val confirmPassword = viewModel.confirmPassword.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val successMessage = viewModel.successMessage.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Change Password", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = currentPassword.value,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword.value,
                onValueChange = viewModel::onNewPasswordChange,
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.changePassword() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Password")
            }

            if (errorMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(errorMessage.value, color = MaterialTheme.colorScheme.error)
            }

            if (successMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(successMessage.value, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
