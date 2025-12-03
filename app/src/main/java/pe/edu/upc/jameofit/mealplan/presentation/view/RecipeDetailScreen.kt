package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.mealplan.data.model.AddRecipeRequest
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

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

    LaunchedEffect(recipeId) { viewModel.loadRecipeById(recipeId) }

    DisposableEffect(Unit) {
        onDispose { viewModel.clearCurrentRecipe() }
    }

    // -------------------------------
    // LOADING
    // -------------------------------
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = JameoGreen) }
        return
    }

    // -------------------------------
    // NOT FOUND STATE
    // -------------------------------
    if (recipe == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Receta no encontrada")
                Spacer(Modifier.height(8.dp))
                Button(onClick = onBack) { Text("Volver") }
            }
        }
        return
    }

    val isAddingMode = mealPlanId != 0L

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                JameoGreen,
                                JameoGreen.copy(alpha = 0.85f),
                                JameoGreen.copy(alpha = 0.65f),
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        "Detalle de Receta",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // -------------------------------
            // CARD PRINCIPAL GLASS
            // -------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.95f))
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        recipe!!.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = JameoGreen
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(recipe!!.description, color = Color.Gray)

                    Spacer(Modifier.height(14.dp))

                    InfoRowPremium("Tiempo de preparación", "${recipe!!.preparationTime} min")
                    InfoRowPremium("Dificultad", recipe!!.difficulty)
                    InfoRowPremium("Categoría", recipe!!.category)
                    InfoRowPremium("Tipo de Receta", recipe!!.recipeType)

                    Spacer(Modifier.height(18.dp))
                    Text(
                        "Ingredientes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = JameoGreen
                    )

                    Spacer(Modifier.height(6.dp))

                    recipe!!.recipeIngredients.forEach { ing ->
                        Text("- ${ing.ingredient.name}: ${ing.amountGrams}g")
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }

            // -------------------------------
            // BOTONES
            // -------------------------------
            if (isAddingMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JameoGreen,
                            contentColor = Color.White
                        )
                    ) { Text("Agregar") }

                    Spacer(Modifier.width(16.dp))

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = JameoGreen
                        )
                    ) { Text("Cancelar") }
                }
            } else {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JameoGreen,
                        contentColor = Color.White
                    )
                ) { Text("Volver") }
            }
        }

        // -------------------------------
        // DIALOGO PREMIUM
        // -------------------------------
        if (showDialog && isAddingMode) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "Agregar al Meal Plan",
                        fontWeight = FontWeight.Bold,
                        color = JameoGreen
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

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
                                label = { Text("Tipo") },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expandedType)
                                },
                                colors = textFieldColorsPremium2(),
                                shape = RoundedCornerShape(12.dp)
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
                                label = { Text("Día") },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expandedDay)
                                },
                                colors = textFieldColorsPremium2(),
                                shape = RoundedCornerShape(12.dp)
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
                    Button(
                        onClick = {
                            if (selectedType.isBlank()) return@Button

                            val request = AddRecipeRequest(
                                recipeId = recipeId,
                                type = selectedType,
                                day = selectedDay
                            )
                            viewModel.addRecipeToMealPlan(mealPlanId, request)
                            showDialog = false
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = JameoGreen)
                    ) { Text("Agregar", color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar", color = JameoGreen)
                    }
                }
            )
        }
    }
}

@Composable
fun InfoRowPremium(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Text(value, color = Color.Black)
    }
}

@Composable
fun textFieldColorsPremium2() = TextFieldDefaults.colors(
    focusedIndicatorColor = JameoGreen,
    unfocusedIndicatorColor = JameoGreen.copy(alpha = 0.4f),
    focusedLabelColor = JameoGreen,
    unfocusedLabelColor = Color.Gray,
    focusedContainerColor = Color(0xFFF3F6F4),
    unfocusedContainerColor = Color(0xFFF3F6F4),
)