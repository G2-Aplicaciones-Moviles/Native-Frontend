package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToMealPlanScreen(
    mealPlanId: Long,
    onRecipeAdded: () -> Unit,
    onCancel: () -> Unit,
    viewModel: MealPlanViewModel = viewModel()
) {
    var recipeId by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = recipeId,
                onValueChange = { recipeId = it },
                label = { Text("ID de receta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo (ej. Desayuno, Almuerzo...)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text("Día (número)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (recipeId.isNotEmpty() && type.isNotEmpty() && day.isNotEmpty()) {
                        isLoading = true
                        viewModel.addRecipeToMealPlan(
                            mealPlanId = mealPlanId,
                            recipeId = recipeId.toInt(),
                            type = type,
                            day = day.toInt(),
                            onSuccess = {
                                isLoading = false
                                onRecipeAdded()
                            },
                            onError = {
                                isLoading = false
                            }
                        )
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Agregando..." else "Agregar receta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

