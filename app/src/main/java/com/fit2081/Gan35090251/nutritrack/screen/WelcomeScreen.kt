package com.fit2081.Gan35090251.nutritrack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.Gan35090251.nutritrack.R

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(R.drawable.nutritrack_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "This app provides general health and nutrition information for educational purposes only. " +
                        "It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified " +
                        "healthcare professional before making any changes to your diet, exercise, or health regimen. " +
                        "Use this app at your own risk. Visit: https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginScreen()

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Designed by Gan Jia Hui (35090251)",
                fontSize = 18.sp,
            )
        }
    }
}
