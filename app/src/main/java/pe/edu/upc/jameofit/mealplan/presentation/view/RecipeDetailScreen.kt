package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upc.jameofit.mealplan.data.model.AddRecipeRequest
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.ui.theme.JameoBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    mealPlanId: Long,
    viewModel: MealPlanViewModel,
    onBack: () -> Unit
) {
    val recipe by viewModel.currentRecipe.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(1) }

    LaunchedEffect(recipeId) {
        viewModel.loadRecipeById(recipeId)
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.clearCurrentRecipe() }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (recipe == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Recipe not found")
                Spacer(Modifier.height(8.dp))
                Button(onClick = onBack) { Text("Go Back") }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    recipe!!.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )

                Spacer(Modifier.height(6.dp))
                Text(recipe!!.description)

                Spacer(Modifier.height(14.dp))

                InfoRow("Preparation Time", "${recipe!!.preparationTime} min")
                InfoRow("Difficulty", recipe!!.difficulty)
                InfoRow("Category", recipe!!.category)
                InfoRow("Recipe Type", recipe!!.recipeType)

                Spacer(Modifier.height(14.dp))

                Text("Ingredients", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))

                recipe!!.recipeIngredients?.forEach { ing ->
                    Text("- ${ing.ingredient.name}: ${ing.amountGrams}g")
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Spacer(Modifier.height(20.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoBlue,
                    contentColor = Color.White
                )
            ) { Text("Agregar") }

            Spacer(Modifier.width(16.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoBlue,
                    contentColor = Color.White
                )
            ) { Text("Cancelar") }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add to Meal Plan") },
            text = {
                Column {

                    var expandedType by remember { mutableStateOf(false) }
                    val typeOptions = listOf("Breakfast", "Lunch", "Snack")

                    ExposedDropdownMenuBox(
                        expanded = expandedType,
                        onExpandedChange = { expandedType = !expandedType }
                    ) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Type") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedType,
                            onDismissRequest = { expandedType = false }
                        ) {
                            typeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedType = option
                                        expandedType = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))


                    var expandedDay by remember { mutableStateOf(false) }
                    val dayOptions = (1..7).toList()

                    ExposedDropdownMenuBox(
                        expanded = expandedDay,
                        onExpandedChange = { expandedDay = !expandedDay }
                    ) {
                        OutlinedTextField(
                            value = selectedDay.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Day") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedDay,
                            onDismissRequest = { expandedDay = false }
                        ) {
                            dayOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.toString()) },
                                    onClick = {
                                        selectedDay = option
                                        expandedDay = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (selectedType.isBlank()) return@Button

                    val request = AddRecipeRequest(
                        recipeId = recipeId,
                        type = selectedType,
                        day = selectedDay
                    )

                    viewModel.addRecipeToMealPlan(mealPlanId, request)

                    showDialog = false
                    onBack()
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}

