package com.fit2081.Gan35090251.nutritrack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Gan35090251.nutritrack.viewmodel.PersonaViewModel

data class Persona(val name: String, val imageRes: Int, val des: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonaSelectionScreen(
    personaNames: List<String>,
    viewModel: PersonaViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setPersonaNames(personaNames)
    }

    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3
        ) {
            viewModel.personas.forEach { persona ->
                Button(
                    onClick = { viewModel.onPersonaSelected(persona) },
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
                ) {
                    Text(text = persona.name, fontSize = 10.sp)
                }
            }
        }

        viewModel.selectedPersona?.let { persona ->
            PersonaModal(persona = persona, onDismiss = viewModel::dismissModal)
        }
    }
}

@Composable
fun PersonaModal(persona: Persona, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = persona.imageRes),
                    contentDescription = "Persona Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = persona.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = persona.des, fontSize = 12.sp)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}
