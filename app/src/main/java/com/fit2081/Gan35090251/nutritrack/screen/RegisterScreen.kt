package com.fit2081.Gan35090251.nutritrack.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fit2081.Gan35090251.nutritrack.viewmodel.RegisterViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fit2081.Gan35090251.nutritrack.ui.theme.NutriTrackTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriTrackTheme {
                val registerViewModel: RegisterViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = registerViewModel,
                        onRegistered = { finish() },
                        onBackToLogin = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    onRegistered: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Register Account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = viewModel.isDropdownExpanded,
                onExpandedChange = { viewModel.toggleDropdownExpanded() }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedUserId,
                    onValueChange = { viewModel.updateUserId(it) },
                    readOnly = true,
                    placeholder = { Text("Select your ID") },
                    label = { Text("My ID (Provided by your Clinician)") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = viewModel.isDropdownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = viewModel.isDropdownExpanded,
                    onDismissRequest = { viewModel.dismissDropdown() }
                ) {
                    viewModel.userIdList.forEach { userId ->
                        DropdownMenuItem(
                            text = { Text(userId) },
                            onClick = {
                                viewModel.updateUserId(userId)
                                viewModel.dismissDropdown()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.phoneNumber,
                onValueChange = { input ->
                    viewModel.updatePhoneNumber(input.filter { it.isDigit() })
                },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter your phone number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Password") },
                placeholder = { Text("Enter password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        viewModel.selectedUserId.isBlank() ||
                                viewModel.phoneNumber.isBlank() ||
                                viewModel.password.isBlank() ||
                                viewModel.confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.password != viewModel.confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.isUserAlreadyRegistered() -> {
                            Toast.makeText(context, "This user is already registered", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.registerPatient(
                                onRegistered = {
                                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_LONG).show()
                                    onRegistered()
                                },
                                onFailure = {
                                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Submit", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                ) {
                Text("Login", fontWeight = FontWeight.Bold)
            }

        }
    }
}
