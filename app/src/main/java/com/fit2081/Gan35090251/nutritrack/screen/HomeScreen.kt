package com.fit2081.Gan35090251.nutritrack.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.fit2081.Gan35090251.nutritrack.R
import com.fit2081.Gan35090251.nutritrack.ui.theme.NutriTrackTheme
import com.fit2081.Gan35090251.nutritrack.viewmodel.HomeViewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.LoginViewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.NutriCoachViewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.SettingsViewModel


class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriTrackTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomAppBar(navController) }
                ) { innerPadding ->
                    MyNavHost(innerPadding, navController)
                }
            }
        }
    }
}

@Composable
fun MyNavHost(innerPadding: PaddingValues, navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("Home") {
            HomeScreenUI(modifier = Modifier.padding(innerPadding), viewModel)
        }
        composable("Insights") {
            InsightsScreen(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )

        }
        composable("NutriCoach") {
            NutriCoachScreen(modifier = Modifier.padding(innerPadding))
        }
        composable("Settings") {
            SettingsScreen(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
        composable("ClinicianLogin") {
            ClinicianLoginScreen(navController)
        }
        composable("ClinicianDashboard") {
            ClinicianDashboardScreen()
        }
        composable("ChangePassword") {
            ChangePasswordScreen()
        }
    }
}

@Composable
fun MyBottomAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val items = listOf("Home", "Insights", "NutriCoach", "Settings")

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insights" -> Icon(Icons.Filled.Favorite, contentDescription = "Insights")
                        "NutriCoach" -> Icon(Icons.Filled.Face, contentDescription = "NutriCoach")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                label = { Text(item) },
                selected = currentDestination == item,
                onClick = {
                    navController.navigate(item) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun HomeScreenUI(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUserData(context)
    }

    val userName by viewModel.userName.collectAsState()
    val score by viewModel.score.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(text = "Hello,")

            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "You've already filled in your Food Intake Questionnaire, but you can change details here",
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        context.startActivity(Intent(context, Questionnaire::class.java))
                    },
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit", fontSize = 16.sp)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.foodplate),
                contentDescription = "Food Plate",
                modifier = Modifier
                    .size(250.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "My Score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Food Quality Score: $score",
                fontSize = 24.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What is the Food Quality Score?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with " +
                        "established food guidelines, helping you identify both strengths and opportunities for " +
                        "improvement in your diet.\n\n" +
                        "This personalized measurement considers various food groups including vegetables, fruits, " +
                        "whole grains, and proteins to give you practical insights for making healthier food choices.",
                fontSize = 14.sp,
            )
        }
    }
}
