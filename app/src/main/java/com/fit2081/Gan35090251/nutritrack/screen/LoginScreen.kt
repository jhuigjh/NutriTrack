package com.fit2081.Gan35090251.nutritrack.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(),
) {
    val isVisible = viewModel.isBottomSheetVisible
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.toggleBottomSheet() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (isVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { viewModel.toggleBottomSheet() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Login",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = viewModel.isDropdownExpanded,
                        onExpandedChange = {
                            viewModel.updateDropdownExpanded(!viewModel.isDropdownExpanded)
                        }
                    ) {
                        OutlinedTextField(
                            value = viewModel.selectedOption,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select your ID") },
                            label = { Text("My ID (Provided by your Clinician)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.isDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(horizontal = 8.dp),
                            )

                        ExposedDropdownMenu(
                            expanded = viewModel.isDropdownExpanded,
                            onDismissRequest = {
                                viewModel.updateDropdownExpanded(false)
                            }
                        ) {
                            viewModel.userIdList.forEach { userId ->
                                DropdownMenuItem(
                                    text = { Text(userId) },
                                    onClick = {
                                        viewModel.updateSelectedOption(userId)
                                        viewModel.updateDropdownExpanded(false)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.validateLogin(
                                context = context,
                                onSuccess = {
                                    viewModel.viewModelScope.launch {
                                        val userId = viewModel.selectedOption.toLongOrNull()
                                        if (userId != null) {
                                            val completed = viewModel.hasCompletedQuestionnaire(userId)
                                            val intent = if (completed) {
                                                Intent(context, HomeScreen::class.java)
                                            } else {
                                                Intent(context, Questionnaire::class.java)
                                            }
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            context.startActivity(intent)
                                        } else {
                                            Toast.makeText(context, "Please select a valid ID.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onFailure = {
                                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                                },
                                onUnregistered = {
                                    Toast.makeText(context, "This ID hasn't been registered yet. Please register first.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, RegisterActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
