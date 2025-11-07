package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

@Composable
fun MealPlanDetailScreen(
    mealPlanId: Long,
    viewModel: MealPlanViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val mealPlan by viewModel.selectedMealPlan.collectAsState()
    val entries by viewModel.entries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(mealPlanId) {
        viewModel.loadMealPlanById(mealPlanId)
        viewModel.loadEntries(mealPlanId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )

            mealPlan == null -> Text(
                text = "Meal plan no encontrado.",
                modifier = Modifier.align(Alignment.Center)
            )

            else -> mealPlan?.let { plan ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = plan.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = plan.description)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Categoría: ${plan.category}")
                        Text(text = "Calorías: ${plan.calories}")
                        Text(text = "Carbohidratos: ${plan.carbs}")
                        Text(text = "Proteínas: ${plan.proteins}")
                        Text(text = "Grasas: ${plan.fats}")
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Recetas",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (entries.isEmpty()) {
                        item {
                            Text(
                                text = "Este meal plan aún no tiene recetas.",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        items(entries) { entry ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = entry.recipeName ?: "Receta sin nombre",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(text = "ID de receta: ${entry.recipeId}")
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("drawer/add_recipe_to_meal_plan/$mealPlanId")
                                }
                            ) {
                                Text("Agregar Receta")
                            }

                            Button(
                                onClick = {
                                    viewModel.deleteMealPlan(mealPlanId)
                                    navController.popBackStack()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
