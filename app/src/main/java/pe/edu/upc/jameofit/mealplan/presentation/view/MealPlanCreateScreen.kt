package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

@Composable
fun MealPlanCreateScreen(
    viewModel: MealPlanViewModel = viewModel(),
    onMealPlanCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()


    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isCurrent by remember { mutableStateOf(true) }
    var tagsText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Crear Plan Alimenticio",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del plan") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría (Ej. Definición, Mantenimiento)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tagsText,
            onValueChange = { tagsText = it },
            label = { Text("Tags (separa con comas)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Checkbox(
                checked = isCurrent,
                onCheckedChange = { isCurrent = it }
            )
            Text("Marcar como plan actual")
        }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            viewModel.createMealPlan(
                                name = name,
                                description = description,
                                calories = 0.1,
                                carbs = 0.1,
                                proteins = 0.1,
                                fats = 0.1,
                                profileId = 1,
                                category = category,
                                isCurrent = isCurrent,
                                tags = tagsText.split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }
                            )
                            onMealPlanCreated()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Crear Plan", style = MaterialTheme.typography.titleMedium)
                }
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}

