@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upc.jameofit.recipe.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.recipe.data.model.CategoryResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTypeResponse
import pe.edu.upc.jameofit.recipe.presentation.viewmodel.RecipeViewModel
import pe.edu.upc.jameofit.shared.presentation.components.ErrorSnackbarHost
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader
import pe.edu.upc.jameofit.shared.presentation.components.showErrorOnce
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

@Composable
fun RecipeCreateScreen(
    currentUserId: Long,
    initialCategoryId: Long,
    viewModel: RecipeViewModel,
    onRecipeCreated: () -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val recipeName by viewModel.recipeName.collectAsState()
    val recipeDescription by viewModel.recipeDescription.collectAsState()
    val preparationTime by viewModel.preparationTime.collectAsState()
    val difficulty by viewModel.difficulty.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedRecipeType by viewModel.selectedRecipeType.collectAsState()

    val categories by viewModel.categories.collectAsState()
    val recipeTypes by viewModel.recipeTypes.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val recipeCreationSuccess by viewModel.recipeCreationSuccess.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        viewModel.loadRecipeTypes()
    }

    LaunchedEffect(categories, initialCategoryId) {
        if (initialCategoryId != 0L && categories.isNotEmpty() && selectedCategory == null) {
            categories.firstOrNull { it.id == initialCategoryId }?.let {
                viewModel.selectCategory(it)
            }
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            scope.launch { snackbarHostState.showErrorOnce(errorMessage!!) }
            viewModel.resetErrorMessage()
        }
    }

    LaunchedEffect(recipeCreationSuccess) {
        if (recipeCreationSuccess == true) {
            Toast.makeText(ctx, "Receta creada exitosamente!", Toast.LENGTH_SHORT).show()
            viewModel.resetRecipeCreationSuccess()
            onRecipeCreated()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
                Text(
                    "Crear nueva Receta",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }


            TextField(
                value = recipeName,
                onValueChange = { viewModel.updateRecipeName(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre de la Receta") },
                singleLine = true,
                colors = defaultTextFieldColors(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                value = recipeDescription,
                onValueChange = { viewModel.updateRecipeDescription(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descripción") },
                maxLines = 5,
                colors = defaultTextFieldColors(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                value = preparationTime,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && (newValue.isEmpty() || newValue.length == 1 || newValue[0] != '0')) {
                        viewModel.updatePreparationTime(newValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tiempo de preparación") },
                placeholder = { Text("Ej: 30 (medido en minutos)") },
                singleLine = true,
                colors = defaultTextFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )
            val prepTimeError = preparationTime.toIntOrNull()?.let { it <= 0 } == true
            if (prepTimeError) Assistive("El tiempo debe ser un número entero positivo")


            TextField(
                value = difficulty,
                onValueChange = { viewModel.updateDifficulty(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Dificultad (Ej: Fácil, Media, Difícil)") },
                singleLine = true,
                colors = defaultTextFieldColors(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            var categoryExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = selectedCategory?.name ?: "Selecciona una categoría",
                    onValueChange = {},
                    label = { Text("Categoría") },
                    trailingIcon = { TrailingIcon(categoryExpanded) },
                    singleLine = true,
                    colors = defaultTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.selectCategory(category)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            var recipeTypeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = recipeTypeExpanded,
                onExpandedChange = { recipeTypeExpanded = !recipeTypeExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = selectedRecipeType?.name ?: "Selecciona un tipo de receta",
                    onValueChange = {},
                    label = { Text("Tipo de Receta") },
                    trailingIcon = { TrailingIcon(recipeTypeExpanded) },
                    singleLine = true,
                    colors = defaultTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = recipeTypeExpanded,
                    onDismissRequest = { recipeTypeExpanded = false }
                ) {
                    recipeTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                viewModel.selectRecipeType(type)
                                recipeTypeExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.createRecipe(currentUserId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoBlue,
                    contentColor = Color.White
                ),
                enabled = !isLoading
            ) {
                Text(
                    "Crear Receta",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        ErrorSnackbarHost(hostState = snackbarHostState)
        FullscreenLoader(visible = isLoading)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun Assistive(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
}

@Composable
private fun defaultTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFF0F0F0),
    unfocusedContainerColor = Color(0xFFF0F0F0),
    focusedIndicatorColor = JameoGreen,
    unfocusedIndicatorColor = JameoGreen,
    focusedLabelColor = JameoGreen,
    unfocusedLabelColor = Color.Gray
)