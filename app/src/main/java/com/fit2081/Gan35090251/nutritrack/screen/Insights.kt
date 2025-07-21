package com.fit2081.Gan35090251.nutritrack.screen

import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fit2081.Gan35090251.nutritrack.viewmodel.InsightsViewModel

@Composable
fun InsightsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: InsightsViewModel = viewModel()
)
 {
    val context = LocalContext.current

    val insights by viewModel.insightsData.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Insights: Food Score",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            insights.forEach { (label, value) ->
                val max = when (label) {
                    "Discretionary", "Sodium", "Added Sugars" -> 10f
                    "Vegetables", "Fruits", "Water", "Unsaturated Fat", "Alcohol" -> 5f
                    "Grains & Cereals" -> 5f
                    "Meat & Alternatives", "Dairy & Alternatives" -> 10f
                    else -> 10f
                }
                StaticSliderRow(label, value, max)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Total Food Quality Score ${"%.2f".format(totalScore)}/100.00",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Slider(
                value = totalScore,
                onValueChange = {},
                enabled = false,
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val shareText = "Total Food Quality Score: ${"%.2f".format(totalScore)}/100.00"
                        val shareIntent = Intent(ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                    Text("Share", fontSize = 16.sp, modifier = Modifier.padding(start = 6.dp))
                }

                Button(
                    onClick = {
                        navController.navigate("NutriCoach") },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Improve", modifier = Modifier.size(16.dp))
                    Text("Improve my diet!", fontSize = 16.sp, modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    }
}

@Composable
fun StaticSliderRow(label: String, value: Float, maxValue: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: %.2f / %.2f".format(value, maxValue),
            modifier = Modifier.width(200.dp),
            fontWeight = FontWeight.Bold,
        )
        Slider(
            value = value,
            onValueChange = {},
            valueRange = 0f..maxValue,
            enabled = false,
            modifier = Modifier.weight(1f)
        )
    }
}
